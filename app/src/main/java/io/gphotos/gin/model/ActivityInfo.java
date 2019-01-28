package io.gphotos.gin.model;

import com.google.gson.annotations.SerializedName;
import io.gphotos.gin.bean.AlbumBean;
import io.gphotos.gin.bean.MeetBean;

public class ActivityInfo {
    @SerializedName("active_album")
    public AlbumBean activeAlbum;
    public MeetBean meeting;

    public String getInfo() {
        String str = "";
        String str2 = "";
        if (this.meeting != null) {
            str = this.meeting.name;
        }
        if (this.activeAlbum != null) {
            str2 = this.activeAlbum.title;
        }
        return String.format("当前专辑：%s \n当前活动：%s  \n", new Object[]{str, str2});
    }
}
