package io.gphotos.gin.framework;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.net.URL;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType> implements Cloneable {
    GlideRequest(@NonNull Class<TranscodeType> cls, @NonNull RequestBuilder<?> requestBuilder) {
        super(cls, requestBuilder);
    }

    GlideRequest(@NonNull Glide glide, @NonNull RequestManager requestManager, @NonNull Class<TranscodeType> cls, @NonNull Context context) {
        super(glide, requestManager, cls, context);
    }

    @CheckResult
    @NonNull
    protected GlideRequest<File> getDownloadOnlyRequest() {
        return new GlideRequest(File.class, this).apply(DOWNLOAD_ONLY_OPTIONS);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> sizeMultiplier(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).sizeMultiplier(f);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).sizeMultiplier(f);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> useUnlimitedSourceGeneratorsPool(boolean z) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).useUnlimitedSourceGeneratorsPool(z);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).useUnlimitedSourceGeneratorsPool(z);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> useAnimationPool(boolean z) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).useAnimationPool(z);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).useAnimationPool(z);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> onlyRetrieveFromCache(boolean z) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).onlyRetrieveFromCache(z);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).onlyRetrieveFromCache(z);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> diskCacheStrategy(@NonNull DiskCacheStrategy diskCacheStrategy) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).diskCacheStrategy(diskCacheStrategy);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).diskCacheStrategy(diskCacheStrategy);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> priority(@NonNull Priority priority) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).priority(priority);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).priority(priority);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> placeholder(@Nullable Drawable drawable) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).placeholder(drawable);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).placeholder(drawable);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> placeholder(@DrawableRes int i) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).placeholder(i);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).placeholder(i);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> fallback(@Nullable Drawable drawable) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).fallback(drawable);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fallback(drawable);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> fallback(@DrawableRes int i) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).fallback(i);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fallback(i);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> error(@Nullable Drawable drawable) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).error(drawable);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).error(drawable);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> error(@DrawableRes int i) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).error(i);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).error(i);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> theme(@Nullable Theme theme) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).theme(theme);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).theme(theme);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> skipMemoryCache(boolean z) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).skipMemoryCache(z);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).skipMemoryCache(z);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> override(int i, int i2) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).override(i, i2);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).override(i, i2);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> override(int i) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).override(i);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).override(i);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> signature(@NonNull Key key) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).signature(key);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).signature(key);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public <T> GlideRequest<TranscodeType> set(@NonNull Option<T> option, @NonNull T t) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).set((Option) option, (Object) t);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).set((Option) option, (Object) t);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> decode(@NonNull Class<?> cls) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).decode((Class) cls);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).decode((Class) cls);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> encodeFormat(@NonNull CompressFormat compressFormat) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).encodeFormat(compressFormat);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).encodeFormat(compressFormat);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> encodeQuality(@IntRange(from = 0, to = 100) int i) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).encodeQuality(i);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).encodeQuality(i);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> frame(@IntRange(from = 0) long j) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).frame(j);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).frame(j);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> format(@NonNull DecodeFormat decodeFormat) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).format(decodeFormat);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).format(decodeFormat);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> disallowHardwareConfig() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).disallowHardwareConfig();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).disallowHardwareConfig();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> downsample(@NonNull DownsampleStrategy downsampleStrategy) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).downsample(downsampleStrategy);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).downsample(downsampleStrategy);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> timeout(@IntRange(from = 0) int i) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).timeout(i);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).timeout(i);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalCenterCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalCenterCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalCenterCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> centerCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).centerCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).centerCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalFitCenter() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalFitCenter();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalFitCenter();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> fitCenter() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).fitCenter();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fitCenter();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalCenterInside() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalCenterInside();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalCenterInside();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> centerInside() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).centerInside();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).centerInside();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalCircleCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalCircleCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalCircleCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> circleCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).circleCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).circleCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> transform(@NonNull Transformation<Bitmap> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).transform((Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).transform((Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> transforms(@NonNull Transformation<Bitmap>... transformationArr) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).transforms((Transformation[]) transformationArr);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).transforms((Transformation[]) transformationArr);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalTransform(@NonNull Transformation<Bitmap> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalTransform((Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalTransform((Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public <T> GlideRequest<TranscodeType> optionalTransform(@NonNull Class<T> cls, @NonNull Transformation<T> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalTransform((Class) cls, (Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalTransform((Class) cls, (Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public <T> GlideRequest<TranscodeType> transform(@NonNull Class<T> cls, @NonNull Transformation<T> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).transform((Class) cls, (Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).transform((Class) cls, (Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> dontTransform() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).dontTransform();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).dontTransform();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> dontAnimate() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).dontAnimate();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).dontAnimate();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> apply(@NonNull RequestOptions requestOptions) {
        return (GlideRequest) super.apply(requestOptions);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> transition(@NonNull TransitionOptions<?, ? super TranscodeType> transitionOptions) {
        return (GlideRequest) super.transition(transitionOptions);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> listener(@Nullable RequestListener<TranscodeType> requestListener) {
        return (GlideRequest) super.listener(requestListener);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> addListener(@Nullable RequestListener<TranscodeType> requestListener) {
        return (GlideRequest) super.addListener(requestListener);
    }

    @NonNull
    public GlideRequest<TranscodeType> error(@Nullable RequestBuilder<TranscodeType> requestBuilder) {
        return (GlideRequest) super.error(requestBuilder);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType> requestBuilder) {
        return (GlideRequest) super.thumbnail((RequestBuilder) requestBuilder);
    }

    @SafeVarargs
    @CheckResult
    @NonNull
    public final GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType>... requestBuilderArr) {
        return (GlideRequest) super.thumbnail((RequestBuilder[]) requestBuilderArr);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> thumbnail(float f) {
        return (GlideRequest) super.thumbnail(f);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Object obj) {
        return (GlideRequest) super.load(obj);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Bitmap bitmap) {
        return (GlideRequest) super.load(bitmap);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Drawable drawable) {
        return (GlideRequest) super.load(drawable);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable String str) {
        return (GlideRequest) super.load(str);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Uri uri) {
        return (GlideRequest) super.load(uri);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable File file) {
        return (GlideRequest) super.load(file);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable @RawRes @DrawableRes Integer num) {
        return (GlideRequest) super.load(num);
    }

    @Deprecated
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable URL url) {
        return (GlideRequest) super.load(url);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable byte[] bArr) {
        return (GlideRequest) super.load(bArr);
    }

    @CheckResult
    public GlideRequest<TranscodeType> clone() {
        return (GlideRequest) super.clone();
    }
}
