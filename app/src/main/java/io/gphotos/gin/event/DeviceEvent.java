package io.gphotos.gin.event;

public class DeviceEvent {
    public static final int ACTION_DEVICE_CONNECT = 3;
    public static final int ACTION_DEVICE_OFF = 2;
    public static final int ACTION_DEVICE_ON = 1;
    public int action;
    public String description;

    public DeviceEvent(int i, String str) {
        this.action = i;
        this.description = str;
    }
}
