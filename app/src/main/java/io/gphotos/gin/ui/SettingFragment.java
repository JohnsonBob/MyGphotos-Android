package io.gphotos.gin.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.gphotos.gin.R;
import io.gphotos.gin.manager.AccountManager;
import io.gphotos.gin.manager.UploadManager;
import io.gphotos.gin.service.GinCameraService;
import io.gphotos.gin.util.SPUtil;
import io.gphotos.gin.util.TUtil;
import android.app.AlertDialog.Builder;

public class SettingFragment extends DialogFragment {
    @BindView(R.id.btnClearDB)
    Button btnClear;
    @BindView(R.id.btnConnectCamera)
    Button btnConnect;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnUpload)
    Button btnUpload;
    @BindView(R.id.radio_2880)
    RadioButton radio2880;
    @BindView(R.id.radio_original)
    RadioButton radioOriginal;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        int imageUploadSetting = AccountManager.getInstance().getImageUploadSetting();
        if (imageUploadSetting == 0) {
            this.radioOriginal.setChecked(true);
        } else if (imageUploadSetting == 1) {
            this.radio2880.setChecked(true);
        }
        return inflate;
    }

    @OnCheckedChanged({R.id.radio_original,R.id.radio_2880})
    public void onRadioButtonCheckChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            switch (compoundButton.getId()) {
                case R.id.radio_2880:
                    AccountManager.getInstance().setImageUploadSetting(1);
                    return;
                case R.id.radio_original:
                    AccountManager.getInstance().setImageUploadSetting(0);
                    return;
                default:
                    return;
            }
        }
    }

    @OnClick(R.id.btnUpload)
    public void btnUpload() {
        UploadManager.getInstance().startUploadManual();
        TUtil.info(getContext(), "已启动");
    }

    @OnClick(R.id.btnConnectCamera)
    public void btnConnect() {
        Intent intent = new Intent(getActivity(), GinCameraService.class);
        intent.putExtra("action", 1);
        getActivity().startService(intent);
    }

    @OnClick({2131230756})
    public void btnClear() {
        Builder builder = new Builder(getActivity());
        builder.setTitle((CharSequence) "危险操作");
        builder.setMessage((CharSequence) "确定要清空本地数据库及缓存文件么？？");
        builder.setPositiveButton((CharSequence) "确定清空", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AccountManager.getInstance().clearLocalDBAndImages();
                UploadManager.getInstance().clear();
                TUtil.info(SettingFragment.this.getContext(), "已清除");
            }
        });
        builder.setNeutralButton((CharSequence) "取消", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

    @OnClick({2131230757})
    public void btnClose() {
        dismiss();
    }

    @OnClick(R.id.btnLogout)
    public void btnLogout() {
        Builder builder = new Builder(getActivity());
        builder.setTitle((CharSequence) "危险操作");
        builder.setMessage((CharSequence) "确定登出？？");
        builder.setPositiveButton((CharSequence) "确定", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Activity activity = SettingFragment.this.getActivity();
                if (activity != null) {
                    SPUtil.clearToken();
                    SPUtil.clearAccount();
                    activity.startActivity(new Intent(activity, SplashActivity.class));
                    activity.finish();
                }
            }
        });
        builder.setNeutralButton((CharSequence) "取消", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setCancelable(true);
        builder.show();
    }
}
