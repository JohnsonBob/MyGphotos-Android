package io.gphotos.gin.ui;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import io.gphotos.gin.Database.ImageModel;
import io.gphotos.gin.Database.ImageModel_Table;
import io.gphotos.gins.R;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.framework.BaseFragment;
import io.gphotos.gin.manager.UploadManager;
import io.gphotos.gin.service.GinCameraService;
import io.gphotos.gin.ui.adapter.GalleryAdapter;
import io.gphotos.gin.ui.adapter.GridSpacesItemDecoration;
import io.gphotos.gin.util.DateTimeUtil;
import io.gphotos.gin.util.FileUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GalleryFragment extends BaseFragment implements Consumer<List> {
    private static final int PICK_IMAGE_MULTIPLE = 123;
    private final String TAG = GalleryFragment.class.getSimpleName();
    GalleryAdapter adapter;
    @BindView(R.id.gallery_btn)
    Button btnGallery;
    @BindView(R.id.import_btn)
    Button btnImport;
    long mId;
    @BindView(R.id.gallery_rv)
    RecyclerView mRv;
    int mType;
    @BindView(R.id.gallery_tv_status)
    TextView tvStatus;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        bundle = getArguments();
        if (bundle != null) {
            this.mType = bundle.getInt("type");
            this.mId = bundle.getLong("id");
        }
        getData();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_gallery, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        int dimension = (int) getResources().getDimension(R.dimen.grid_space);
        this.adapter = new GalleryAdapter(getActivity(), this.mRv, 3);
        this.mRv.setAdapter(this.adapter);
        this.mRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        this.mRv.addItemDecoration(new GridSpacesItemDecoration(dimension, 3));
        return inflate;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionEvent(StatusEvent statusEvent) {
        if (isAdded() && statusEvent.action == 4) {
            Log.d(this.TAG, "onActionEvent updated db");
            Log.d(this.TAG, statusEvent.description);
            if (statusEvent.description.equals("OVER")) {
                this.btnGallery.setText("扫卡开始同步");
            } else if (statusEvent.description.equals("ERROR")) {
                this.btnGallery.setText("扫卡开始同步");
            } else if (statusEvent.description.equals("Start")) {
                this.adapter.notifyDataSetChanged();
                return;
            } else if (statusEvent.description.equals("Error")) {
                this.adapter.notifyDataSetChanged();
                return;
            } else if (statusEvent.description.equals("Finish")) {
                this.adapter.changeDataLocalOK(statusEvent.pId);
                return;
            }
            getData();
        }
    }

    private void getData() {
        Flowable.just(Long.valueOf(this.mId)).map(new Function<Long, List>() {
            public List apply(Long l) throws Exception {
                GalleryFragment.this.logThreadName("RX map apply");
                return SQLite.select(new IProperty[0]).from(ImageModel.class).orderBy(ImageModel_Table.id, false).queryList();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) this);
    }

    public void accept(List list) throws Exception {
        logThreadName("in accept");
        if (this.adapter != null) {
            this.adapter.setData(list);
            this.adapter.notifyDataSetChanged();
            this.tvStatus.setText(String.format(Locale.getDefault(), "本地照片： %d 张", new Object[]{Integer.valueOf(this.adapter.getItemCount())}));
        }
    }

    private void logThreadName(String str) {
        Log.d(this.TAG, str);
        Log.d(this.TAG, Thread.currentThread().getName());
    }

    private void d(String str) {
        Log.d(this.TAG, str);
    }

    @OnClick(R.id.gallery_btn)
    public void clickScan() {
        this.btnGallery.setText("同步中...");
        GinCameraService.startScanCamera(getActivity());
    }

    @OnClick(R.id.import_btn)
    public void selectImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "选择照片"), PICK_IMAGE_MULTIPLE);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == PICK_IMAGE_MULTIPLE && intent != null) {
            String[] strArr = new String[]{"_data"};
            final List arrayList = new ArrayList();
            if (intent.getData() != null) {
                Cursor query = getActivity().getContentResolver().query(intent.getData(), strArr, null, null, null);
                query.moveToFirst();
                arrayList.add(query.getString(query.getColumnIndex(strArr[0])));
                query.close();
            } else if (intent.getClipData() != null) {
                ClipData clipData = intent.getClipData();
                ArrayList arrayList2 = new ArrayList();
                for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                    Uri uri = clipData.getItemAt(i3).getUri();
                    arrayList2.add(uri);
                    Cursor query2 = getActivity().getContentResolver().query(uri, strArr, null, null, null);
                    query2.moveToFirst();
                    arrayList.add(query2.getString(query2.getColumnIndex(strArr[0])));
                    query2.close();
                }
            }
            if (!arrayList.isEmpty()) {
                showLoading();
                Flowable.just(Long.valueOf(1)).map(new Function<Long, Long>() {
                    public Long apply(Long l) throws Exception {
                        GalleryFragment.this.logThreadName("RX map apply");
                        GalleryFragment.this.processImagesFromLocal(arrayList);
                        return Long.valueOf(1);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    public void accept(Long l) throws Exception {
                        GalleryFragment.this.hideLoading();
                    }
                });
            }
        }
    }

    private void showLoading() {
        this.btnImport.setText("导入中...");
    }

    private void hideLoading() {
        this.btnImport.setText("从相册");
    }

    private void processImagesFromLocal(List<String> list) {
        for (String str : list) {
            if (str != null) {
                File file = new File(str);
                if (file.exists()) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.name = file.getName();
                    imageModel.dateCreated = file.lastModified();
                    imageModel.uploadStatus = 0;
                    imageModel.isUploaded = false;
                    String createFileNameForCameraImage = FileUtil.createFileNameForCameraImage("", file.getName(), DateTimeUtil.getTimeOfDateString(file.lastModified()));
                    String createFileNameForCameraImageOriginal = FileUtil.createFileNameForCameraImageOriginal("", file.getName(), DateTimeUtil.getTimeOfDateString(file.lastModified()));
                    Bitmap decodeFile = FileUtil.decodeFile(file.getAbsolutePath(), 128, 128);
                    Object obj = 1;
                    if (decodeFile != null) {
                        FileUtil.saveBitmap2File(decodeFile, createFileNameForCameraImage);
                    } else {
                        obj = null;
                    }
                    try {
                        FileUtil.copyFile(file, new File(createFileNameForCameraImageOriginal));
                    } catch (Exception e) {
                        e.printStackTrace();
                        obj = null;
                    }
                    if (obj != null) {
                        d("is ok");
                        imageModel.thumbnailPath = createFileNameForCameraImage;
                        imageModel.filePath = createFileNameForCameraImageOriginal;
                        imageModel.uploadStatus = 0;
                        imageModel.isUploaded = false;
                        if (UploadManager.getInstance().addImageModelToList(imageModel)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("add it ");
                            stringBuilder.append(imageModel.name);
                            d(stringBuilder.toString());
                        }
                    } else {
                        d("not ok");
                    }
                }
            }
        }
        EventBus.getDefault().post(new StatusEvent(4, ""));
    }
}
