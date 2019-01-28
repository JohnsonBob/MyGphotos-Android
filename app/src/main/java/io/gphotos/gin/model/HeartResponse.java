package io.gphotos.gin.model;

import com.google.gson.annotations.SerializedName;
import io.gphotos.gin.bean.AlbumBean;
import io.gphotos.gin.bean.MeetBean;
import io.gphotos.gin.bean.PhotographerBean;
import io.gphotos.gin.manager.AccountManager;

public class HeartResponse {
    public AlbumBean album;
    public Config config;
    public MeetBean meeting;
    public PhotographerBean photographer;

    public static class Config {
        @SerializedName("upload_server_addr")
        public String uploadServerUrl;
    }

    public String getInfo() {
        String str = "";
        String str2 = "";
        String str3 = "";
        if (this.meeting != null) {
            str = this.meeting.name;
        }
        if (this.album != null) {
            str2 = this.album.title;
        }
        if (this.photographer != null) {
            str3 = this.photographer.name;
        }
        return String.format("账户：%s \n摄影师：%s \n当前专辑：%s \n当前活动：%s \n", new Object[]{AccountManager.getInstance().getMobile(), str3, str, str2});
    }
}
