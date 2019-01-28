package io.gphotos.gin.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.gphotos.gin.R;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.event.UploadEvent;
import io.gphotos.gin.framework.BaseFragment;
import io.gphotos.gin.manager.UploadManager;
import io.gphotos.gin.model.ActivityInfo;
import io.gphotos.gin.model.HeartResponse;
import io.gphotos.gin.util.PhoneUtil;
import io.gphotos.gin.util.SPUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StatusFragment extends BaseFragment {
    @BindView(R.id.btnPhoneNetwork)
    Button btnNetwork;
    @BindView(R.id.btnSystem)
    Button btnSystem;
    private Handler mTimeoutHandle = new Handler();
    private Runnable mTimeoutRunnable = new Runnable() {
        public void run() {
            if (StatusFragment.this.isAdded()) {
                StatusFragment.this.btnSystem.setTextColor(StatusFragment.this.getResources().getColor(R.color.darkFontLight));
            }
        }
    };
    private String statusBattery = "--";
    private String statusCamera = "--";
    private String statusExternalStorage;
    private String statusNetwork;
    @BindView(R.id.txtInfo)
    TextView tvInfo;
    @BindView(R.id.tvOK)
    TextView tvOk;
    @BindView(R.id.txtPhone)
    TextView tvPhone;
    @BindView(R.id.tvReady)
    TextView tvReady;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        d("on create");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PhoneUtil.getAvailableExternalMemorySize());
        stringBuilder.append(" / ");
        stringBuilder.append(PhoneUtil.getTotalExternalMemorySize());
        this.statusExternalStorage = stringBuilder.toString();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_status, viewGroup, false);
        d("on onCreateView");
        ButterKnife.bind((Object) this, inflate);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (StatusFragment.this.isAdded()) {
                    PhoneUtil.checkConnection(StatusFragment.this.getActivity());
                }
            }
        }, 1000);
        return inflate;
    }

    public void onStart() {
        super.onStart();
        d("on onStart");
        EventBus.getDefault().register(this);
        UploadManager.getInstance().notifyUploadStatus();
    }

    public void onStop() {
        super.onStop();
        d("on onStop");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUploadEvent(UploadEvent uploadEvent) {
        if (uploadEvent != null && isAdded()) {
            this.tvOk.setText(String.valueOf(uploadEvent.cntUploaded));
            this.tvReady.setText(String.valueOf(uploadEvent.cntLeft));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionEvent(StatusEvent statusEvent) {
        if (isAdded()) {
            int i = statusEvent.action;
            switch (i) {
                case 1:
                    this.statusBattery = statusEvent.description;
                    break;
                case 2:
                case 6:
                    this.statusNetwork = statusEvent.description;
                    updateNetwork(i);
                    break;
                case 3:
                    this.statusCamera = statusEvent.description;
                    break;
                case 5:
                    this.statusCamera = "未检测到相机";
                    break;
                case 7:
                    ActivityInfo currentActivityInfo = SPUtil.getCurrentActivityInfo();
                    if (currentActivityInfo != null) {
                        this.tvInfo.setText(currentActivityInfo.getInfo());
                        break;
                    }
                    break;
                case 8:
                    HeartResponse heartBeat = SPUtil.getHeartBeat();
                    if (heartBeat != null) {
                        this.tvInfo.setText(heartBeat.getInfo());
                    }
                    updateSystem(i, statusEvent.description);
                    break;
                case 9:
                    updateSystem(i, statusEvent.description);
                    break;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(PhoneUtil.getAvailableExternalMemorySize());
            stringBuilder.append(" / ");
            stringBuilder.append(PhoneUtil.getTotalExternalMemorySize());
            this.statusExternalStorage = stringBuilder.toString();
            updateStatusPhone();
        }
    }

    private void updateStatusPhone() {
        this.tvPhone.setText(String.format("相机：%s \n剩余容量：%s \n手机电量：%s \n", new Object[]{this.statusCamera, this.statusExternalStorage, this.statusBattery}));
    }

    private void updateSystem(int i, String str) {
        this.btnSystem.setTextColor(getResources().getColor(R.color.ok));
        if (i == 9) {
            this.btnSystem.setTextColor(getResources().getColor(R.color.error));
        } else {
            this.mTimeoutHandle.postDelayed(this.mTimeoutRunnable, 3000);
        }
        this.btnSystem.setText(str);
    }

    private void updateNetwork(int i) {
        this.btnNetwork.setTextColor(getResources().getColor(R.color.ok));
        if (i == 6) {
            this.btnNetwork.setTextColor(getResources().getColor(R.color.error));
        }
        this.btnNetwork.setText(this.statusNetwork);
    }

    private void d(String str) {
        Log.d("StatusFragment", str);
    }
}
