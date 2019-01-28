package io.gphotos.gin.framework;

import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class GlideOptions extends RequestOptions implements Cloneable {
    private static GlideOptions centerCropTransform2;
    private static GlideOptions centerInsideTransform1;
    private static GlideOptions circleCropTransform3;
    private static GlideOptions fitCenterTransform0;
    private static GlideOptions noAnimation5;
    private static GlideOptions noTransformation4;

    @CheckResult
    @NonNull
    public static GlideOptions sizeMultiplierOf(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        return new GlideOptions().sizeMultiplier(f);
    }

    @CheckResult
    @NonNull
    public static GlideOptions diskCacheStrategyOf(@NonNull DiskCacheStrategy diskCacheStrategy) {
        return new GlideOptions().diskCacheStrategy(diskCacheStrategy);
    }

    @CheckResult
    @NonNull
    public static GlideOptions priorityOf(@NonNull Priority priority) {
        return new GlideOptions().priority(priority);
    }

    @CheckResult
    @NonNull
    public static GlideOptions placeholderOf(@Nullable Drawable drawable) {
        return new GlideOptions().placeholder(drawable);
    }

    @CheckResult
    @NonNull
    public static GlideOptions placeholderOf(@DrawableRes int i) {
        return new GlideOptions().placeholder(i);
    }

    @CheckResult
    @NonNull
    public static GlideOptions errorOf(@Nullable Drawable drawable) {
        return new GlideOptions().error(drawable);
    }

    @CheckResult
    @NonNull
    public static GlideOptions errorOf(@DrawableRes int i) {
        return new GlideOptions().error(i);
    }

    @CheckResult
    @NonNull
    public static GlideOptions skipMemoryCacheOf(boolean z) {
        return new GlideOptions().skipMemoryCache(z);
    }

    @CheckResult
    @NonNull
    public static GlideOptions overrideOf(@IntRange(from = 0) int i, @IntRange(from = 0) int i2) {
        return new GlideOptions().override(i, i2);
    }

    @CheckResult
    @NonNull
    public static GlideOptions overrideOf(@IntRange(from = 0) int i) {
        return new GlideOptions().override(i);
    }

    @CheckResult
    @NonNull
    public static GlideOptions signatureOf(@NonNull Key key) {
        return new GlideOptions().signature(key);
    }

    @CheckResult
    @NonNull
    public static GlideOptions fitCenterTransform() {
        if (fitCenterTransform0 == null) {
            fitCenterTransform0 = new GlideOptions().fitCenter().autoClone();
        }
        return fitCenterTransform0;
    }

    @CheckResult
    @NonNull
    public static GlideOptions centerInsideTransform() {
        if (centerInsideTransform1 == null) {
            centerInsideTransform1 = new GlideOptions().centerInside().autoClone();
        }
        return centerInsideTransform1;
    }

    @CheckResult
    @NonNull
    public static GlideOptions centerCropTransform() {
        if (centerCropTransform2 == null) {
            centerCropTransform2 = new GlideOptions().centerCrop().autoClone();
        }
        return centerCropTransform2;
    }

    @CheckResult
    @NonNull
    public static GlideOptions circleCropTransform() {
        if (circleCropTransform3 == null) {
            circleCropTransform3 = new GlideOptions().circleCrop().autoClone();
        }
        return circleCropTransform3;
    }

    @CheckResult
    @NonNull
    public static GlideOptions bitmapTransform(@NonNull Transformation<Bitmap> transformation) {
        return new GlideOptions().transform((Transformation) transformation);
    }

    @CheckResult
    @NonNull
    public static GlideOptions noTransformation() {
        if (noTransformation4 == null) {
            noTransformation4 = new GlideOptions().dontTransform().autoClone();
        }
        return noTransformation4;
    }

    @CheckResult
    @NonNull
    public static <T> GlideOptions option(@NonNull Option<T> option, @NonNull T t) {
        return new GlideOptions().set((Option) option, (Object) t);
    }

    @CheckResult
    @NonNull
    public static GlideOptions decodeTypeOf(@NonNull Class<?> cls) {
        return new GlideOptions().decode((Class) cls);
    }

    @CheckResult
    @NonNull
    public static GlideOptions formatOf(@NonNull DecodeFormat decodeFormat) {
        return new GlideOptions().format(decodeFormat);
    }

    @CheckResult
    @NonNull
    public static GlideOptions frameOf(@IntRange(from = 0) long j) {
        return new GlideOptions().frame(j);
    }

    @CheckResult
    @NonNull
    public static GlideOptions downsampleOf(@NonNull DownsampleStrategy downsampleStrategy) {
        return new GlideOptions().downsample(downsampleStrategy);
    }

    @CheckResult
    @NonNull
    public static GlideOptions timeoutOf(@IntRange(from = 0) int i) {
        return new GlideOptions().timeout(i);
    }

    @CheckResult
    @NonNull
    public static GlideOptions encodeQualityOf(@IntRange(from = 0, to = 100) int i) {
        return new GlideOptions().encodeQuality(i);
    }

    @CheckResult
    @NonNull
    public static GlideOptions encodeFormatOf(@NonNull CompressFormat compressFormat) {
        return new GlideOptions().encodeFormat(compressFormat);
    }

    @CheckResult
    @NonNull
    public static GlideOptions noAnimation() {
        if (noAnimation5 == null) {
            noAnimation5 = new GlideOptions().dontAnimate().autoClone();
        }
        return noAnimation5;
    }

    @CheckResult
    @NonNull
    public final GlideOptions sizeMultiplier(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        return (GlideOptions) super.sizeMultiplier(f);
    }

    @CheckResult
    @NonNull
    public final GlideOptions useUnlimitedSourceGeneratorsPool(boolean z) {
        return (GlideOptions) super.useUnlimitedSourceGeneratorsPool(z);
    }

    @CheckResult
    @NonNull
    public final GlideOptions useAnimationPool(boolean z) {
        return (GlideOptions) super.useAnimationPool(z);
    }

    @CheckResult
    @NonNull
    public final GlideOptions onlyRetrieveFromCache(boolean z) {
        return (GlideOptions) super.onlyRetrieveFromCache(z);
    }

    @CheckResult
    @NonNull
    public final GlideOptions diskCacheStrategy(@NonNull DiskCacheStrategy diskCacheStrategy) {
        return (GlideOptions) super.diskCacheStrategy(diskCacheStrategy);
    }

    @CheckResult
    @NonNull
    public final GlideOptions priority(@NonNull Priority priority) {
        return (GlideOptions) super.priority(priority);
    }

    @CheckResult
    @NonNull
    public final GlideOptions placeholder(@Nullable Drawable drawable) {
        return (GlideOptions) super.placeholder(drawable);
    }

    @CheckResult
    @NonNull
    public final GlideOptions placeholder(@DrawableRes int i) {
        return (GlideOptions) super.placeholder(i);
    }

    @CheckResult
    @NonNull
    public final GlideOptions fallback(@Nullable Drawable drawable) {
        return (GlideOptions) super.fallback(drawable);
    }

    @CheckResult
    @NonNull
    public final GlideOptions fallback(@DrawableRes int i) {
        return (GlideOptions) super.fallback(i);
    }

    @CheckResult
    @NonNull
    public final GlideOptions error(@Nullable Drawable drawable) {
        return (GlideOptions) super.error(drawable);
    }

    @CheckResult
    @NonNull
    public final GlideOptions error(@DrawableRes int i) {
        return (GlideOptions) super.error(i);
    }

    @CheckResult
    @NonNull
    public final GlideOptions theme(@Nullable Theme theme) {
        return (GlideOptions) super.theme(theme);
    }

    @CheckResult
    @NonNull
    public final GlideOptions skipMemoryCache(boolean z) {
        return (GlideOptions) super.skipMemoryCache(z);
    }

    @CheckResult
    @NonNull
    public final GlideOptions override(int i, int i2) {
        return (GlideOptions) super.override(i, i2);
    }

    @CheckResult
    @NonNull
    public final GlideOptions override(int i) {
        return (GlideOptions) super.override(i);
    }

    @CheckResult
    @NonNull
    public final GlideOptions signature(@NonNull Key key) {
        return (GlideOptions) super.signature(key);
    }

    @CheckResult
    public final GlideOptions clone() {
        return (GlideOptions) super.clone();
    }

    @CheckResult
    @NonNull
    public final <T> GlideOptions set(@NonNull Option<T> option, @NonNull T t) {
        return (GlideOptions) super.set(option, t);
    }

    @CheckResult
    @NonNull
    public final GlideOptions decode(@NonNull Class<?> cls) {
        return (GlideOptions) super.decode(cls);
    }

    @CheckResult
    @NonNull
    public final GlideOptions encodeFormat(@NonNull CompressFormat compressFormat) {
        return (GlideOptions) super.encodeFormat(compressFormat);
    }

    @CheckResult
    @NonNull
    public final GlideOptions encodeQuality(@IntRange(from = 0, to = 100) int i) {
        return (GlideOptions) super.encodeQuality(i);
    }

    @CheckResult
    @NonNull
    public final GlideOptions frame(@IntRange(from = 0) long j) {
        return (GlideOptions) super.frame(j);
    }

    @CheckResult
    @NonNull
    public final GlideOptions format(@NonNull DecodeFormat decodeFormat) {
        return (GlideOptions) super.format(decodeFormat);
    }

    @CheckResult
    @NonNull
    public final GlideOptions disallowHardwareConfig() {
        return (GlideOptions) super.disallowHardwareConfig();
    }

    @CheckResult
    @NonNull
    public final GlideOptions downsample(@NonNull DownsampleStrategy downsampleStrategy) {
        return (GlideOptions) super.downsample(downsampleStrategy);
    }

    @CheckResult
    @NonNull
    public final GlideOptions timeout(@IntRange(from = 0) int i) {
        return (GlideOptions) super.timeout(i);
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalCenterCrop() {
        return (GlideOptions) super.optionalCenterCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions centerCrop() {
        return (GlideOptions) super.centerCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalFitCenter() {
        return (GlideOptions) super.optionalFitCenter();
    }

    @CheckResult
    @NonNull
    public final GlideOptions fitCenter() {
        return (GlideOptions) super.fitCenter();
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalCenterInside() {
        return (GlideOptions) super.optionalCenterInside();
    }

    @CheckResult
    @NonNull
    public final GlideOptions centerInside() {
        return (GlideOptions) super.centerInside();
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalCircleCrop() {
        return (GlideOptions) super.optionalCircleCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions circleCrop() {
        return (GlideOptions) super.circleCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions transform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideOptions) super.transform(transformation);
    }

    @SafeVarargs
    @CheckResult
    @NonNull
    public final GlideOptions transforms(@NonNull Transformation<Bitmap>... transformationArr) {
        return (GlideOptions) super.transforms(transformationArr);
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalTransform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideOptions) super.optionalTransform(transformation);
    }

    @CheckResult
    @NonNull
    public final <T> GlideOptions optionalTransform(@NonNull Class<T> cls, @NonNull Transformation<T> transformation) {
        return (GlideOptions) super.optionalTransform((Class) cls, (Transformation) transformation);
    }

    @CheckResult
    @NonNull
    public final <T> GlideOptions transform(@NonNull Class<T> cls, @NonNull Transformation<T> transformation) {
        return (GlideOptions) super.transform((Class) cls, (Transformation) transformation);
    }

    @CheckResult
    @NonNull
    public final GlideOptions dontTransform() {
        return (GlideOptions) super.dontTransform();
    }

    @CheckResult
    @NonNull
    public final GlideOptions dontAnimate() {
        return (GlideOptions) super.dontAnimate();
    }

    @CheckResult
    @NonNull
    public final GlideOptions apply(@NonNull RequestOptions requestOptions) {
        return (GlideOptions) super.apply(requestOptions);
    }

    @NonNull
    public final GlideOptions lock() {
        return (GlideOptions) super.lock();
    }

    @NonNull
    public final GlideOptions autoClone() {
        return (GlideOptions) super.autoClone();
    }
}
