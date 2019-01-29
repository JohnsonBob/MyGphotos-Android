package io.gphotos.gin.lib3.ptp;

import android.graphics.Bitmap;
import android.hardware.usb.UsbRequest;
import android.os.Handler;
import android.util.Log;

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
import java.util.concurrent.TimeUnit;

public abstract class PtpCamera implements Camera {
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

    class WorkerThread extends Thread implements IO {
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

        WorkerThread(PtpCamera arg1, io.gphotos.gin.lib3.ptp.PtpCamera arg2) {
            this(arg1);
        }

        private WorkerThread(PtpCamera ptpCamera1) {

            this.bigInSize = 0x4000;
            this.fullInSize = 0x4000;
        }



        public void handleCommand(Command command) {
            String tag = PtpCamera.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("handling command ");
            stringBuilder.append(command.getClass().getSimpleName());
            Log.i(tag, stringBuilder.toString());
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

        private void notifyWorkEnded() {
            WorkerListener v0 = PtpCamera.this.workerListener;
            if (v0 != null) {
                v0.onWorkerEnded();
            }
        }

        private void notifyWorkStarted() {
            WorkerListener v0 = PtpCamera.this.workerListener;
            if (v0 != null) {
                v0.onWorkerStarted();
            }
        }

        public void run() {
            Object v0_1 = null;
            this.notifyWorkStarted();
            this.maxPacketOutSize = PtpCamera.this.connection.getMaxPacketOutSize();
            this.maxPacketInSize = PtpCamera.this.connection.getMaxPacketInSize();
            if (this.maxPacketOutSize > 0) {
                int v3 = 65535;
                if (this.maxPacketOutSize > v3) {
                } else {
                    if (this.maxPacketInSize > 0) {
                        if (this.maxPacketInSize > v3) {
                        } else {
                            this.smallIn = ByteBuffer.allocate(Math.max(this.maxPacketInSize, this.maxPacketOutSize));
                            this.smallIn.order(ByteOrder.LITTLE_ENDIAN);
                            this.bigIn1 = ByteBuffer.allocate(0x4000);
                            this.bigIn1.order(ByteOrder.LITTLE_ENDIAN);
                            this.bigIn2 = ByteBuffer.allocate(0x4000);
                            this.bigIn2.order(ByteOrder.LITTLE_ENDIAN);
                            this.bigIn3 = ByteBuffer.allocate(0x4000);
                            this.bigIn3.order(ByteOrder.LITTLE_ENDIAN);
                            this.fullIn = ByteBuffer.allocate(this.fullInSize);
                            this.fullIn.order(ByteOrder.LITTLE_ENDIAN);
                            this.r1 = PtpCamera.this.connection.createInRequest();
                            this.r2 = PtpCamera.this.connection.createInRequest();
                            this.r3 = PtpCamera.this.connection.createInRequest();
                            while (true) {
                                synchronized (this) {
                                    try {
                                        if (this.stop) {
                                            this.r3.close();
                                            this.r2.close();
                                            this.r1.close();
                                            this.notifyWorkEnded();
                                            return;
                                        }
                                    } catch (Throwable v0) {
                                        break;
                                    }

                                    if (this.lastEventCheck + 700 < System.currentTimeMillis()) {
                                        this.lastEventCheck = System.currentTimeMillis();
                                        PtpCamera.this.queueEventCheck();
                                    }
                                    try {
                                        v0_1 = PtpCamera.this.queue.poll(1000, TimeUnit.MILLISECONDS);

                                    } catch (InterruptedException e) {
                                        if (v0_1 == null) {
                                            continue;
                                        }

                                        ((PtpAction) v0_1).exec(((IO) this));
                                        continue;
                                    }
                                }
                            }

                        }
                    }

                    PtpCamera.this.onUsbError(String.format("usb initialization error: in size invalid %d", Integer.valueOf(this.maxPacketInSize)));
                    return;
                }
            }

            PtpCamera.this.onUsbError(String.format("Usb initialization error: out size invalid %d", Integer.valueOf(this.maxPacketOutSize)));
        }
    }

