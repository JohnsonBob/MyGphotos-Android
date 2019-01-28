package io.gphotos.gin.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import io.gphotos.gin.Database.ImageModel;
import io.gphotos.gin.api.GphotoApi;
import io.gphotos.gin.api.GphotoClient;
import io.gphotos.gin.api.RetryableCallback;
import io.gphotos.gin.manager.AccountManager;
import io.gphotos.gin.manager.UploadManager;
import io.gphotos.gin.model.BaseCallResponse;
import io.gphotos.gin.model.UploadResponse;
import io.gphotos.gin.util.FileUtil;
import io.gphotos.gin.util.TUtil;
import java.io.File;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Response;

public class GinUpload2Service extends Service {
    static final int CALLBACK_FAILURE = 2;
    static final int CALLBACK_OK = 0;
    static final int CALLBACK_RETRY = 4;
    static final int CALLBACK_SERVER_ERROR = 1;
    static final int CALLBACK_START = 3;
    static final int HIGH_HEIGHT = 2880;
    static final int HIGH_WIDTH = 2880;
    static final int JPG_QUALITY = 90;
    protected boolean mIsRunning;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        executeNext();
        return Integer.valueOf("1");
    }

    private void executeNext() {
        if (!this.mIsRunning) {
            if (UploadManager.getInstance().queue.isEmpty()) {
                stopSelf();
            } else {
                this.mIsRunning = true;
                final ImageModel imageModel = (ImageModel) UploadManager.getInstance().queue.peek();
                int activityAlbumId = AccountManager.getInstance().getActivityAlbumId();
                if (activityAlbumId == 0) {
                    d("no activity album");
                    TUtil.error(this, "no album ID");
                    this.mIsRunning = false;
                    return;
                }
                File saveBitmap2File;
                String str = imageModel.filePath;
                long j = imageModel.id;
                if (AccountManager.getInstance().getImageUploadSetting() == 1) {
                    Bitmap decodeFile = FileUtil.decodeFile(str, 2880, 2880);
                    if (decodeFile == null) {
                        d("resizeBitmap is null");
                        TUtil.error(this, "resizeBitmap is null");
                        uploadStatusCallback(imageModel, 2, "resizeBitmap is null");
                        this.mIsRunning = false;
                        return;
                    }
                    String substring = str.substring(str.lastIndexOf("."));
                    String substring2 = str.substring(0, str.lastIndexOf("."));
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(substring2);
                    stringBuilder.append("_high");
                    stringBuilder.append(substring);
                    saveBitmap2File = FileUtil.saveBitmap2File(decodeFile, stringBuilder.toString(), 90);
                    FileUtil.copyExifData(new File(str), saveBitmap2File, null);
                } else {
                    saveBitmap2File = new File(str);
                }
                if (saveBitmap2File == null || !saveBitmap2File.exists()) {
                    d("file is not exists");
                    TUtil.error(this, "file is not exists");
                    this.mIsRunning = false;
                    uploadStatusCallback(imageModel, 2, "file not exists");
                    return;
                }
                uploadStatusCallback(imageModel, 3, "start");
                String md5 = FileUtil.getMD5(saveBitmap2File);
                Call uploadWithMd5 = ((GphotoApi) GphotoClient.getInstance().create(GphotoApi.class)).uploadWithMd5(GphotoClient.str2RequestBody(String.valueOf(activityAlbumId)), GphotoClient.str2RequestBody(md5), GphotoClient.file2MultipartBody(saveBitmap2File));
                uploadWithMd5.enqueue(new RetryableCallback<BaseCallResponse<UploadResponse>>(uploadWithMd5) {
                    public void onFinalResponse(Call<BaseCallResponse<UploadResponse>> call, Response<BaseCallResponse<UploadResponse>> response) {
                        if (response.isSuccessful() && ((BaseCallResponse) response.body()).err == 0) {
                            GinUpload2Service.this.uploadStatusCallback(imageModel, 0, "ok");
                        } else {
                            GinUpload2Service.this.uploadStatusCallback(imageModel, 1, ((BaseCallResponse) response.body()).msg);
                        }
                    }

                    public void onFinalFailure(Call<BaseCallResponse<UploadResponse>> call, Throwable th) {
                        GinUpload2Service.this.uploadStatusCallback(imageModel, 2, "final failure");
                    }

                    public void onRetry() {
                        GinUpload2Service.this.uploadStatusCallback(imageModel, 4, "retry");
                    }
                });
            }
        }
    }

    private void uploadStatusCallback(ImageModel imageModel, int i, String str) {
        d(String.format(Locale.getDefault(), "upload complete %d, %d = %s", new Object[]{Long.valueOf(imageModel.id), Integer.valueOf(i), str}));
        switch (i) {
            case 0:
                this.mIsRunning = false;
                UploadManager.getInstance().queue.remove();
                UploadManager.getInstance().onUploadTaskOK(imageModel);
                executeNext();
                return;
            case 1:
            case 2:
                this.mIsRunning = false;
                TUtil.error(this, str);
                UploadManager.getInstance().queue.remove();
                UploadManager.getInstance().onUploadError(imageModel.id);
                executeNext();
                return;
            case 3:
                UploadManager.getInstance().onUploadStart(imageModel.id);
                return;
            case 4:
                UploadManager.getInstance().onUploadRetry(imageModel.id);
                return;
            default:
                return;
        }
    }

    private void d(String str) {
        Log.d("GINUploadService2", str);
    }
}
