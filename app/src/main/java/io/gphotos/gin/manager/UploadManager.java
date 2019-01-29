package io.gphotos.gin.manager;

import android.content.Intent;
import android.util.Log;
import android.util.SparseIntArray;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import io.gphotos.gin.Database.ImageModel;
import io.gphotos.gin.GinApplication;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.event.UploadEvent;
import io.gphotos.gin.service.GinUpload2Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import org.greenrobot.eventbus.EventBus;

public class UploadManager {
    private static UploadManager instance = new UploadManager();
    private static final int mode = 2;
    private int cntLeft;
    private SparseIntArray dupArray = new SparseIntArray();
    private SparseIntArray dupHandle = new SparseIntArray();
    private List<ImageModel> imageModelList = new CopyOnWriteArrayList();
    private long mCurrentUploadId;
    public int mTaskReadyToUploaded;
    public int mTaskRetryCnt;
    public int mTaskTotal;
    public int mTaskUploaded;
    public int mTaskUploadedTotal;
    public final LinkedBlockingQueue<ImageModel> queue = new LinkedBlockingQueue();

    private UploadManager() {
    }

    public static UploadManager getInstance() {
        return instance;
    }

    public void initialize() {
        if (this.imageModelList == null) {
            this.imageModelList = new CopyOnWriteArrayList();
        }
        IProperty[] iProperties = new IProperty[0];
        Collection<ImageModel> queryList = null;
        Select select = SQLite.select(iProperties);
        From<ImageModel> from = select.from(ImageModel.class);
        queryList = from.queryList();

        for (ImageModel imageModel : queryList) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(imageModel.name);
            stringBuilder.append(imageModel.dateCreated);
            int hashCode = stringBuilder.toString().hashCode();
            if (this.dupArray.get(hashCode, -1) == -1) {
                this.dupArray.put(hashCode, 0);
            }
            int i = imageModel.objectHandle;
            if (this.dupHandle.get(i, Integer.MAX_VALUE) == Integer.MAX_VALUE) {
                this.dupHandle.put(i, 0);
            }
        }
        this.imageModelList.addAll(queryList);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("init upload ");
        stringBuilder2.append(this.imageModelList.size());
        d(stringBuilder2.toString());
    }

    public boolean addImageModelToList(ImageModel imageModel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(imageModel.name);
        stringBuilder.append(imageModel.dateCreated);
        int hashCode = stringBuilder.toString().hashCode();
        if (this.dupArray.get(hashCode, -1) != -1) {
            return false;
        }
        this.dupArray.put(hashCode, 0);
        imageModel.save();
        this.imageModelList.add(imageModel);
        addUploadTask(imageModel);
        return true;
    }

    public boolean isHandleDup(int i) {
        if (this.dupHandle.get(i, Integer.MAX_VALUE) != Integer.MAX_VALUE) {
            return true;
        }
        this.dupHandle.put(i, 0);
        return false;
    }

    public void addUploadTask(ImageModel imageModel) {
        this.mTaskTotal++;
        addUploadTask2CustomUploadService(imageModel);
        notifyUploadStatus();
    }

    public void addUploadTask2CustomUploadService(ImageModel imageModel) {
        this.queue.add(imageModel);
        GinApplication.getAppContext().startService(new Intent(GinApplication.getAppContext(), GinUpload2Service.class));
    }

    public void addUploadTask2CustomUploadService(List<ImageModel> list) {
        this.queue.addAll(list);
        GinApplication.getAppContext().startService(new Intent(GinApplication.getAppContext(), GinUpload2Service.class));
    }

    public void onUploadStart(long j) {
        this.mCurrentUploadId = j;
        notifyImageStatus("Start");
    }

    public void onUploadError(long j) {
        this.mCurrentUploadId = 0;
        notifyImageStatus("Error");
    }

    public void onUploadTaskOK(ImageModel imageModel) {
        this.mCurrentUploadId = 0;
        this.mTaskUploaded++;
        this.mTaskUploadedTotal++;
        imageModel.isUploaded = true;
        imageModel.uploadStatus = 1;
        imageModel.save();
        notifyUploadStatus();
        notifyImageStatus("Finish", imageModel.id);
    }

    public void onUploadTaskOK(long j) {
        this.mCurrentUploadId = 0;
        this.mTaskUploaded++;
        this.mTaskUploadedTotal++;
        for (ImageModel imageModel : this.imageModelList) {
            if (imageModel.id == j) {
                imageModel.isUploaded = true;
                imageModel.uploadStatus = 1;
                imageModel.save();
                break;
            }
        }
        notifyUploadStatus();
        notifyImageStatus("Finish", j);
    }

    public void onUploadRetry(long j) {
        this.mTaskRetryCnt++;
    }

    public long getmCurrentUploadId() {
        return this.mCurrentUploadId;
    }

    public void startUploadManual() {
        GinApplication.getAppContext().startService(new Intent(GinApplication.getAppContext(), GinUpload2Service.class));
    }

    public void startToUpload() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("total size ");
        stringBuilder.append(this.imageModelList.size());
        d(stringBuilder.toString());
        List arrayList = new ArrayList();
        for (ImageModel imageModel : this.imageModelList) {
            int i = imageModel.uploadStatus;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(imageModel.name);
            stringBuilder2.append("");
            stringBuilder2.append(imageModel.uploadStatus);
            d(stringBuilder2.toString());
            switch (i) {
                case 0:
                    arrayList.add(imageModel);
                    break;
                case 1:
                    this.mTaskUploadedTotal++;
                    break;
                default:
                    break;
            }
        }
        if (arrayList.size() > 0) {
            this.mTaskTotal = arrayList.size();
            this.mTaskUploaded = 0;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("upload task is ");
            stringBuilder3.append(this.mTaskTotal);
            d(stringBuilder3.toString());
            addUploadTask2CustomUploadService(arrayList);
        }
        notifyUploadStatus();
    }

    private void notifyImageStatus(String str, long j) {
        StatusEvent statusEvent = new StatusEvent(4, str);
        statusEvent.pId = j;
        EventBus.getDefault().post(statusEvent);
    }

    private void notifyImageStatus(String str) {
        notifyImageStatus(str, 0);
    }

    public int getCntUploaded() {
        return this.mTaskUploadedTotal;
    }

    public int getCntLeft() {
        return this.queue.size();
    }

    public void notifyUploadStatus() {
        UploadEvent uploadEvent = new UploadEvent(1, "");
        uploadEvent.cntTotal = this.imageModelList.size();
        uploadEvent.cntUploaded = this.mTaskUploadedTotal;
        uploadEvent.cntLeft = this.queue.size();
        uploadEvent.cntRetry = this.mTaskRetryCnt;
        EventBus.getDefault().post(uploadEvent);
    }

    public void clear() {
        this.imageModelList.clear();
        this.dupArray.clear();
        this.dupHandle.clear();
        this.mTaskTotal = 0;
        this.mTaskUploaded = 0;
        this.mTaskUploadedTotal = 0;
        this.mTaskRetryCnt = 0;
        this.mTaskReadyToUploaded = 0;
    }

    private void d(String str) {
        Log.d("GinManager", str);
    }
}
