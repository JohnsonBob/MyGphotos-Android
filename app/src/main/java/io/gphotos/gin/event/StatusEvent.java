package io.gphotos.gin.event;

public class StatusEvent {
    public static final int ACTION_BATTERY = 1;
    public static final int ACTION_CAMERA = 3;
    public static final int ACTION_CAMERA_OFF = 5;
    public static final int ACTION_CURRENT_ACTIVITY = 7;
    public static final int ACTION_DB_UPDATED = 4;
    public static final int ACTION_HEART_BEAT = 8;
    public static final int ACTION_HEART_BEAT_ERROR = 9;
    public static final int ACTION_NETWORK = 2;
    public static final int ACTION_NETWORK_ERROR = 6;
    public static final int ACTION_TEST = 0;
    public int action;
    public String description;
    public String json;
    public long pId;

    public StatusEvent(int i, String str) {
        this.action = i;
        this.description = str;
    }
}
