package io.gphotos.gin.model;

import com.google.gson.annotations.SerializedName;

public class UserConfig {
    @SerializedName("AlbumID")
    public long albumId;
    @SerializedName("Code")
    public String code;
    @SerializedName("ManualUpload")
    public String manualUpload;
    @SerializedName("MeetingID")
    public long meetingId;
    @SerializedName("Mobile")
    public String mobile;
    @SerializedName("ThumbSize")
    public int thumbSize;
}
