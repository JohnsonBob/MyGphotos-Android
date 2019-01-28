package io.gphotos.gin.api;

import android.text.TextUtils;
import android.util.Log;
import io.gphotos.gin.event.ReqEvent;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.manager.AccountManager;
import io.gphotos.gin.manager.DeviceManager;
import io.gphotos.gin.manager.UploadManager;
import io.gphotos.gin.model.ActivityInfo;
import io.gphotos.gin.model.ActivityInfoWrapper;
import io.gphotos.gin.model.BaseCallResponse;
import io.gphotos.gin.model.HeartResponse;
import io.gphotos.gin.model.HeartStats;
import io.gphotos.gin.model.HeartStatsWrapper;
import io.gphotos.gin.model.ListResponse;
import io.gphotos.gin.model.LoginResponse;
import io.gphotos.gin.model.UploadResponse;
import io.gphotos.gin.model.UserConfig;
import io.gphotos.gin.util.PhoneUtil;
import io.gphotos.gin.util.SPUtil;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GphotoClient {
    private static final String BASE_URL = "https://api.51gphoto.com";
    private static String TAG = "API";
    private static long cntHeart;
    private static Retrofit mRetrofit;
    private static String token;

    public static void heartBeatStart() {
    }

    private GphotoClient() {
    }

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            Builder builder = new Builder();
            new HttpLoggingInterceptor().setLevel(Level.BODY);
            builder.addInterceptor(new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (TextUtils.isEmpty(GphotoClient.token)) {
                        GphotoClient.token = SPUtil.getToken();
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Bearer ");
                    stringBuilder.append(GphotoClient.token);
                    return chain.proceed(request.newBuilder().header("Authorization", stringBuilder.toString()).build());
                }
            });
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        }
        return mRetrofit;
    }

    public static void hearbeat() {
        GphotoApi gphotoApi = (GphotoApi) getInstance().create(GphotoApi.class);
        UserConfig userConfig = new UserConfig();
        String[] account = SPUtil.getAccount();
        if (account != null && account.length == 2) {
            userConfig.mobile = account[0];
            userConfig.code = account[1];
        }
        userConfig.manualUpload = "OFF";
        userConfig.meetingId = (long) AccountManager.getInstance().getMeetingId();
        userConfig.albumId = (long) AccountManager.getInstance().getActivityAlbumId();
        int imageUploadSetting = AccountManager.getInstance().getImageUploadSetting();
        if (imageUploadSetting == 0) {
            userConfig.thumbSize = 0;
        } else if (imageUploadSetting == 1) {
            userConfig.thumbSize = 2880;
        }
        DeviceManager instance = DeviceManager.getInstance();
        HeartStats.Builder builder = new HeartStats.Builder();
        builder.startedAt(System.currentTimeMillis()).account(userConfig.mobile).cameraConnected(instance.isCameraConnect()).cameraModel(instance.getCameraModel()).storageFree(PhoneUtil.getAvailableExternalMemorySize()).storageTotal(PhoneUtil.getTotalExternalMemorySize()).memoryTotal(PhoneUtil.getTotalInternalMemorySizeByMByte()).memoryFree(PhoneUtil.getAvailableInternalMemorySizeByMByte()).uploadOk(UploadManager.getInstance().getCntUploaded()).uploadQueued(UploadManager.getInstance().getCntLeft()).uploadPending(UploadManager.getInstance().getCntLeft()).userConfig(userConfig).libVersion("android-ptp").version("a1.7").networkOk(true);
        HeartStatsWrapper heartStatsWrapper = new HeartStatsWrapper();
        heartStatsWrapper.stats = builder.build();
        try {
            retrofit2.Response execute = gphotoApi.heartbeat(heartStatsWrapper).execute();
            if (!execute.isSuccessful() || execute.body() == null) {
                EventBus.getDefault().post(new StatusEvent(9, "系统服务：异常"));
            } else if (((BaseCallResponse) execute.body()).err == 0) {
                AccountManager.getInstance().updateHeartBeat((HeartResponse) ((BaseCallResponse) execute.body()).res);
                EventBus eventBus = EventBus.getDefault();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("系统服务：正常");
                long j = cntHeart;
                cntHeart = 1 + j;
                stringBuilder.append(j);
                eventBus.post(new StatusEvent(8, stringBuilder.toString()));
            } else {
                EventBus.getDefault().post(new StatusEvent(9, "系统服务：异常"));
                EventBus.getDefault().post(new ReqEvent(((BaseCallResponse) execute.body()).err, ((BaseCallResponse) execute.body()).msg));
            }
        } catch (Exception e) {
            Log.d("api", "heart exp");
            e.printStackTrace();
            EventBus.getDefault().post(new StatusEvent(9, "系统服务：异常"));
        }
    }

    public static void testLogin() {
        ((GphotoApi) getInstance().create(GphotoApi.class)).codeLogin("18501622646", "622646").enqueue(new Callback<BaseCallResponse<LoginResponse>>() {
            public void onResponse(Call<BaseCallResponse<LoginResponse>> call, retrofit2.Response<BaseCallResponse<LoginResponse>> response) {
                if (response.isSuccessful() && ((BaseCallResponse) response.body()).err == 0) {
                    SPUtil.saveToken(((LoginResponse) ((BaseCallResponse) response.body()).res).apiToken);
                }
            }

            public void onFailure(Call<BaseCallResponse<LoginResponse>> call, Throwable th) {
                Log.d(GphotoClient.TAG, "onFailure");
            }
        });
    }

    public static void testInfo() {
        ((GphotoApi) getInstance().create(GphotoApi.class)).getInfo().enqueue(new Callback<BaseCallResponse<ActivityInfo>>() {
            public void onFailure(Call<BaseCallResponse<ActivityInfo>> call, Throwable th) {
            }

            public void onResponse(Call<BaseCallResponse<ActivityInfo>> call, retrofit2.Response<BaseCallResponse<ActivityInfo>> response) {
                if (response.isSuccessful() && ((BaseCallResponse) response.body()).err == 0) {
                    SPUtil.saveCurrentActivityInfo((ActivityInfo) ((BaseCallResponse) response.body()).res);
                    EventBus.getDefault().post(new StatusEvent(7, ""));
                }
            }
        });
    }

    public static void testGetBindMeetingList() {
        Log.d("GIN", "testGetBindMeetingList");
        ((GphotoApi) getInstance().create(GphotoApi.class)).getBindMeetingList().enqueue(new Callback<BaseCallResponse<ListResponse<ActivityInfoWrapper>>>() {
            public void onFailure(Call<BaseCallResponse<ListResponse<ActivityInfoWrapper>>> call, Throwable th) {
            }

            public void onResponse(Call<BaseCallResponse<ListResponse<ActivityInfoWrapper>>> call, retrofit2.Response<BaseCallResponse<ListResponse<ActivityInfoWrapper>>> response) {
            }
        });
    }

    public static void testUpload(String str) {
        ActivityInfo currentActivityInfo = SPUtil.getCurrentActivityInfo();
        if (currentActivityInfo == null) {
            Log.d("GIN", "no current activity info");
        } else if (currentActivityInfo.activeAlbum == null) {
            Log.d("GIN", "no current album");
        } else {
            Log.d("GIN", "start to upload");
            ((GphotoApi) getInstance().create(GphotoApi.class)).uploadSimple(str2RequestBody(String.valueOf(currentActivityInfo.activeAlbum.id)), file2MultipartBody(new File(str))).enqueue(new Callback<BaseCallResponse<UploadResponse>>() {
                public void onFailure(Call<BaseCallResponse<UploadResponse>> call, Throwable th) {
                }

                public void onResponse(Call<BaseCallResponse<UploadResponse>> call, retrofit2.Response<BaseCallResponse<UploadResponse>> response) {
                }
            });
        }
    }

    public static <T> void enqueueWithRetry(Call<T> call, final Callback<T> callback) {
        call.enqueue(new RetryableCallback<T>(call) {
            public void onFinalResponse(Call<T> call, retrofit2.Response<T> response) {
                callback.onResponse(call, response);
            }

            public void onFinalFailure(Call<T> call, Throwable th) {
                callback.onFailure(call, th);
            }
        });
    }

    public static RequestBody str2RequestBody(String str) {
        return RequestBody.create(MediaType.parse("text/plain"), str);
    }

    public static Part file2MultipartBody(File file) {
        return Part.createFormData("photo", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
    }
}
