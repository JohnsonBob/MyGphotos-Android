package io.gphotos.gin.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import android.util.Log;
import io.gphotos.gin.Database.ImageModel;
import io.gphotos.gin.api.GphotoApi;
import io.gphotos.gin.api.GphotoClient;
import io.gphotos.gin.manager.AccountManager;
import io.gphotos.gin.manager.UploadManager;
import io.gphotos.gin.model.BaseCallResponse;
import io.gphotos.gin.util.FileUtil;
import java.io.File;
import retrofit2.Response;

public class GinUploadService extends JobIntentService {
    private static final String ACTION_UPLOAD_IMAGE = "io.gphotos.gin.upload_image";
    public static final int JOB_ID = 1;
    private static final String PARAMETER_FILE_NAME = "fileName";
    private static final String PARAMETER_FILE_PATH = "filePath";
    private static final String PARAMETER_ID = "modelId";

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(@Nullable Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    protected void onHandleWork(@Nullable Intent intent) {
        uploadImage(intent);
    }

    public static void addImageTask(Context context, ImageModel imageModel) {
        Intent intent = new Intent(context, GinUploadService.class);
        intent.setAction(ACTION_UPLOAD_IMAGE);
        intent.putExtra(PARAMETER_FILE_PATH, imageModel.filePath);
        intent.putExtra(PARAMETER_FILE_NAME, imageModel.name);
        intent.putExtra(PARAMETER_ID, imageModel.id);
        JobIntentService.enqueueWork(context, GinUploadService.class, 1, intent);
    }

    private void uploadImage(Intent intent) {
        d("process image, ready to upload");
        if (intent == null) {
            d("intent is null");
            return;
        }
        if (ACTION_UPLOAD_IMAGE.equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra(PARAMETER_FILE_PATH);
            intent.getStringExtra(PARAMETER_FILE_NAME);
            long longExtra = intent.getLongExtra(PARAMETER_ID, 0);
            int activityAlbumId = AccountManager.getInstance().getActivityAlbumId();
            if (activityAlbumId == 0) {
                d("no activity album");
                return;
            }
            File saveBitmap2File;
            if (AccountManager.getInstance().getImageUploadSetting() == 1) {
                Bitmap decodeFile = FileUtil.decodeFile(stringExtra, 2880, 2880);
                if (decodeFile == null) {
                    d("resizeBitmap is null");
                    return;
                }
                String substring = stringExtra.substring(stringExtra.lastIndexOf("."));
                String substring2 = stringExtra.substring(0, stringExtra.lastIndexOf("."));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(substring2);
                stringBuilder.append("_high");
                stringBuilder.append(substring);
                saveBitmap2File = FileUtil.saveBitmap2File(decodeFile, stringBuilder.toString(), 90);
                FileUtil.copyExifData(new File(stringExtra), saveBitmap2File, null);
            } else {
                saveBitmap2File = new File(stringExtra);
            }
            if (saveBitmap2File == null || !saveBitmap2File.exists()) {
                d("file is not exists");
                return;
            }
            stringExtra = FileUtil.getMD5(saveBitmap2File);
            notifyStartUpload(longExtra);
            try {
                Response execute = ((GphotoApi) GphotoClient.getInstance().create(GphotoApi.class)).uploadWithMd5(GphotoClient.str2RequestBody(String.valueOf(activityAlbumId)), GphotoClient.str2RequestBody(stringExtra), GphotoClient.file2MultipartBody(saveBitmap2File)).execute();
                if (execute.isSuccessful() && ((BaseCallResponse) execute.body()).err == 0) {
                    d("upload ok one");
                    notifyFinishUpload(longExtra);
                } else {
                    notifyErrorUpload(longExtra);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("upload err : ");
                    stringBuilder2.append(((BaseCallResponse) execute.body()).msg);
                    d(stringBuilder2.toString());
                }
            } catch (Exception e) {
                d("upload err e");
                e.printStackTrace();
                notifyErrorUpload(longExtra);
                d("upload err end");
            }
        }
    }

    private void notifyStartUpload(long j) {
        UploadManager.getInstance().onUploadStart(j);
    }

    private void notifyFinishUpload(long j) {
        UploadManager.getInstance().onUploadTaskOK(j);
    }

    private void notifyErrorUpload(long j) {
        UploadManager.getInstance().onUploadError(j);
    }

    private void d(String str) {
        Log.d("GINUploadService", str);
    }
}
