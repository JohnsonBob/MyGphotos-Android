package io.gphotos.gin.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.mtp.MtpDevice;
import android.mtp.MtpDeviceInfo;
import android.mtp.MtpObjectInfo;
import android.util.Log;
import io.gphotos.gin.Database.ImageModel;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.lib3.ptp.PtpConstants.ObjectFormat;
import io.gphotos.gin.util.FileUtil;
import io.reactivex.annotations.SchedulerSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class MtpUsbService implements MtpService {
    private final String ACTION_USB_PERMISSION = "io.gphotos.gin.USB_PERMISSION";
    private final String TAG = MtpUsbService.class.getSimpleName();
    private MtpDevice camera;
    private UsbDeviceConnection connection;
    private Context context;
    private final BroadcastReceiver permissionReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:26:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0061  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x004c  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x003c  */
        /* JADX WARNING: Removed duplicated region for block: B:26:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0061  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x004c  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x003c  */
        /* JADX WARNING: Removed duplicated region for block: B:26:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0061  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x004c  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x003c  */
        public void onReceive(android.content.Context r5, android.content.Intent r6) {
            /*
            r4 = this;
            r0 = r6.getAction();
            r1 = r0.hashCode();
            r2 = -2114103349; // 0xffffffff81fd57cb float:-9.30635E-38 double:NaN;
            r3 = 0;
            if (r1 == r2) goto L_0x002d;
        L_0x000e:
            r2 = -1608292967; // 0xffffffffa0236599 float:-1.3840253E-19 double:NaN;
            if (r1 == r2) goto L_0x0023;
        L_0x0013:
            r2 = 37460922; // 0x23b9bba float:1.3783282E-37 double:1.85081546E-316;
            if (r1 == r2) goto L_0x0019;
        L_0x0018:
            goto L_0x0037;
        L_0x0019:
            r1 = "io.gphotos.gin.USB_PERMISSION";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0037;
        L_0x0021:
            r0 = 0;
            goto L_0x0038;
        L_0x0023:
            r1 = "android.hardware.usb.action.USB_DEVICE_DETACHED";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0037;
        L_0x002b:
            r0 = 2;
            goto L_0x0038;
        L_0x002d:
            r1 = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0037;
        L_0x0035:
            r0 = 1;
            goto L_0x0038;
        L_0x0037:
            r0 = -1;
        L_0x0038:
            switch(r0) {
                case 0: goto L_0x0061;
                case 1: goto L_0x004c;
                case 2: goto L_0x003c;
                default: goto L_0x003b;
            };
        L_0x003b:
            goto L_0x0076;
        L_0x003c:
            r5 = org.greenrobot.eventbus.EventBus.getDefault();
            r6 = new io.gphotos.gin.event.StatusEvent;
            r0 = 5;
            r1 = "";
            r6.<init>(r0, r1);
            r5.post(r6);
            goto L_0x0076;
        L_0x004c:
            r0 = org.greenrobot.eventbus.EventBus.getDefault();
            r1 = new io.gphotos.gin.event.StatusEvent;
            r2 = 3;
            r3 = "正在检测...";
            r1.<init>(r2, r3);
            r0.post(r1);
            r0 = io.gphotos.gin.service.MtpUsbService.this;
            r0.initialize(r5, r6);
            goto L_0x0076;
        L_0x0061:
            r0 = "device";
            r0 = r6.getParcelableExtra(r0);
            r0 = (android.hardware.usb.UsbDevice) r0;
            r1 = "permission";
            r6 = r6.getBooleanExtra(r1, r3);
            if (r6 == 0) goto L_0x0076;
        L_0x0071:
            r6 = io.gphotos.gin.service.MtpUsbService.this;
            r6.connect(r5, r0);
        L_0x0076:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.service.MtpUsbService.1.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    private final UsbManager usbManager;

    public MtpUsbService(Context context) {
        Log.d(this.TAG, "init");
        this.context = context;
        this.usbManager = (UsbManager) context.getSystemService("usb");
    }

    private void connect(Context context, UsbDevice usbDevice) {
        this.connection = this.usbManager.openDevice(usbDevice);
        this.camera = new MtpDevice(usbDevice);
        this.camera.open(this.connection);
        EventBus.getDefault().post(new StatusEvent(3, this.camera.getDeviceInfo().getModel()));
        Log.d(this.TAG, "connect ok");
    }

    private void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter("io.gphotos.gin.USB_PERMISSION");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        context.registerReceiver(this.permissionReceiver, intentFilter);
    }

    private void unregisterReceiver(Context context) {
        context.unregisterReceiver(this.permissionReceiver);
    }

    public void testJob() {
        Log.d(this.TAG, "testJob");
        new Thread() {
            public void run() {
                MtpUsbService.this.test();
            }
        }.start();
    }

    private List test() {
        List arrayList = new ArrayList();
        if (this.camera == null) {
            return arrayList;
        }
        MtpDeviceInfo deviceInfo = this.camera.getDeviceInfo();
        String str = SchedulerSupport.NONE;
        if (deviceInfo != null) {
            str = deviceInfo.getSerialNumber();
        }
        Log.d(this.TAG, str);
        int[] storageIds = this.camera.getStorageIds();
        if (storageIds == null) {
            return arrayList;
        }
        for (int objectHandles : storageIds) {
            int[] objectHandles2 = this.camera.getObjectHandles(objectHandles, ObjectFormat.EXIF_JPEG, 0);
            if (objectHandles2 == null) {
                return arrayList;
            }
            for (int i : objectHandles2) {
                MtpObjectInfo objectInfo = this.camera.getObjectInfo(i);
                if (objectInfo != null) {
                    byte[] thumbnail = this.camera.getThumbnail(i);
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
                    imageModel.filePath = createFileNameForCameraImage;
                    imageModel.save();
                    Log.d(this.TAG, imageModel.toString());
                    arrayList.add(imageModel);
                }
            }
        }
        String str2 = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("size = ");
        stringBuilder.append(arrayList.size());
        Log.d(str2, stringBuilder.toString());
        return arrayList;
    }

    public void initialize(Context context, Intent intent) {
        Log.d(this.TAG, "initialize");
        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
        if (usbDevice != null) {
            Log.d(this.TAG, "initialize connect");
            connect(context, usbDevice);
            return;
        }
        Log.d(this.TAG, "lookup");
        usbDevice = lookupCompatibleDevice(this.usbManager);
        if (usbDevice != null) {
            registerReceiver(context);
            this.usbManager.requestPermission(usbDevice, PendingIntent.getBroadcast(context, 0, new Intent("io.gphotos.gin.USB_PERMISSION"), 0));
        }
    }

    private UsbDevice lookupCompatibleDevice(UsbManager usbManager) {
        Iterator it = usbManager.getDeviceList().values().iterator();
        return it.hasNext() ? (UsbDevice) it.next() : null;
    }

    public void shutdown() {
        if (this.camera != null) {
            this.camera.close();
            this.camera = null;
            this.connection.close();
            this.connection = null;
        }
    }
}
