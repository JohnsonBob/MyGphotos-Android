package io.gphotos.gin.lib3.ptp;

import android.graphics.Bitmap;
import android.hardware.usb.UsbRequest;
import android.os.Handler;
import androidx.core.os.EnvironmentCompat;
import android.util.Log;
import com.raizlabs.android.dbflow.sql.language.Operator.Operation;

import io.gphotos.gin.lib3.ptp.commands.CloseSessionCommand;
import io.gphotos.gin.lib3.ptp.commands.Command;
import io.gphotos.gin.lib3.ptp.commands.GetDeviceInfoCommand;
import io.gphotos.gin.lib3.ptp.commands.GetDevicePropValueCommand;
import io.gphotos.gin.lib3.ptp.commands.GetObjectHandlesCommand;
import io.gphotos.gin.lib3.ptp.commands.GetStorageInfosAction;
import io.gphotos.gin.lib3.ptp.commands.InitiateCaptureCommand;
import io.gphotos.gin.lib3.ptp.commands.OpenSessionCommand;
import io.gphotos.gin.lib3.ptp.commands.RetrieveImageAction;
import io.gphotos.gin.lib3.ptp.commands.RetrieveImageInfoAction;
import io.gphotos.gin.lib3.ptp.commands.RetrievePictureAction;
import io.gphotos.gin.lib3.ptp.commands.SetDevicePropValueCommand;
import io.gphotos.gin.lib3.ptp.model.DeviceInfo;
import io.gphotos.gin.lib3.ptp.model.DevicePropDesc;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class PtpCamera implements Camera {
    private static final String TAG = "PtpCamera";
    protected boolean autoFocusSupported;
    protected boolean bulbSupported;
    protected boolean cameraIsCapturing;
    private final PtpUsbConnection connection;
    protected DeviceInfo deviceInfo;
    protected boolean driveLensSupported;
    protected final Handler handler = new Handler();
    protected boolean histogramSupported;
    protected CameraListener listener;
    protected boolean liveViewAfAreaSupported;
    protected boolean liveViewOpen;
    protected boolean liveViewSupported;
    private int pictureSampleSize;
    protected final int productId;
    protected final Map<Integer, Integer> properties = new HashMap();
    private final Map<Integer, int[]> propertyDescriptions = new HashMap();
    protected final Set<Integer> ptpInternalProperties = new HashSet();
    protected final Map<Integer, Integer> ptpProperties = new HashMap();
    protected final Map<Integer, DevicePropDesc> ptpPropertyDesc = new HashMap();
    protected final Map<Integer, Integer> ptpToVirtualProperty = new HashMap();
    protected final LinkedBlockingQueue<PtpAction> queue = new LinkedBlockingQueue();
    protected State state;
    private int transactionId;
    private final int vendorId;
    protected final Map<Integer, Integer> virtualToPtpProperty = new HashMap();
    private WorkerListener workerListener;
    private final WorkerThread workerThread = new WorkerThread(this, null);

    public interface IO {
        void handleCommand(Command command);
    }

    enum State {
        Starting,
        Active,
        Stoping,
        Stopped,
        Error
    }

    private class WorkerThread extends Thread implements IO {
        private ByteBuffer bigIn1;
        private ByteBuffer bigIn2;
        private ByteBuffer bigIn3;
        private final int bigInSize;
        private ByteBuffer fullIn;
        private int fullInSize;
        private long lastEventCheck;
        private int maxPacketInSize;
        private int maxPacketOutSize;
        private UsbRequest r1;
        private UsbRequest r2;
        private UsbRequest r3;
        private ByteBuffer smallIn;
        public boolean stop;

        private WorkerThread() {
            this.bigInSize = 16384;
            this.fullInSize = 16384;
        }

        //这里错误
        WorkerThread(PtpCamera ptpCamera, PtpCamera anonymousClass1) {
            this();
        }


        public void run() {

            throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.lib3.ptp.PtpCamera.WorkerThread.run():void");
        }

        public void handleCommand(Command command) {
            String access$500 = PtpCamera.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("handling command ");
            stringBuilder.append(command.getClass().getSimpleName());
            Log.i(access$500, stringBuilder.toString());
            ByteBuffer byteBuffer = this.smallIn;
            byteBuffer.position(0);
            command.encodeCommand(byteBuffer);
            int position = byteBuffer.position();
            int i = 0;
            if (PtpCamera.this.connection.bulkTransferOut(byteBuffer.array(), position, 30000) < position) {
                PtpCamera.this.onUsbError(String.format("Code CP %d %d", new Object[]{Integer.valueOf(i), Integer.valueOf(position)}));
                return;
            }
            if (command.hasDataToSend()) {
                byteBuffer = ByteBuffer.allocate(PtpCamera.this.connection.getMaxPacketOutSize());
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                command.encodeData(byteBuffer);
                position = byteBuffer.position();
                if (PtpCamera.this.connection.bulkTransferOut(byteBuffer.array(), position, 30000) < position) {
                    PtpCamera.this.onUsbError(String.format("Code DP %d %d", new Object[]{Integer.valueOf(i), Integer.valueOf(position)}));
                    return;
                }
            }
            while (!command.hasResponseReceived()) {
                i = this.maxPacketInSize;
                ByteBuffer byteBuffer2 = this.smallIn;
                byteBuffer2.position(0);
                int i2 = 0;
                while (i2 == 0) {
                    i2 = PtpCamera.this.connection.bulkTransferIn(byteBuffer2.array(), i, 30000);
                }
                if (i2 < 12) {
                    PtpCamera.this.onUsbError(String.format("Couldn't read header, only %d bytes available!", new Object[]{Integer.valueOf(i2)}));
                    return;
                }
                i = byteBuffer2.getInt();
                if (i2 < i) {
                    if (i > this.fullInSize) {
                        this.fullInSize = (int) (((double) i) * 1.5d);
                        this.fullIn = ByteBuffer.allocate(this.fullInSize);
                        this.fullIn.order(ByteOrder.LITTLE_ENDIAN);
                    }
                    ByteBuffer byteBuffer3 = this.fullIn;
                    byteBuffer3.position(0);
                    byteBuffer3.put(byteBuffer2.array(), 0, i2);
                    int i3 = i - i2;
                    int min = Math.min(16384, i3);
                    i3 = Math.max(0, Math.min(16384, i3 - min));
                    this.r1.queue(this.bigIn1, min);
                    if (i3 > 0) {
                        this.r2.queue(this.bigIn2, i3);
                    }
                    while (i2 < i) {
                        int max = Math.max(0, Math.min(16384, ((i - i2) - min) - i3));
                        if (max > 0) {
                            this.bigIn3.position(0);
                            this.r3.queue(this.bigIn3, max);
                        }
                        if (min > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn1.array(), 0, byteBuffer3.array(), i2, min);
                            i2 += min;
                        }
                        min = Math.max(0, Math.min(16384, ((i - i2) - i3) - max));
                        if (min > 0) {
                            this.bigIn1.position(0);
                            this.r1.queue(this.bigIn1, min);
                        }
                        if (i3 > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn2.array(), 0, byteBuffer3.array(), i2, i3);
                            i2 += i3;
                        }
                        i3 = Math.max(0, Math.min(16384, ((i - i2) - min) - max));
                        if (i3 > 0) {
                            this.bigIn2.position(0);
                            this.r2.queue(this.bigIn2, i3);
                        }
                        if (max > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn3.array(), 0, byteBuffer3.array(), i2, max);
                            i2 += max;
                        }
                    }
                    byteBuffer2 = byteBuffer3;
                }
                byteBuffer2.position(0);
                try {
                    command.receivedRead(byteBuffer2);
                } catch (RuntimeException e) {
                    String access$5002 = PtpCamera.TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Exception ");
                    stringBuilder2.append(e.getLocalizedMessage());
                    Log.e(access$5002, stringBuilder2.toString());
                    e.printStackTrace();
                    PtpCamera.this.onPtpError(String.format("Error parsing %s with length %d", new Object[]{command.getClass().getSimpleName(), Integer.valueOf(i)}));
                }
            }
        }

        private void notifyWorkStarted() {
            WorkerListener access$600 = PtpCamera.this.workerListener;
            if (access$600 != null) {
                access$600.onWorkerStarted();
            }
        }

        private void notifyWorkEnded() {
            WorkerListener access$600 = PtpCamera.this.workerListener;
            if (access$600 != null) {
                access$600.onWorkerEnded();
            }
        }
    }

    public boolean getPropertyEnabledState(int i) {
        return false;
    }

    protected abstract boolean isBulbCurrentShutterSpeed();

    protected abstract void onOperationCodesReceived(Set<Integer> set);

    protected abstract void queueEventCheck();

    public PtpCamera(PtpUsbConnection ptpUsbConnection, CameraListener cameraListener, WorkerListener workerListener) {
        this.connection = ptpUsbConnection;
        this.listener = cameraListener;
        this.workerListener = workerListener;
        this.pictureSampleSize = 2;
        this.state = State.Starting;
        this.vendorId = ptpUsbConnection.getVendorId();
        this.productId = ptpUsbConnection.getProductId();
        this.queue.add(new GetDeviceInfoCommand(this));
        openSession();
        this.workerThread.start();
        Log.i(TAG, String.format("Starting session for %04x %04x", new Object[]{Integer.valueOf(this.vendorId), Integer.valueOf(this.productId)}));
    }

    protected void addPropertyMapping(int i, int i2) {
        this.ptpToVirtualProperty.put(Integer.valueOf(i2), Integer.valueOf(i));
        this.virtualToPtpProperty.put(Integer.valueOf(i), Integer.valueOf(i2));
    }

    protected void addInternalProperty(int i) {
        this.ptpInternalProperties.add(Integer.valueOf(i));
    }

    public void setListener(CameraListener cameraListener) {
        this.listener = cameraListener;
    }

    public void shutdown() {
        this.state = State.Stoping;
        this.workerThread.lastEventCheck = System.currentTimeMillis() + 1000000;
        this.queue.clear();
        if (this.liveViewOpen) {
            setLiveView(false);
        }
        closeSession();
    }

    public void shutdownHard() {
        this.state = State.Stopped;
        synchronized (this.workerThread) {
            this.workerThread.stop = true;
        }
        if (this.connection != null) {
            this.connection.close();
        }
    }

    public State getState() {
        return this.state;
    }

    public int nextTransactionId() {
        int i = this.transactionId;
        this.transactionId = i + 1;
        return i;
    }

    public int currentTransactionId() {
        return this.transactionId;
    }

    public void resetTransactionId() {
        this.transactionId = 0;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        Log.i(TAG, deviceInfo.toString());
        this.deviceInfo = deviceInfo;
        Set hashSet = new HashSet();
        for (int valueOf : deviceInfo.operationsSupported) {
            hashSet.add(Integer.valueOf(valueOf));
        }
        onOperationCodesReceived(hashSet);
    }

    public void enqueue(final Command command, int i) {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                if (PtpCamera.this.state == State.Active) {
                    PtpCamera.this.queue.add(command);
                }
            }
        }, (long) i);
    }

    public int getPtpProperty(int i) {
        Integer num = (Integer) this.ptpProperties.get(Integer.valueOf(i));
        return num != null ? num.intValue() : 0;
    }

    public void onSessionOpened() {
        this.state = State.Active;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCameraStarted(PtpCamera.this);
                }
            }
        });
    }

    public void onSessionClosed() {
        shutdownHard();
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCameraStopped(PtpCamera.this);
                }
            }
        });
    }

    public void onPropertyChanged(int i, final int i2) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("p ");
        stringBuilder.append(i);
        stringBuilder.append(" ");
        stringBuilder.append(i2);
        Log.i(str, stringBuilder.toString());
        this.ptpProperties.put(Integer.valueOf(i), Integer.valueOf(i2));
        final Integer num = (Integer) this.ptpToVirtualProperty.get(Integer.valueOf(i));
        String str2 = TAG;
        String str3 = "onPropertyChanged %s %s(%d)";
        Object[] objArr = new Object[3];
        objArr[0] = PtpConstants.propertyToString(i);
        objArr[1] = num != null ? propertyToString(num.intValue(), i2) : "";
        objArr[2] = Integer.valueOf(i2);
        Log.d(str2, String.format(str3, objArr));
        if (num != null) {
            this.handler.post(new Runnable() {
                public void run() {
                    PtpCamera.this.properties.put(num, Integer.valueOf(i2));
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onPropertyChanged(num.intValue(), i2);
                    }
                }
            });
        }
    }

    public void onPropertyDescChanged(int i, final int[] iArr) {
        Log.d(TAG, String.format("onPropertyDescChanged %s:\n%s", new Object[]{PtpConstants.propertyToString(i), Arrays.toString(iArr)}));
        final Integer num = (Integer) this.ptpToVirtualProperty.get(Integer.valueOf(i));
        if (num != null) {
            this.handler.post(new Runnable() {
                public void run() {
                    PtpCamera.this.propertyDescriptions.put(num, iArr);
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onPropertyDescChanged(num.intValue(), iArr);
                    }
                }
            });
        }
    }

    public void onPropertyDescChanged(int i, DevicePropDesc devicePropDesc) {
        this.ptpPropertyDesc.put(Integer.valueOf(i), devicePropDesc);
        onPropertyDescChanged(i, devicePropDesc.description);
    }

    public void onLiveViewStarted() {
        this.liveViewOpen = true;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStarted();
                }
            }
        });
    }

    public void onLiveViewRestarted() {
        this.liveViewOpen = true;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStarted();
                }
            }
        });
    }

    public void onLiveViewStopped() {
        this.liveViewOpen = false;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStopped();
                }
            }
        });
    }

    public void onLiveViewReceived(final LiveViewData liveViewData) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewData(liveViewData);
                }
            }
        });
    }

    public void onPictureReceived(int i, String str, Bitmap bitmap, Bitmap bitmap2) {
        final int i2 = i;
        final String str2 = str;
        final Bitmap bitmap3 = bitmap;
        final Bitmap bitmap4 = bitmap2;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCapturedPictureReceived(i2, str2, bitmap3, bitmap4);
                }
            }
        });
    }

    public void onEventCameraCapture(boolean z) {
        this.cameraIsCapturing = z;
        if (isBulbCurrentShutterSpeed()) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener == null) {
                        return;
                    }
                    if (PtpCamera.this.cameraIsCapturing) {
                        PtpCamera.this.listener.onBulbStarted();
                    } else {
                        PtpCamera.this.listener.onBulbStopped();
                    }
                }
            });
        }
    }

    public void onEventDevicePropChanged(int i) {
        if ((this.ptpToVirtualProperty.containsKey(Integer.valueOf(i)) || this.ptpInternalProperties.contains(Integer.valueOf(i))) && this.ptpPropertyDesc.containsKey(Integer.valueOf(i))) {
            this.queue.add(new GetDevicePropValueCommand(this, i, ((DevicePropDesc) this.ptpPropertyDesc.get(Integer.valueOf(i))).datatype));
        }
    }

    public void onEventObjectAdded(final int i, final int i2) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onObjectAdded(i, i2);
                }
            }
        });
    }

    public void onBulbExposureTime(final int i) {
        if (i >= 0 && i <= 360000) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onBulbExposureTime(i);
                    }
                }
            });
        }
    }

    public void onFocusStarted() {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onFocusStarted();
                }
            }
        });
    }

    public void onFocusEnded(final boolean z) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onFocusEnded(z);
                }
            }
        });
    }

    public void onDeviceBusy(PtpAction ptpAction, boolean z) {
        Log.i(TAG, "onDeviceBusy, sleeping a bit");
        if (z) {
            ptpAction.reset();
            this.queue.add(ptpAction);
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException unused) {
        }
    }

    public void onPtpWarning(String str) {
        String str2 = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onPtpWarning: ");
        stringBuilder.append(str);
        Log.i(str2, stringBuilder.toString());
    }

    public void onPtpError(final String str) {
        String str2 = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onPtpError: ");
        stringBuilder.append(str);
        Log.e(str2, stringBuilder.toString());
        this.state = State.Error;
        if (this.state == State.Active) {
            shutdown();
        } else {
            shutdownHard();
        }
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onError(str);
                }
            }
        });
    }

    private void onUsbError(final String str) {
        String str2 = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onUsbError: ");
        stringBuilder.append(str);
        Log.e(str2, stringBuilder.toString());
        this.queue.clear();
        shutdownHard();
        this.state = State.Error;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onError(String.format("Error in USB communication: %s", new Object[]{str}));
                }
            }
        });
    }

    protected void openSession() {
        this.queue.add(new OpenSessionCommand(this));
    }

    protected void closeSession() {
        this.queue.add(new CloseSessionCommand(this));
    }

    public void setWorkerListener(WorkerListener workerListener) {
        this.workerListener = workerListener;
    }

    public String getDeviceName() {
        return this.deviceInfo != null ? this.deviceInfo.model : "";
    }

    public boolean isSessionOpen() {
        return this.state == State.Active;
    }

    public int getProperty(int i) {
        return this.properties.containsKey(Integer.valueOf(i)) ? ((Integer) this.properties.get(Integer.valueOf(i))).intValue() : Integer.MAX_VALUE;
    }

    public int[] getPropertyDesc(int i) {
        if (this.propertyDescriptions.containsKey(Integer.valueOf(i))) {
            return (int[]) this.propertyDescriptions.get(Integer.valueOf(i));
        }
        return new int[0];
    }

    public void setProperty(int i, int i2) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        if (num != null && this.ptpPropertyDesc.containsKey(num)) {
            this.queue.add(new SetDevicePropValueCommand(this, num.intValue(), i2, ((DevicePropDesc) this.ptpPropertyDesc.get(num)).datatype));
        }
    }

    public String propertyToString(int i, int i2) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        if (num == null) {
            return "";
        }
        String mapToString = PtpPropertyHelper.mapToString(this.productId, num.intValue(), i2);
        if (mapToString == null) {
            mapToString = Operation.EMPTY_PARAM;
        }
        return mapToString;
    }

    public Integer propertyToIcon(int i, int i2) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        if (num == null) {
            return null;
        }
        num = PtpPropertyHelper.mapToDrawable(num.intValue(), i2);
        if (num == null) {
            num = null;
        }
        return num;
    }

    public String getBiggestPropertyValue(int i) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        return num != null ? PtpPropertyHelper.getBiggestValue(num.intValue()) : "";
    }

    public void capture() {
        this.queue.add(new InitiateCaptureCommand(this));
    }

    public boolean isAutoFocusSupported() {
        return this.autoFocusSupported;
    }

    public boolean isLiveViewSupported() {
        return this.liveViewSupported;
    }

    public boolean isLiveViewAfAreaSupported() {
        return this.liveViewAfAreaSupported;
    }

    public boolean isHistogramSupported() {
        return this.histogramSupported;
    }

    public boolean isLiveViewOpen() {
        return this.liveViewOpen;
    }

    public boolean isDriveLensSupported() {
        return this.driveLensSupported;
    }

    public String getDeviceInfo() {
        return this.deviceInfo != null ? this.deviceInfo.toString() : EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public void writeDebugInfo(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(this.deviceInfo.toString());
            fileWriter.close();
        } catch (IOException unused) {
        }
    }

    public void retrievePicture(int i) {
        this.queue.add(new RetrievePictureAction(this, i, this.pictureSampleSize));
    }

    public void retrieveStorages(StorageInfoListener storageInfoListener) {
        this.queue.add(new GetStorageInfosAction(this, storageInfoListener));
    }

    public void retrieveImageHandles(StorageInfoListener storageInfoListener, int i, int i2) {
        this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, i2));
    }

    public void retrieveImageInfo(RetrieveImageInfoListener retrieveImageInfoListener, int i) {
        this.queue.add(new RetrieveImageInfoAction(this, retrieveImageInfoListener, i));
    }

    public void retrieveImage(RetrieveImageListener retrieveImageListener, int i) {
        this.queue.add(new RetrieveImageAction(this, retrieveImageListener, i, this.pictureSampleSize));
    }

    public void setCapturedPictureSampleSize(int i) {
        this.pictureSampleSize = i;
    }
}
