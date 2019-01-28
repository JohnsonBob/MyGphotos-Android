package io.gphotos.gin.lib3.ptp;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;
import io.gphotos.gin.lib3.ptp.model.ObjectInfo;
import java.io.File;
import java.util.List;

public interface Camera {

    public interface CameraListener {
        void onBulbExposureTime(int i);

        void onBulbStarted();

        void onBulbStopped();

        void onCameraStarted(Camera camera);

        void onCameraStopped(Camera camera);

        void onCapturedPictureReceived(int i, String str, Bitmap bitmap, Bitmap bitmap2);

        void onError(String str);

        void onFocusEnded(boolean z);

        void onFocusPointsChanged();

        void onFocusStarted();

        void onLiveViewData(LiveViewData liveViewData);

        void onLiveViewStarted();

        void onLiveViewStopped();

        void onNoCameraFound();

        void onNoCompatibleCameraFound(Context context, UsbDevice usbDevice);

        void onObjectAdded(int i, int i2);

        void onPropertyChanged(int i, int i2);

        void onPropertyDescChanged(int i, int[] iArr);

        void onPropertyStateChanged(int i, boolean z);
    }

    public static class DriveLens {
        public static final int Far = 2;
        public static final int Hard = 3;
        public static final int Medium = 2;
        public static final int Near = 1;
        public static final int Soft = 1;
    }

    public static class Property {
        public static final int ApertureValue = 2;
        public static final int AvailableShots = 7;
        public static final int BatteryLevel = 6;
        public static final int ColorTemperature = 8;
        public static final int CurrentExposureIndicator = 14;
        public static final int CurrentFocusPoint = 13;
        public static final int ExposureCompensation = 16;
        public static final int ExposureMeteringMode = 11;
        public static final int FocusMeteringMode = 12;
        public static final int FocusMode = 9;
        public static final int FocusPoints = 15;
        public static final int IsoSpeed = 3;
        public static final int PictureStyle = 10;
        public static final int ShootingMode = 5;
        public static final int ShutterSpeed = 1;
        public static final int Whitebalance = 4;

    }

    public interface RetrieveImageInfoListener {
        void onImageInfoRetrieved(int i, ObjectInfo objectInfo, Bitmap bitmap);
    }

    public interface RetrieveImageListener {
        void onBytesRetrieved(int i, byte[] bArr);

        void onImageRetrieved(int i, Bitmap bitmap);
    }

    public interface StorageInfoListener {
        void onAllStoragesFound();

        void onImageHandlesRetrieved(int[] iArr);

        void onStorageFound(int i, String str);
    }

    public interface WorkerListener {
        void onWorkerEnded();

        void onWorkerStarted();
    }

    void capture();

    void driveLens(int i, int i2);

    void focus();

    String getBiggestPropertyValue(int i);

    String getDeviceInfo();

    String getDeviceName();

    List<FocusPoint> getFocusPoints();

    void getLiveViewPicture(LiveViewData liveViewData);

    int getProperty(int i);

    int[] getPropertyDesc(int i);

    boolean getPropertyEnabledState(int i);

    boolean isAutoFocusSupported();

    boolean isDriveLensSupported();

    boolean isHistogramSupported();

    boolean isLiveViewAfAreaSupported();

    boolean isLiveViewOpen();

    boolean isLiveViewSupported();

    boolean isSessionOpen();

    boolean isSettingPropertyPossible(int i);

    Integer propertyToIcon(int i, int i2);

    String propertyToString(int i, int i2);

    void retrieveImage(RetrieveImageListener retrieveImageListener, int i);

    void retrieveImageHandles(StorageInfoListener storageInfoListener, int i, int i2);

    void retrieveImageInfo(RetrieveImageInfoListener retrieveImageInfoListener, int i);

    void retrievePicture(int i);

    void retrieveStorages(StorageInfoListener storageInfoListener);

    void setCapturedPictureSampleSize(int i);

    void setLiveView(boolean z);

    void setLiveViewAfArea(float f, float f2);

    void setProperty(int i, int i2);

    void setWorkerListener(WorkerListener workerListener);

    void writeDebugInfo(File file);
}
