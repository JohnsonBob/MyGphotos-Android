package io.gphotos.gin.bean;

import com.google.gson.annotations.SerializedName;

public class AlbumBean {
    public String cover;
    public Integer id;
    @SerializedName("meeting_id")
    public Integer meetingId;
    public String subtitle;
    @SerializedName("subtitle_en")
    public String subtitleEn;
    public String title;
    @SerializedName("title_en")
    public String titleEn;
    public int type;
}
