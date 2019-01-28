package io.gphotos.gin.event;

public class UploadEvent {
    public static final int STATUS_ERROR = 2;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_ONGOING = 1;
    public static final int STATUS_UPLOADED = 3;
    public int cntLeft;
    public int cntRetry;
    public int cntTotal;
    public int cntUploaded;
    public String description;
    public int percent;
    public int status;

    public UploadEvent(int i, String str) {
        this.status = i;
        this.description = str;
    }
}
