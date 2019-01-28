package io.gphotos.gin.model;

import com.google.gson.annotations.SerializedName;
import io.gphotos.gin.bean.AlbumBean;
import io.gphotos.gin.bean.MeetBean;
import java.util.List;

public class ActivityInfoWrapper {
    @SerializedName("active_status")
    public int activeStatus;
    public AlbumBean album;
    public List<AlbumBean> albums;
    public String date;
    public long id;
    public MeetBean meeting;
    @SerializedName("photographer_id")
    public long photographerId;
}
