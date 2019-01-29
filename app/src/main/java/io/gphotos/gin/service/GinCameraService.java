package io.gphotos.gin.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.mtp.MtpDevice;
import android.mtp.MtpDeviceInfo;
import android.mtp.MtpObjectInfo;
import android.os.IBinder;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Operator.Operation;

import androidx.annotation.Nullable;
import io.gphotos.gin.Database.ImageModel;
import io.gphotos.gin.api.GphotoClient;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.lib3.ptp.Camera;
import io.gphotos.gin.lib3.ptp.Camera.RetrieveImageInfoListener;
import io.gphotos.gin.lib3.ptp.Camera.RetrieveImageListener;
import io.gphotos.gin.lib3.ptp.Camera.StorageInfoListener;
import io.gphotos.gin.lib3.ptp.PtpConstants.ObjectFormat;
import io.gphotos.gin.lib3.ptp.PtpService;
import io.gphotos.gin.lib3.ptp.PtpService.Singleton;
import io.gphotos.gin.lib3.ptp.model.ObjectInfo;
import io.gphotos.gin.manager.DeviceManager;
import io.gphotos.gin.manager.UploadManager;
import io.gphotos.gin.service.WrapperListener.WrapperCameraListener;
import io.gphotos.gin.util.DateTimeUtil;
import io.gphotos.gin.util.FileUtil;
import io.gphotos.gin.util.TUtil;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

