package io.gphotos.gin.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("album_id")
    public long albumId;
    public long id;
    @SerializedName("pic_height")
    public String imageHeight;
    @SerializedName("pic")
    public String imageUrl;
    @SerializedName("pic_width")
    public String imageWidth;
    @SerializedName("meeting_id")
    public long meetingId;
}
