package io.gphotos.gin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import io.gphotos.gin.event.StatusEvent;
import java.text.DecimalFormat;
import org.greenrobot.eventbus.EventBus;

public class PhoneUtil {
    public static void checkConnection(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            EventBus.getDefault().post(new StatusEvent(6, "无网络"));
            return;
        }
        boolean isConnected = activeNetworkInfo.isConnected();
        String str = "%s : %s";
        Object[] objArr = new Object[2];
        objArr[0] = (activeNetworkInfo.getType() == 1 ? 1 : null) != null ? "WIFI" : "Mobile";
        objArr[1] = isConnected ? "已连接" : "未连接";
        String format = String.format(str, objArr);
        if (isConnected) {
            EventBus.getDefault().post(new StatusEvent(2, format));
        } else {
            EventBus.getDefault().post(new StatusEvent(6, format));
        }
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static long getAvailableInternalMemorySizeByMByte() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    }

    public static long getAvailableInternalMemorySizeByByte() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
    }

    public static long getTotalInternalMemorySizeByMByte() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    }

    public static long getTotalInternalMemorySizeByByte() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getBlockCountLong() * statFs.getBlockSizeLong();
    }

    public static String getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize(statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
    }

    public static String getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize(statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    public static boolean isExternalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getAvailableExternalMemorySize() {
        if (!isExternalMemoryAvailable()) {
            return "NONE";
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return formatSize(statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
    }

    public static String getTotalExternalMemorySize() {
        if (!isExternalMemoryAvailable()) {
            return "NONE";
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return formatSize(statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    public static String formatSize(long j) {
        String str;
        double d = (double) j;
        if (d >= 1024.0d) {
            str = "KB";
            d /= 1024.0d;
            if (d >= 1024.0d) {
                str = "MB";
                d /= 1024.0d;
            }
            if (d >= 1024.0d) {
                str = "GB";
                d /= 1024.0d;
            }
        } else {
            str = null;
        }
        StringBuilder stringBuilder = new StringBuilder(new DecimalFormat("#.#").format(d));
        stringBuilder.append(str);
        return stringBuilder.toString();
    }
}