    private static final String TAG = "PtpCamera";
    protected boolean autoFocusSupported;
    protected boolean bulbSupported;
    protected boolean cameraIsCapturing;
    private final PtpUsbConnection connection;
    protected DeviceInfo deviceInfo;
    protected boolean driveLensSupported;
    protected final Handler handler;
    protected boolean histogramSupported;
    protected CameraListener listener;
    protected boolean liveViewAfAreaSupported;
    protected boolean liveViewOpen;
    protected boolean liveViewSupported;
    private int pictureSampleSize;
    protected final int productId;
    protected final Map properties;
    private final Map propertyDescriptions;
    protected final Set ptpInternalProperties;
    protected final Map ptpProperties;
    protected final Map ptpPropertyDesc;
    protected final Map ptpToVirtualProperty;
    protected final LinkedBlockingQueue queue;
    protected State state;
    private int transactionId;
    private final int vendorId;
    protected final Map virtualToPtpProperty;
    private WorkerListener workerListener;
    private final WorkerThread workerThread;

    static {
    }

    public PtpCamera(PtpUsbConnection arg3, CameraListener arg4, WorkerListener arg5) {
        super();
        this.workerThread = new WorkerThread(this, null);
        this.handler = new Handler();
        this.queue = new LinkedBlockingQueue();
        this.virtualToPtpProperty = new HashMap();
        this.ptpToVirtualProperty = new HashMap();
        this.ptpPropertyDesc = new HashMap();
        this.ptpProperties = new HashMap();
        this.properties = new HashMap();
        this.propertyDescriptions = new HashMap();
        this.ptpInternalProperties = new HashSet();
        this.connection = arg3;
        this.listener = arg4;
        this.workerListener = arg5;
        this.pictureSampleSize = 2;
        this.state = State.Starting;
        this.vendorId = arg3.getVendorId();
        this.productId = arg3.getProductId();
        this.queue.add(new GetDeviceInfoCommand(this));
        this.openSession();
        this.workerThread.start();
        Log.i(PtpCamera.TAG, String.format("Starting session for %04x %04x", Integer.valueOf(this.vendorId), Integer.valueOf(this.productId)));
    }

    static Map access$200(PtpCamera arg0) {
        return arg0.propertyDescriptions;
    }

    static PtpUsbConnection access$300(PtpCamera arg0) {
        return arg0.connection;
    }

    static void access$400(PtpCamera arg0, String arg1) {
        arg0.onUsbError(arg1);
    }

    static String access$500() {
        return PtpCamera.TAG;
    }

    static WorkerListener access$600(PtpCamera arg0) {
        return arg0.workerListener;
    }

    protected void addInternalProperty(int arg2) {
        this.ptpInternalProperties.add(Integer.valueOf(arg2));
    }

    protected void addPropertyMapping(int arg4, int arg5) {
        this.ptpToVirtualProperty.put(Integer.valueOf(arg5), Integer.valueOf(arg4));
        this.virtualToPtpProperty.put(Integer.valueOf(arg4), Integer.valueOf(arg5));
    }

    public void capture() {
        this.queue.add(new InitiateCaptureCommand(this));
    }

    protected void closeSession() {
        this.queue.add(new CloseSessionCommand(this));
    }

    public int currentTransactionId() {
        return this.transactionId;
    }

