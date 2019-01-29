package io.gphotos.gin;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowConfig.Builder;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

public class GinApplication extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        FlowManager.init(mContext);
        MMKV.initialize( mContext);
        CrashReport.initCrashReport(getApplicationContext(), "99a1b55f17", false);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
