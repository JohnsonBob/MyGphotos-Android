package io.gphotos.gin.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import io.gphotos.gin.Database.ImageModel;
import io.gphotos.gin.R;
import io.gphotos.gin.framework.GlideApp;
import io.gphotos.gin.manager.UploadManager;
import java.io.File;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private int mColumns = 3;
    private Context mContext;
    private List<ImageModel> mList;
    private RecyclerView mRecyclerView;
    RequestOptions options = new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE);

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cell_gallery_iv)
        ImageView ivCover;
        View rootView;
        @BindView(R.id.cell_gallery_tv)
        TextView tvStatus;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.rootView = view;
            ButterKnife.bind((Object) this, view);
            int dimension = (int) GalleryAdapter.this.mContext.getResources().getDimension(R.dimen.grid_space);
            LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.height = (int) ((((double) (GalleryAdapter.this.mRecyclerView.getMeasuredWidth() - (dimension * 2))) * 0.75d) / ((double) GalleryAdapter.this.mColumns));
            this.ivCover.setLayoutParams(layoutParams);
        }
    }

    public class ViewHolderViewBinding<T extends ViewHolder> implements Unbinder {
        protected T target;

        @UiThread
        public ViewHolderViewBinding(T t, View view) {
            this.target = t;
            t.ivCover = (ImageView) Utils.findRequiredViewAsType(view, R.id.cell_gallery_iv, "field 'ivCover'", ImageView.class);
            t.tvStatus = (TextView) Utils.findRequiredViewAsType(view, R.id.cell_gallery_tv, "field 'tvStatus'", TextView.class);
        }

        @CallSuper
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            viewHolder.ivCover = null;
            viewHolder.tvStatus = null;
            this.target = null;
        }
    }

    public GalleryAdapter(Context context, RecyclerView recyclerView, int i) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mColumns = i;
    }

    public void changeDataLocalOK(long j) {
        for (ImageModel imageModel : this.mList) {
            if (imageModel.id == j) {
                imageModel.uploadStatus = 1;
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setData(List<ImageModel> list) {
        this.mList = list;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_gallery, viewGroup, false));
    }

    public int getItemCount() {
        return this.mList == null ? 20 : this.mList.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (this.mList != null && this.mList.size() != 0) {
            ImageModel imageModel = (ImageModel) this.mList.get(i);
            String str = imageModel.thumbnailPath;
            int i2 = imageModel.uploadStatus;
            GlideApp.with(this.mContext).load(new File(str)).fitCenter().into(viewHolder.ivCover);
            viewHolder.tvStatus.setTextColor(this.mContext.getResources().getColor(R.color.darkFontLight));
            if (imageModel.id == UploadManager.getInstance().getmCurrentUploadId()) {
                viewHolder.tvStatus.setTextColor(this.mContext.getResources().getColor(R.color.colorBlueLight));
                viewHolder.tvStatus.setText("正在上传");
            } else if (i2 == 0) {
                viewHolder.tvStatus.setText("待上传");
            } else if (i2 == 1) {
                viewHolder.tvStatus.setText("已上传");
            }
        }
    }
}
