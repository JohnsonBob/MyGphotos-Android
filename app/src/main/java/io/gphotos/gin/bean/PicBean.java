package io.gphotos.gin.bean;

import com.google.gson.annotations.SerializedName;

public class PicBean {
    public Integer id;
    @SerializedName("pic")
    public String imageUrl;
    public String md5;
    @SerializedName("raw_name")
    public String rawName;
}
