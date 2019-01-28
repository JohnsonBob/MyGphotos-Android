package io.gphotos.gin.bean;

import com.google.gson.annotations.SerializedName;

public class MeetBean {
    public String alias;
    @SerializedName("company_id")
    public String companyId;
    @SerializedName("company_name")
    public String companyName;
    public String cover;
    @SerializedName("end_time")
    public String endTime;
    public Integer id;
    public String intro;
    @SerializedName("url")
    public String meetingUrl;
    public String name;
    @SerializedName("start_time")
    public String startTime;
}
