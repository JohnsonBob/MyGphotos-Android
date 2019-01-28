package io.gphotos.gin.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import io.gphotos.gin.lib3.ptp.Camera;
import io.gphotos.gin.lib3.ptp.Camera.CameraListener;
import io.gphotos.gin.lib3.ptp.Camera.RetrieveImageInfoListener;
import io.gphotos.gin.lib3.ptp.Camera.RetrieveImageListener;
import io.gphotos.gin.lib3.ptp.Camera.StorageInfoListener;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;
import io.gphotos.gin.lib3.ptp.model.ObjectInfo;

public class WrapperListener implements CameraListener, RetrieveImageListener, RetrieveImageInfoListener, StorageInfoListener {
    WrapperCameraListener listener;

    public interface WrapperCameraListener {
        void onCameraStarted(Camera camera);

        void onCameraStopped(Camera camera);

        void onError(String str);

        void onImageInfoRetrieved(int i, ObjectInfo objectInfo, Bitmap bitmap);

        void onImageRetrieved(int i, Bitmap bitmap);

        void onNoCameraFound();

        void onNoCompatibleCameraFound(Context context, UsbDevice usbDevice);

        void onObjectAdded(int i, int i2);
    }

    public void onAllStoragesFound() {
    }

    public void onBulbExposureTime(int i) {
    }

    public void onBulbStarted() {
    }

    public void onBulbStopped() {
    }

    public void onBytesRetrieved(int i, byte[] bArr) {
    }

    public void onCapturedPictureReceived(int i, String str, Bitmap bitmap, Bitmap bitmap2) {
    }

    public void onFocusEnded(boolean z) {
    }

    public void onFocusPointsChanged() {
    }

    public void onFocusStarted() {
    }

    public void onImageHandlesRetrieved(int[] iArr) {
    }

    public void onImageRetrieved(int i, Bitmap bitmap) {
    }

    public void onLiveViewData(LiveViewData liveViewData) {
    }

    public void onLiveViewStarted() {
    }

    public void onLiveViewStopped() {
    }

    public void onPropertyChanged(int i, int i2) {
    }

    public void onPropertyDescChanged(int i, int[] iArr) {
    }

    public void onPropertyStateChanged(int i, boolean z) {
    }

    public void onStorageFound(int i, String str) {
    }

    public void setCameraListener(WrapperCameraListener wrapperCameraListener) {
        this.listener = wrapperCameraListener;
    }

    public void onImageInfoRetrieved(int i, ObjectInfo objectInfo, Bitmap bitmap) {
        if (this.listener != null) {
            this.listener.onImageInfoRetrieved(i, objectInfo, bitmap);
        }
    }

    public void onCameraStarted(Camera camera) {
        if (this.listener != null) {
            this.listener.onCameraStarted(camera);
        }
    }

    public void onCameraStopped(Camera camera) {
        if (this.listener != null) {
            this.listener.onCameraStopped(camera);
        }
    }

    public void onNoCameraFound() {
        if (this.listener != null) {
            this.listener.onNoCameraFound();
        }
    }

    public void onNoCompatibleCameraFound(Context context, UsbDevice usbDevice) {
        if (this.listener != null) {
            this.listener.onNoCompatibleCameraFound(context, usbDevice);
        }
    }

    public void onError(String str) {
        if (this.listener != null) {
            this.listener.onError(str);
        }
    }

    public void onObjectAdded(int i, int i2) {
        if (this.listener != null) {
            this.listener.onObjectAdded(i, i2);
        }
    }
}
