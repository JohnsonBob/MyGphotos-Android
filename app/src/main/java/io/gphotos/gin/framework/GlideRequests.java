package io.gphotos.gin.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.net.URL;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

public class GlideRequests extends RequestManager {
    public GlideRequests(@NonNull Glide glide, @NonNull Lifecycle lifecycle, @NonNull RequestManagerTreeNode requestManagerTreeNode, @NonNull Context context) {
        super(glide, lifecycle, requestManagerTreeNode, context);
    }

    @CheckResult
    @NonNull
    public <ResourceType> GlideRequest<ResourceType> as(@NonNull Class<ResourceType> cls) {
        return new GlideRequest(this.glide, this, cls, this.context);
    }

    @NonNull
    public GlideRequests applyDefaultRequestOptions(@NonNull RequestOptions requestOptions) {
        return (GlideRequests) super.applyDefaultRequestOptions(requestOptions);
    }

    @NonNull
    public GlideRequests setDefaultRequestOptions(@NonNull RequestOptions requestOptions) {
        return (GlideRequests) super.setDefaultRequestOptions(requestOptions);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Bitmap> asBitmap() {
        return (GlideRequest) super.asBitmap();
    }

    @CheckResult
    @NonNull
    public GlideRequest<GifDrawable> asGif() {
        return (GlideRequest) super.asGif();
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> asDrawable() {
        return (GlideRequest) super.asDrawable();
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Bitmap bitmap) {
        return (GlideRequest) super.load(bitmap);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Drawable drawable) {
        return (GlideRequest) super.load(drawable);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable String str) {
        return (GlideRequest) super.load(str);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Uri uri) {
        return (GlideRequest) super.load(uri);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable File file) {
        return (GlideRequest) super.load(file);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable @RawRes @DrawableRes Integer num) {
        return (GlideRequest) super.load(num);
    }

    @Deprecated
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable URL url) {
        return (GlideRequest) super.load(url);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable byte[] bArr) {
        return (GlideRequest) super.load(bArr);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Object obj) {
        return (GlideRequest) super.load(obj);
    }

    @CheckResult
    @NonNull
    public GlideRequest<File> downloadOnly() {
        return (GlideRequest) super.downloadOnly();
    }

    @CheckResult
    @NonNull
    public GlideRequest<File> download(@Nullable Object obj) {
        return (GlideRequest) super.download(obj);
    }

    @CheckResult
    @NonNull
    public GlideRequest<File> asFile() {
        return (GlideRequest) super.asFile();
    }

    protected void setRequestOptions(@NonNull RequestOptions requestOptions) {
        if (requestOptions instanceof GlideOptions) {
            super.setRequestOptions(requestOptions);
        } else {
            super.setRequestOptions(new GlideOptions().apply(requestOptions));
        }
    }
}