    public void enqueue(Command arg3, int arg4) {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                if (PtpCamera.this.state == State.Active) {
                    PtpCamera.this.queue.add(arg3);
                }
            }
        }, ((long) arg4));
    }

    public String getBiggestPropertyValue(int arg2) {
        Object v2 = this.virtualToPtpProperty.get(Integer.valueOf(arg2));
        if (v2 != null) {
            return PtpPropertyHelper.getBiggestValue(((Integer) v2).intValue());
        }

        return "";
    }

    public String getDeviceInfo() {
        String v0 = this.deviceInfo != null ? this.deviceInfo.toString() : "unknown";
        return v0;
    }

    public String getDeviceName() {
        String v0 = this.deviceInfo != null ? this.deviceInfo.model : "";
        return v0;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getProperty(int i) {
        return this.properties.containsKey(Integer.valueOf(i)) ? ((Integer) this.properties.get(Integer.valueOf(i))).intValue() : Integer.MAX_VALUE;

    }

    public int[] getPropertyDesc(int arg3) {
        if (this.propertyDescriptions.containsKey(Integer.valueOf(arg3))) {
            return (int[]) this.propertyDescriptions.get(Integer.valueOf(arg3));
        }

        return new int[0];
    }

    public boolean getPropertyEnabledState(int arg1) {
        return false;
    }

    public int getPtpProperty(int arg2) {
        Object v2 = this.ptpProperties.get(Integer.valueOf(arg2));
        return v2 != null ? ((Integer) v2).intValue() : 0;
    }

    public State getState() {
        return this.state;
    }

    public boolean isAutoFocusSupported() {
        return this.autoFocusSupported;
    }

    protected abstract boolean isBulbCurrentShutterSpeed();

    public boolean isDriveLensSupported() {
        return this.driveLensSupported;
    }

    public boolean isHistogramSupported() {
        return this.histogramSupported;
    }

    public boolean isLiveViewAfAreaSupported() {
        return this.liveViewAfAreaSupported;
    }

    public boolean isLiveViewOpen() {
        return this.liveViewOpen;
    }

    public boolean isLiveViewSupported() {
        return this.liveViewSupported;
    }

    public boolean isSessionOpen() {
        boolean v0 = this.state == State.Active ? true : false;
        return v0;
    }

    public int nextTransactionId() {
        int v0 = this.transactionId;
        this.transactionId = v0 + 1;
        return v0;
    }

    public void onBulbExposureTime(int arg3) {
        if (arg3 >= 0 && arg3 <= 360000) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onBulbExposureTime(arg3);
                    }
                }
            });
        }
    }

    public void onDeviceBusy(PtpAction arg3, boolean arg4) {
        Log.i(PtpCamera.TAG, "onDeviceBusy, sleeping a bit");
        if (arg4) {
            arg3.reset();
            this.queue.add(arg3);
        }

        long v3 = 200;
        try {
            Thread.sleep(v3);
            return;
        } catch (InterruptedException d) {
            return;
        }
    }

    public void onEventCameraCapture(boolean arg2) {
        this.cameraIsCapturing = arg2;
        if (this.isBulbCurrentShutterSpeed()) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener != null) {
                        if (PtpCamera.this.cameraIsCapturing) {
                            PtpCamera.this.listener.onBulbStarted();
                        } else {
                            PtpCamera.this.listener.onBulbStopped();
                        }
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

    public void onEventObjectAdded(int arg3, int arg4) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onObjectAdded(arg3, arg4);
                }
            }
        });
    }

    public void onFocusEnded(boolean arg3) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onFocusEnded(arg3);
                }
            }
        });
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

    public void onLiveViewReceived(LiveViewData arg3) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewData(arg3);
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

    protected abstract void onOperationCodesReceived(Set<Integer> set);

    public void onPictureReceived(int i, String str, Bitmap bitmap, Bitmap bitmap1) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCapturedPictureReceived(i, str, bitmap, bitmap1);
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

    public void onPropertyDescChanged(int arg3, DevicePropDesc arg4) {
        this.ptpPropertyDesc.put(Integer.valueOf(arg3), arg4);
        this.onPropertyDescChanged(arg3, arg4.description);
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

    public void onPtpWarning(String arg4) {
        String v0 = PtpCamera.TAG;
        Log.i(v0, "onPtpWarning: " + arg4);
    }

    public void onSessionClosed() {
        this.shutdownHard();
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCameraStopped(PtpCamera.this);
                }
            }
        });
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

    private void onUsbError(String arg4) {
        String v0 = PtpCamera.TAG;
        Log.e(v0, "onUsbError: " + arg4);
        this.queue.clear();
        this.shutdownHard();
        this.state = State.Error;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onError(String.format("Error in USB communication: %s", arg4));
                }
            }
        });
    }

    protected void openSession() {
        this.queue.add(new OpenSessionCommand(this));
    }

    public Integer propertyToIcon(int arg2, int arg3) {
        Object v2 = this.virtualToPtpProperty.get(Integer.valueOf(arg2));
        Integer v0 = null;
        if (v2 != null) {
            Integer v2_1 = PtpPropertyHelper.mapToDrawable(((Integer) v2).intValue(), arg3);
            if (v2_1 != null) {
            } else {
                v2_1 = v0;
            }

            return v2_1;
        }

        return v0;
    }

    public String propertyToString(int arg2, int arg3) {
        Object v2 = this.virtualToPtpProperty.get(Integer.valueOf(arg2));
        if (v2 != null) {
            String v2_1 = PtpPropertyHelper.mapToString(this.productId, ((Integer) v2).intValue(), arg3);
            if (v2_1 != null) {
            } else {
                v2_1 = "?";
            }

            return v2_1;
        }

        return "";
    }

    protected abstract void queueEventCheck();

    public void resetTransactionId() {
        this.transactionId = 0;
    }

    public void retrieveImage(RetrieveImageListener arg4, int arg5) {
        this.queue.add(new RetrieveImageAction(this, arg4, arg5, this.pictureSampleSize));
    }

    public void retrieveImageHandles(StorageInfoListener arg3, int arg4, int arg5) {
        this.queue.add(new GetObjectHandlesCommand(this, arg3, arg4, arg5));
    }

    public void retrieveImageInfo(RetrieveImageInfoListener arg3, int arg4) {
        this.queue.add(new RetrieveImageInfoAction(this, arg3, arg4));
    }

    public void retrievePicture(int arg4) {
        this.queue.add(new RetrievePictureAction(this, arg4, this.pictureSampleSize));
    }

    public void retrieveStorages(StorageInfoListener arg3) {
        this.queue.add(new GetStorageInfosAction(this, arg3));
    }

    public void setCapturedPictureSampleSize(int arg1) {
        this.pictureSampleSize = arg1;
    }

    public void setDeviceInfo(DeviceInfo arg4) {
        Log.i(PtpCamera.TAG, arg4.toString());
        this.deviceInfo = arg4;
        HashSet hashSet = new HashSet();
        for (int valueOf : deviceInfo.operationsSupported) {
            hashSet.add(Integer.valueOf(valueOf));
        }
        onOperationCodesReceived(hashSet);
    }

    public void setListener(CameraListener arg1) {
        this.listener = arg1;
    }

    public void setProperty(int i, int i2) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        if (num != null && this.ptpPropertyDesc.containsKey(num)) {
            this.queue.add(new SetDevicePropValueCommand(this, num.intValue(), i2, ((DevicePropDesc) this.ptpPropertyDesc.get(num)).datatype));
        }
    }

    public void setWorkerListener(WorkerListener arg1) {
        this.workerListener = arg1;
    }

    public void shutdown() {
        this.state = State.Stoping;
        this.workerThread.lastEventCheck = System.currentTimeMillis() + 1000000;
        this.queue.clear();
        if (this.liveViewOpen) {
            this.setLiveView(false);
        }

        this.closeSession();
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

    public void writeDebugInfo(File arg2) {
        try {
            FileWriter v0 = new FileWriter(arg2);
            v0.append(this.deviceInfo.toString());
            v0.close();
            return;
        } catch (IOException e) {
            return;
        }
    }
}

