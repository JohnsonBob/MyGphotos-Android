package io.gphotos.gin.lib3.ptp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import io.gphotos.gin.lib3.ptp.Camera.CameraListener;
import java.util.Map.Entry;

public class PtpUsbService implements PtpService {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final String TAG = PtpUsbService.class.getSimpleName();
    private PtpCamera camera;
    private final Handler handler = new Handler();
    private CameraListener listener;
    private final BroadcastReceiver permissonReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (PtpUsbService.ACTION_USB_PERMISSION.equals(intent.getAction())) {
                PtpUsbService.this.unregisterPermissionReceiver(context);
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
                    if (intent.getBooleanExtra("permission", false)) {
                        PtpUsbService.this.connect(context, usbDevice);
                    }
                }
            }
        }
    };
    Runnable shutdownRunnable = new Runnable() {
        public void run() {
            PtpUsbService.this.shutdown();
        }
    };
    private final UsbManager usbManager;

    public PtpUsbService(Context context) {
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    public void setCameraListener(CameraListener cameraListener) {
        this.listener = cameraListener;
        if (this.camera != null) {
            this.camera.setListener(cameraListener);
        }
    }

    public void initialize(Context context, Intent intent) {
        this.handler.removeCallbacks(this.shutdownRunnable);
        if (this.camera != null) {
            Log.i(this.TAG, "initialize: camera available");
            if (this.camera.getState() == PtpCamera.State.Active) {
                if (this.listener != null) {
                    this.listener.onCameraStarted(this.camera);
                }
                return;
            }
            String str = this.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("initialize: camera not active, satet ");
            stringBuilder.append(this.camera.getState());
            Log.i(str, stringBuilder.toString());
            this.camera.shutdownHard();
        }
        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
        if (usbDevice != null) {
            Log.i(this.TAG, "initialize: got device through intent");
            connect(context, usbDevice);
        } else {
            Log.i(this.TAG, "initialize: looking for compatible camera");
            usbDevice = lookupCompatibleDevice(this.usbManager);
            if (usbDevice != null) {
                registerPermissionReceiver(context);
                this.usbManager.requestPermission(usbDevice, PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0));
            } else {
                this.listener.onNoCameraFound();
            }
        }
    }

    public void shutdown() {
        Log.i(this.TAG, "shutdown");
        if (this.camera != null) {
            this.camera.shutdown();
            this.camera = null;
        }
    }

    public void lazyShutdown() {
        Log.i(this.TAG, "lazy shutdown");
        this.handler.postDelayed(this.shutdownRunnable, 4000);
    }

    private void registerPermissionReceiver(Context context) {
        Log.i(this.TAG, "register permission receiver");
        context.registerReceiver(this.permissonReceiver, new IntentFilter(ACTION_USB_PERMISSION));
    }

    private void unregisterPermissionReceiver(Context context) {
        Log.i(this.TAG, "unregister permission receiver");
        context.unregisterReceiver(this.permissonReceiver);
    }

    private UsbDevice lookupCompatibleDevice(UsbManager usbManager) {
        for (Entry value : usbManager.getDeviceList().entrySet()) {
            UsbDevice usbDevice = (UsbDevice) value.getValue();
            if (usbDevice.getVendorId() != PtpConstants.CanonVendorId) {
                if (usbDevice.getVendorId() == PtpConstants.NikonVendorId) {
                }
            }
            return usbDevice;
        }
        return null;
    }

    private boolean connect(Context context, UsbDevice usbDevice) {
        if (this.camera != null) {
            this.camera.shutdown();
            this.camera = null;
        }
        int interfaceCount = usbDevice.getInterfaceCount();
        for (int i = 0; i < interfaceCount; i++) {
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (usbInterface.getEndpointCount() == 3) {
                int endpointCount = usbInterface.getEndpointCount();
                UsbEndpoint usbEndpoint = null;
                UsbEndpoint usbEndpoint2 = usbEndpoint;
                for (int i2 = 0; i2 < endpointCount; i2++) {
                    UsbEndpoint endpoint = usbInterface.getEndpoint(i2);
                    if (endpoint.getType() == 2) {
                        if (endpoint.getDirection() == 128) {
                            usbEndpoint = endpoint;
                        } else if (endpoint.getDirection() == 0) {
                            usbEndpoint2 = endpoint;
                        }
                    }
                }
                if (!(usbEndpoint == null || usbEndpoint2 == null)) {
                    Log.i(this.TAG, "Found compatible USB interface");
                    String str = this.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Interface class ");
                    stringBuilder.append(usbInterface.getInterfaceClass());
                    Log.i(str, stringBuilder.toString());
                    str = this.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Interface subclass ");
                    stringBuilder.append(usbInterface.getInterfaceSubclass());
                    Log.i(str, stringBuilder.toString());
                    str = this.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Interface protocol ");
                    stringBuilder.append(usbInterface.getInterfaceProtocol());
                    Log.i(str, stringBuilder.toString());
                    str = this.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Bulk out max size ");
                    stringBuilder.append(usbEndpoint2.getMaxPacketSize());
                    Log.i(str, stringBuilder.toString());
                    str = this.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Bulk in max size ");
                    stringBuilder.append(usbEndpoint.getMaxPacketSize());
                    Log.i(str, stringBuilder.toString());
                    if (!this.usbManager.hasPermission(usbDevice)) {
                        registerPermissionReceiver(context);
                        this.usbManager.requestPermission(usbDevice, PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0));
                    } else if (usbDevice.getVendorId() == PtpConstants.CanonVendorId) {
                        this.camera = new EosCamera(new PtpUsbConnection(this.usbManager.openDevice(usbDevice), usbEndpoint, usbEndpoint2, usbDevice.getVendorId(), usbDevice.getProductId()), this.listener, new WorkerNotifier(context));
                    } else if (usbDevice.getVendorId() == PtpConstants.NikonVendorId) {
                        this.camera = new NikonCamera(new PtpUsbConnection(this.usbManager.openDevice(usbDevice), usbEndpoint, usbEndpoint2, usbDevice.getVendorId(), usbDevice.getProductId()), this.listener, new WorkerNotifier(context));
                    }
                    return true;
                }
            }
        }
        if (this.listener != null) {
            this.listener.onError("No compatible camera found");
            this.listener.onNoCompatibleCameraFound(context, usbDevice);
        }
        return false;
    }
}