public class GinCameraService extends Service implements WrapperCameraListener {
    public static final int ACTION_HEART = 4;
    public static final int ACTION_INITIALIZE = 1;
    public static final int ACTION_JUST = 0;
    public static final int ACTION_SCAN = 2;
    public static final int ACTION_SHUTDOWN = 3;
    public final String TAG = GinCameraService.class.getSimpleName();
    private BroadcastReceiver cameraConnectReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                Object obj = -1;
                int hashCode = action.hashCode();
                if (hashCode != -2114103349) {
                    if (hashCode == -1608292967 && action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                        obj = 1;
                    }
                } else if (action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                    obj = null;
                }
                /*switch (obj) {
                    case null:
                        GinCameraService.this.initialize(context, intent);
                        break;
                    case 1:
                        GinCameraService.this.shutdown(context, intent);
                        break;
                }*/
                if(obj == null){
                    GinCameraService.this.initialize(context, intent);
                }else if(obj == Integer.valueOf("1")){
                    GinCameraService.this.shutdown(context, intent);
                }
            }
        }
    };
    private int cntForNewImage = 0;
    Disposable disposable;
    private MtpDevice mtpCamera;
    private MtpService mtpService;
    private Camera ptpCamera;
    private WrapperListener ptpListener;
    private PtpService ptpService;

    public class CBImageInfo {
        Bitmap bitmap;
        byte[] buffer;
        int hack;
        int objectHandle;
        ObjectInfo objectInfo;
        Bitmap thumbnail;
        int which = 0;

        public CBImageInfo(int i, ObjectInfo objectInfo, Bitmap bitmap, int i2) {
            this.objectHandle = i;
            this.objectInfo = objectInfo;
            this.thumbnail = bitmap;
            this.hack = i2;
            this.which = 0;
        }

        public CBImageInfo(int i, ObjectInfo objectInfo, Bitmap bitmap, int i2, int i3) {
            this.objectHandle = i;
            this.objectInfo = objectInfo;
            this.thumbnail = bitmap;
            this.hack = i2;
            this.which = i3;
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onImageInfoRetrieved(int i, ObjectInfo objectInfo, Bitmap bitmap) {
    }

    public void onImageRetrieved(int i, Bitmap bitmap) {
    }

    private void initialize(Context context, Intent intent) {
        if (this.ptpService != null) {
            this.ptpService.initialize(context, intent);
        }
    }

    private void shutdown(Context context, Intent intent) {
        if (this.ptpService != null) {
            this.ptpService.shutdown();
        }
    }

    public void onCreate() {
        super.onCreate();
        d("Service onCreate");
        this.ptpService = Singleton.getInstance(this);
        this.mtpService = MtpService.Singleton.getInstance(this);
        this.ptpListener = new WrapperListener();
        this.ptpListener.setCameraListener(this);
        this.ptpService.setCameraListener(this.ptpListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        registerReceiver(this.cameraConnectReceiver, intentFilter);
        UploadManager.getInstance().initialize();
        UploadManager.getInstance().startToUpload();
        startBeatHeart();
    }

    public void onDestroy() {
        super.onDestroy();
        d("Service onDestroy");
        unregisterReceiver(this.cameraConnectReceiver);
        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        d("onStartCommand");
        if (intent == null) {
            return super.onStartCommand(intent, i, i2);
        }
        int intExtra = intent.getIntExtra("action", 0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onStartCommand : ");
        stringBuilder.append(intExtra);
        d(stringBuilder.toString());
        switch (intExtra) {
            case 1:
                initialize(this, intent);
                break;
            case 2:
                getImages();
                break;
            case 3:
                shutdown(this, intent);
                break;
        }
        return super.onStartCommand(intent, i, i2);
    }

    public void onCameraStarted(Camera camera) {
        d("onCameraStarted");
        this.ptpCamera = camera;
        String deviceName = camera.getDeviceName();
        EventBus.getDefault().post(new StatusEvent(3, deviceName));
        DeviceManager.getInstance().setCameraModel(deviceName);
    }

    public void onCameraStopped(Camera camera) {
        d("onCameraStopped");
        EventBus.getDefault().post(new StatusEvent(5, camera.getDeviceName()));
        DeviceManager.getInstance().setCameraModel(null);
    }

    public void onNoCameraFound() {
        d("onNoCameraFound");
    }

    public void onError(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onError : ");
        stringBuilder.append(str);
        d(stringBuilder.toString());
    }

    public void onNoCompatibleCameraFound(Context context, UsbDevice usbDevice) {
        d("onNoCompatibleCameraFound");
    }

    public void onObjectAdded(int i, int i2) {
        getImageByAddedDetect(i, i2);
    }

    private void getImages() {
        d("getImages");
        if (this.ptpCamera != null) {
            getPtpImages();
        } else if (this.mtpCamera != null) {
            getMtpImages();
        } else {
            notifyGalleryNewImageAdded("ERROR");
            TUtil.error(this, "未检测到相机");
        }
    }

    private void getImageByAddedDetect(int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getImageByAddedDetect ");
        stringBuilder.append(i);
        stringBuilder.append(" ");
        stringBuilder.append(i2);
        d(stringBuilder.toString());
        if (this.ptpCamera != null) {
            getImageInfo(new int[]{i}).flatMap(new Function<CBImageInfo, ObservableSource<CBImageInfo>>() {
                public ObservableSource<CBImageInfo> apply(CBImageInfo cBImageInfo) throws Exception {
                    return GinCameraService.this.getImageOriginal(cBImageInfo);
                }
            }).map(new Function<CBImageInfo, Boolean>() {
                public Boolean apply(CBImageInfo cBImageInfo) throws Exception {
                    return Boolean.valueOf(GinCameraService.this.processImageForPtp(cBImageInfo));
                }
            }).observeOn(Schedulers.io()).subscribe();
        }
    }

    private void getMtpImages() {
        if (this.mtpCamera != null) {
            MtpDeviceInfo deviceInfo = this.mtpCamera.getDeviceInfo();
            if (deviceInfo != null) {
                deviceInfo.getSerialNumber();
            }
            int[] storageIds = this.mtpCamera.getStorageIds();
            if (storageIds != null) {
                for (int objectHandles : storageIds) {
                    int[] objectHandles2 = this.mtpCamera.getObjectHandles(objectHandles, ObjectFormat.EXIF_JPEG, 0);
                    if (objectHandles2 != null) {
                        for (int i : objectHandles2) {
                            MtpObjectInfo objectInfo = this.mtpCamera.getObjectInfo(i);
                            if (objectInfo != null) {
                                byte[] thumbnail = this.mtpCamera.getThumbnail(i);
                                String createFileNameForCameraImage = FileUtil.createFileNameForCameraImage("", objectInfo.getName(), String.valueOf(objectInfo.getDateCreated()));
                                FileUtil.saveBytes2File(thumbnail, createFileNameForCameraImage);
                                ImageModel imageModel = new ImageModel();
                                imageModel.dateCreated = objectInfo.getDateCreated();
                                imageModel.format = objectInfo.getFormat();
                                imageModel.imagePixDepth = objectInfo.getImagePixDepth();
                                imageModel.imagePixHeight = objectInfo.getImagePixHeight();
                                imageModel.imagePixWidth = objectInfo.getImagePixWidth();
                                imageModel.compressedSize = objectInfo.getCompressedSize();
                                imageModel.keywords = objectInfo.getKeywords();
                                imageModel.name = objectInfo.getName();
                                imageModel.parent = objectInfo.getParent();
                                imageModel.objectHandle = objectInfo.getObjectHandle();
                                imageModel.thumbCompressedSize = objectInfo.getThumbCompressedSize();
                                imageModel.thumbFormat = objectInfo.getThumbFormat();
                                imageModel.thumbPixHeight = objectInfo.getThumbPixHeight();
                                imageModel.thumbPixWidth = objectInfo.getThumbPixWidth();
                                imageModel.storageId = objectInfo.getStorageId();
                                imageModel.sequenceNumber = objectInfo.getSequenceNumber();
                                imageModel.thumbnailPath = createFileNameForCameraImage;
                            }
                        }
                    }
                }
            }
        }
    }

    private void getPtpImages() {
        d("getPtpImages");
        getStorageHandle().flatMap(new Function<Integer, ObservableSource<int[]>>() {
            public ObservableSource<int[]> apply(Integer num) throws Exception {
                return GinCameraService.this.getImageHandlesByStorage(num.intValue());
            }
        }).flatMap(new Function<int[], ObservableSource<CBImageInfo>>() {
            public ObservableSource<CBImageInfo> apply(int[] iArr) throws Exception {
                return GinCameraService.this.getImageInfo(iArr);
            }
        }).flatMap(new Function<CBImageInfo, ObservableSource<CBImageInfo>>() {
            public ObservableSource<CBImageInfo> apply(CBImageInfo cBImageInfo) throws Exception {
                return GinCameraService.this.getImageOriginal(cBImageInfo);
            }
        }).map(new Function<CBImageInfo, Boolean>() {
            public Boolean apply(CBImageInfo cBImageInfo) throws Exception {
                return Boolean.valueOf(GinCameraService.this.processImageForPtp(cBImageInfo));
            }
        }).observeOn(Schedulers.io()).subscribe();
    }

    private boolean processImageForPtp(CBImageInfo cBImageInfo) {
        int i = cBImageInfo.objectHandle;
        ObjectInfo objectInfo = cBImageInfo.objectInfo;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("processImageForPtp ... ");
        stringBuilder.append(objectInfo.filename);
        stringBuilder.append(" : index = ");
        stringBuilder.append(cBImageInfo.hack);
        d(stringBuilder.toString());
        if (!FileUtil.isValidFileType(objectInfo.filename)) {
            return false;
        }
        String createFileNameForCameraImage = FileUtil.createFileNameForCameraImage("", objectInfo.filename, objectInfo.captureDate);
        String createFileNameForCameraImageOriginal = FileUtil.createFileNameForCameraImageOriginal("", objectInfo.filename, objectInfo.captureDate);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("file path ");
        stringBuilder2.append(createFileNameForCameraImage);
        d(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("file path ");
        stringBuilder2.append(createFileNameForCameraImageOriginal);
        d(stringBuilder2.toString());
        FileUtil.saveBitmap2File(cBImageInfo.thumbnail, createFileNameForCameraImage);
        if (FileUtil.saveBytes2File(cBImageInfo.buffer, createFileNameForCameraImageOriginal) == null) {
            TUtil.error(this, "error File bytes");
            return false;
        }
        ImageModel imageModel = new ImageModel();
        imageModel.dateCreated = DateTimeUtil.getTimeOfDateStr(objectInfo.captureDate);
        imageModel.format = objectInfo.objectFormat;
        imageModel.imagePixDepth = objectInfo.imageBitDepth;
        imageModel.imagePixHeight = objectInfo.imagePixHeight;
        imageModel.imagePixWidth = objectInfo.imagePixWidth;
        imageModel.compressedSize = objectInfo.objectCompressedSize;
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("");
        stringBuilder3.append(objectInfo.keywords);
        imageModel.keywords = stringBuilder3.toString();
        imageModel.name = objectInfo.filename;
        imageModel.parent = objectInfo.parentObject;
        imageModel.objectHandle = i;
        imageModel.thumbCompressedSize = objectInfo.thumbCompressedSize;
        imageModel.thumbFormat = objectInfo.thumbFormat;
        imageModel.thumbPixHeight = objectInfo.thumbPixHeight;
        imageModel.thumbPixWidth = objectInfo.thumbPixWidth;
        imageModel.storageId = objectInfo.storageId;
        imageModel.sequenceNumber = objectInfo.sequenceNumber;
        imageModel.thumbnailPath = createFileNameForCameraImage;
        imageModel.filePath = createFileNameForCameraImageOriginal;
        imageModel.uploadStatus = 0;
        imageModel.isUploaded = false;
        addImageModelToList(imageModel);
        if (cBImageInfo.hack == -1) {
            notifyGalleryNewImageAdded("OVER");
        }
        return true;
    }

    private void addImageModelToList(ImageModel imageModel) {
        d("addImageModelToList");
        if (UploadManager.getInstance().addImageModelToList(imageModel)) {
            this.cntForNewImage++;
            if (this.cntForNewImage > 20) {
                notifyGalleryNewImageAdded();
                this.cntForNewImage = 0;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add it ");
            stringBuilder.append(imageModel.name);
            d(stringBuilder.toString());
            return;
        }
        d("dup : drop it");
    }

    private Observable<CBImageInfo> getImageOriginal(final CBImageInfo cBImageInfo) {
        return Observable.create(new ObservableOnSubscribe<CBImageInfo>() {
            public void subscribe(final ObservableEmitter<CBImageInfo> observableEmitter) throws Exception {
                if (GinCameraService.this.ptpCamera == null) {
                    observableEmitter.onComplete();
                } else if (cBImageInfo.objectInfo.objectFormat == ObjectFormat.EXIF_JPEG) {
                    GinCameraService.this.ptpCamera.retrieveImage(new RetrieveImageListener() {
                        public void onImageRetrieved(int i, Bitmap bitmap) {
                        }

                        public void onBytesRetrieved(int i, byte[] bArr) {
                            cBImageInfo.buffer = bArr;
                            observableEmitter.onNext(cBImageInfo);
                        }
                    }, cBImageInfo.objectHandle);
                } else {
                    GinCameraService.this.d("get wrong image format");
                }
            }
        });
    }

    private Observable<CBImageInfo> getImageInfo(final int[] iArr) {
        d("getImageInfo");
        return Observable.create(new ObservableOnSubscribe<CBImageInfo>() {
            public void subscribe(final ObservableEmitter<CBImageInfo> observableEmitter) throws Exception {
                if (GinCameraService.this.ptpCamera == null) {
                    observableEmitter.onComplete();
                    return;
                }
                int i = 0;
                int i2 = 0;
                while (i < iArr.length) {
                    int i3 = iArr[i];
                    if (!UploadManager.getInstance().isHandleDup(i3)) {
                        i2++;
                        final int i4 = i == iArr.length + -1 ? -1 : i;
                        GinCameraService.this.ptpCamera.retrieveImageInfo(new RetrieveImageInfoListener() {
                            public void onImageInfoRetrieved(int i, ObjectInfo objectInfo, Bitmap bitmap) {
                                observableEmitter.onNext(new CBImageInfo(i, objectInfo, bitmap, i4));
                            }
                        }, i3);
                    }
                    i++;
                }
                if (i2 == 0) {
                    GinCameraService.this.notifyGalleryNewImageAdded("OVER");
                    observableEmitter.onComplete();
                }
            }
        });
    }

    private Observable<int[]> getImageHandlesByStorage(final int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getImageHandlesByStorage :");
        stringBuilder.append(i);
        d(stringBuilder.toString());
        return Observable.create(new ObservableOnSubscribe<int[]>() {
            public void subscribe(final ObservableEmitter<int[]> observableEmitter) throws Exception {
                if (GinCameraService.this.ptpCamera == null) {
                    observableEmitter.onComplete();
                } else {
                    GinCameraService.this.ptpCamera.retrieveImageHandles(new StorageInfoListener() {
                        public void onStorageFound(int i, String str) {
                            observableEmitter.onComplete();
                        }

                        public void onAllStoragesFound() {
                            observableEmitter.onComplete();
                        }

                        public void onImageHandlesRetrieved(int[] iArr) {
                            observableEmitter.onNext(iArr);
                        }
                    }, i, ObjectFormat.EXIF_JPEG);
                }
            }
        });
    }

    private Observable<Integer> getStorageHandle() {
        d("getStorageHandle");
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            public void subscribe(final ObservableEmitter<Integer> observableEmitter) throws Exception {
                if (GinCameraService.this.ptpCamera == null) {
                    observableEmitter.onComplete();
                } else {
                    GinCameraService.this.ptpCamera.retrieveStorages(new StorageInfoListener() {
                        public void onImageHandlesRetrieved(int[] iArr) {
                        }

                        public void onStorageFound(int i, String str) {
                            GinCameraService ginCameraService = GinCameraService.this;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("storage found ");
                            stringBuilder.append(i);
                            stringBuilder.append(Operation.MINUS);
                            stringBuilder.append(str);
                            ginCameraService.d(stringBuilder.toString());
                            observableEmitter.onNext(Integer.valueOf(i));
                        }

                        public void onAllStoragesFound() {
                            observableEmitter.onComplete();
                        }
                    });
                }
            }
        });
    }

    private void d(String str) {
        Log.d(this.TAG, str);
    }

    private void notifyGalleryNewImageAdded() {
        notifyGalleryNewImageAdded("");
    }

    private void notifyGalleryNewImageAdded(String str) {
        EventBus.getDefault().post(new StatusEvent(4, str));
    }

    public static void startScanCamera(Context context) {
        Intent intent = new Intent(context, GinCameraService.class);
        intent.putExtra("action", 2);
        context.startService(intent);
    }

    public void startBeatHeart() {
        if (this.disposable == null || this.disposable.isDisposed()) {
            this.disposable = Flowable.interval(1, 6, TimeUnit.SECONDS).map(new Function<Long, Long>() {
                public Long apply(Long l) throws Exception {
                    GphotoClient.hearbeat();
                    return Long.valueOf(0);
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }
}
