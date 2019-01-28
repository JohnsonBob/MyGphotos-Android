package io.gphotos.gin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.gphotos.gin.event.ReqEvent;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.framework.BaseCompatActivity;
import io.gphotos.gin.receiver.SystemWrapperReceiver;
import io.gphotos.gin.service.GinCameraService;
import io.gphotos.gin.ui.SettingFragment;
import io.gphotos.gin.ui.SplashActivity;
import io.gphotos.gin.ui.adapter.MainViewPagerAdapter;
import io.gphotos.gin.util.SPUtil;
import io.gphotos.gin.util.TUtil;

import org.apache.sanselan.formats.jpeg.iptc.IPTCConstants;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import android.app.AlertDialog.Builder;


public class MainActivity extends BaseCompatActivity {
    @BindView(R.id.btnConnect)
    Button btnConnect;
    @BindView(R.id.btnSetting)
    Button btnSetting;
    private FragmentManager mFragmentManager;
    MainViewPagerAdapter pagerAdapter;
    SystemWrapperReceiver receiver = new SystemWrapperReceiver();
    @BindView(R.id.pager)
    ViewPager viewPager;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().requestFeature(8);
        getSupportActionBar().hide();
        getWindow().addFlags(128);
        setContentView((int) R.layout.activity_main);
        ButterKnife.bind((Activity) this);
        this.mFragmentManager = getSupportFragmentManager();
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_LOGS") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WAKE_LOCK")) {
            showRequestPermissionsInfoAlertDialog();
        } else {
            requestReadAndSendSmsPermission();
        }
        this.pagerAdapter = new MainViewPagerAdapter(this.mFragmentManager);
        this.viewPager.setAdapter(this.pagerAdapter);
    }

    public boolean isPermissionGranted() {
        boolean z = true;
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (!(ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_LOGS") == 0)) {
            z = false;
        }
        return z;
    }

    private void requestReadAndSendSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_LOGS", "android.permission.WAKE_LOCK"}, IPTCConstants.IMAGE_RESOURCE_BLOCK_PRINT_FLAGS_INFO);
    }

    public void showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true);
    }

    public void showRequestPermissionsInfoAlertDialog(final boolean z) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle((CharSequence) "获取必要权限");
        builder.setMessage((CharSequence) "SD卡读取权限 \n系统日志读取权限\n");
        builder.setPositiveButton((CharSequence) "OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (z) {
                    MainActivity.this.requestReadAndSendSmsPermission();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    void registerReceiverWrapper() {
        if (this.receiver == null) {
            this.receiver = new SystemWrapperReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.receiver, intentFilter);
    }

    void unRegisterReceiverWrapper() {
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void onStart() {
        super.onStart();
        registerReceiverWrapper();
        Intent intent = new Intent(this, GinCameraService.class);
        intent.putExtra("action", 1);
        startService(intent);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new StatusEvent(4, "onStart"));
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unRegisterReceiverWrapper();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReqEvent(ReqEvent reqEvent) {
        if (!reqEvent.notLogin() || SPUtil.getToken() == null) {
            TUtil.error(this, reqEvent.msg);
            return;
        }
        SPUtil.clearToken();
        SPUtil.clearAccount();
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    @OnClick(R.id.btnConnect)
    public void deviceConnect() {
        Intent intent = new Intent(this, GinCameraService.class);
        intent.putExtra("action", 1);
        startService(intent);
    }

    @OnClick(R.id.btnSetting)
    public void setting() {
        new SettingFragment().show(getSupportFragmentManager(), "SettingDialog");
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}
