package io.gphotos.gin.util;

import android.content.Context;
import com.shashank.sony.fancytoastlib.FancyToast;

public class TUtil {
    public static void error(Context context, String str) {
        FancyToast.makeText(context, str, 0, FancyToast.ERROR, false).show();
    }

    public static void ok(Context context, String str) {
        FancyToast.makeText(context, str, 0, FancyToast.SUCCESS, false).show();
    }

    public static void info(Context context, String str) {
        FancyToast.makeText(context, str, 0, FancyToast.INFO, false).show();
    }
}
