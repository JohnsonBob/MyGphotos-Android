package io.gphotos.gin.api;

import io.gphotos.gin.bean.PicBean;
import io.gphotos.gin.model.ActivityInfo;
import io.gphotos.gin.model.ActivityInfoWrapper;
import io.gphotos.gin.model.BaseCallResponse;
import io.gphotos.gin.model.HeartResponse;
import io.gphotos.gin.model.HeartStatsWrapper;
import io.gphotos.gin.model.ListResponse;
import io.gphotos.gin.model.LoginResponse;
import io.gphotos.gin.model.UploadResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GphotoApi {
    @FormUrlEncoded
    @POST("/api/android/v1/app/photographer/checkPhotoUploaded")
    Call<BaseCallResponse<?>> checkPhotoUploaded(@Field("meeting_id") String str, @Field("md5") String str2);

    @FormUrlEncoded
    @POST("/api/android/v1/app/photographer/codeLogin")
    Call<BaseCallResponse<LoginResponse>> codeLogin(@Field("mobile") String str, @Field("code") String str2);

    @POST("/api/android/v1/app/photographer/getBindAlbumList")
    Call<BaseCallResponse<?>> getBindAlbumList();

    @POST("/api/android/v1/app/photographer/getBindMeetingList")
    Call<BaseCallResponse<ListResponse<ActivityInfoWrapper>>> getBindMeetingList();

    @POST("/api/android/v1/app/photographer/getInfo")
    Call<BaseCallResponse<ActivityInfo>> getInfo();

    @POST("/api/android/v1/app/photographer/heartbeat")
    Call<BaseCallResponse<HeartResponse>> heartbeat(@Body HeartStatsWrapper heartStatsWrapper);

    @FormUrlEncoded
    @POST("/api/android/v1/app/photographer/setBindMeetingAlbum")
    Call<BaseCallResponse<?>> setBindMeetingAlbum(@Field("id") String str, @Field("album_id") String str2);

    @FormUrlEncoded
    @POST("/api/android/v1/app/photographer/startShootAlbum")
    Call<BaseCallResponse<?>> startShootAlbum(@Field("id") String str);

    @POST("/api/android/v1/app/photographer/photo/uploadConfirm")
    @Multipart
    Call<BaseCallResponse<UploadResponse>> upload(@Part("album_id") RequestBody requestBody, @Part("md5") RequestBody requestBody2, @Part("shooting_time") RequestBody requestBody3, @Part MultipartBody.Part part);

    @POST("/api/android/v1/app/photographer/photo/uploadConfirm")
    @Multipart
    Call<BaseCallResponse<UploadResponse>> uploadSimple(@Part("album_id") RequestBody requestBody, @Part MultipartBody.Part part);

    @POST("/api/android/v1/app/photographer/photo/uploadConfirm")
    @Multipart
    Call<BaseCallResponse<UploadResponse>> uploadWithMd5(@Part("album_id") RequestBody requestBody, @Part("md5") RequestBody requestBody2, @Part MultipartBody.Part part);

    @POST("/api/android/v1/app/photographer/uploadedList")
    Call<BaseCallResponse<ListResponse<PicBean>>> uploadedList();
}
