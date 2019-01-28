package io.gphotos.gin.manager;

public class DeviceManager {
    private static DeviceManager instance = new DeviceManager();
    private String mCameraModel;

    private DeviceManager() {
    }

    public static DeviceManager getInstance() {
        return instance;
    }

    public void setCameraModel(String str) {
        this.mCameraModel = str;
    }

    public String getCameraModel() {
        if (this.mCameraModel == null) {
            return "";
        }
        return this.mCameraModel;
    }

    public boolean isCameraConnect() {
        return this.mCameraModel != null;
    }
}
