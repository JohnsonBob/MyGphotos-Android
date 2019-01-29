package io.gphotos.gin.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.gphotos.gin.MainActivity;
import io.gphotos.gins.R;
import io.gphotos.gin.api.GphotoApi;
import io.gphotos.gin.api.GphotoClient;
import io.gphotos.gin.framework.BaseCompatActivity;
import io.gphotos.gin.manager.AccountManager;
import io.gphotos.gin.model.BaseCallResponse;
import io.gphotos.gin.model.LoginResponse;
import io.gphotos.gin.util.SPUtil;
import io.gphotos.gin.util.TUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseCompatActivity {
    @BindView(R.id.btnLogin2)
    CircularProgressButton btnLogin2;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.rlLogin)
    RelativeLayout rlLogin;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind( this);
        if (AccountManager.getInstance().isLogin()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SplashActivity.this.autoLoginOnce();
                    SplashActivity.this.gotoMainActivity();
                }
            }, 2000);
        } else {
            this.rlLogin.setVisibility(View.VISIBLE);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.btnLogin2.dispose();
    }

    @OnEditorAction(R.id.etCode)
    public boolean onEditorAction(int i) {
        if (i != 6) {
            return false;
        }
        onLoginClick2();
        return true;
    }

    @OnClick(R.id.btnLogin2)
    public void onLoginClick2() {
        final String obj = this.etMobile.getText().toString();
        final String obj2 = this.etCode.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            TUtil.error(this, "账号不能为空");
            this.etMobile.requestFocus();
        } else if (TextUtils.isEmpty(obj2)) {
            TUtil.error(this, "密码不能为空");
            this.etCode.requestFocus();
        } else {
            this.btnLogin2.startAnimation();
            ((GphotoApi) GphotoClient.getInstance().create(GphotoApi.class)).codeLogin(obj, obj2).enqueue(new Callback<BaseCallResponse<LoginResponse>>() {
                public void onResponse(Call<BaseCallResponse<LoginResponse>> call, Response<BaseCallResponse<LoginResponse>> response) {
                    if (response.isSuccessful()) {
                        if (((BaseCallResponse) response.body()).err == 0) {
                            SPUtil.saveToken(((LoginResponse) ((BaseCallResponse) response.body()).res).apiToken);
                            SPUtil.saveAccount(obj, obj2);
                            SplashActivity.this.gotoMainActivity();
                        } else {
                            TUtil.error(SplashActivity.this, ((BaseCallResponse) response.body()).msg);
                        }
                    }
                    SplashActivity.this.btnLogin2.revertAnimation();
                }

                public void onFailure(Call<BaseCallResponse<LoginResponse>> call, Throwable th) {
                    SplashActivity.this.btnLogin2.revertAnimation();
                }
            });
        }
    }

    private void gotoMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void autoLoginOnce() {
        String[] account = SPUtil.getAccount();
        if (account != null && account.length == 2) {
            ((GphotoApi) GphotoClient.getInstance().create(GphotoApi.class)).codeLogin(account[0], account[1]).enqueue(new Callback<BaseCallResponse<LoginResponse>>() {
                public void onFailure(Call<BaseCallResponse<LoginResponse>> call, Throwable th) {
                }

                public void onResponse(Call<BaseCallResponse<LoginResponse>> call, Response<BaseCallResponse<LoginResponse>> response) {
                    if (response.isSuccessful() && ((BaseCallResponse) response.body()).err == 0) {
                        Log.d("autoLogin", "ok");
                        SPUtil.saveToken(((LoginResponse) ((BaseCallResponse) response.body()).res).apiToken);
                    }
                }
            });
        }
    }
}
