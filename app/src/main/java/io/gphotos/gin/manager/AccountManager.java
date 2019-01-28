package io.gphotos.gin.manager;

import android.text.TextUtils;
import io.gphotos.gin.event.StatusEvent;
import io.gphotos.gin.model.HeartResponse;
import io.gphotos.gin.util.FileUtil;
import io.gphotos.gin.util.SPUtil;
import org.greenrobot.eventbus.EventBus;

public class AccountManager {
    public static final int SETTING_UPLOAD_HIGH = 1;
    public static final int SETTING_UPLOAD_LOW = 3;
    public static final int SETTING_UPLOAD_MIDDEL = 2;
    public static final int SETTING_UPLOAD_ORIGINAL = 0;
    private static AccountManager instance = new AccountManager();
    private HeartResponse mInfo;
    private String mToken;
    private int mUploadImageSetting = -1;

    public int getImageUploadSetting() {
        if (this.mUploadImageSetting == -1) {
            this.mUploadImageSetting = SPUtil.getUploadImageSetting();
        }
        return this.mUploadImageSetting;
    }

    public void setImageUploadSetting(int i) {
        this.mUploadImageSetting = i;
        SPUtil.saveUploadImageSetting(i);
    }

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        return instance;
    }

    public boolean isLogin() {
        if (TextUtils.isEmpty(this.mToken)) {
            this.mToken = SPUtil.getToken();
        }
        return TextUtils.isEmpty(this.mToken);
    }

    public String getMobile() {
        String accountMobile = SPUtil.getAccountMobile();
        return accountMobile != null ? accountMobile : "--";
    }

    public void initialize() {
        this.mInfo = SPUtil.getHeartBeat();
        this.mToken = SPUtil.getToken();
    }

    public void updateHeartBeat(HeartResponse heartResponse) {
        this.mInfo = heartResponse;
        SPUtil.saveHeartBeat(heartResponse);
    }

    public void clearLocalDBAndImages() {
        FileUtil.clearDB();
        FileUtil.clearFileCache();
        EventBus.getDefault().post(new StatusEvent(4, "clear"));
    }

    public int getMeetingId() {
        if (this.mInfo == null) {
            this.mInfo = SPUtil.getHeartBeat();
        }
        return (this.mInfo == null || this.mInfo.album == null) ? 0 : this.mInfo.album.meetingId.intValue();
    }

    public int getActivityAlbumId() {
        if (this.mInfo == null) {
            this.mInfo = SPUtil.getHeartBeat();
        }
        return (this.mInfo == null || this.mInfo.album == null) ? 0 : this.mInfo.album.id.intValue();
    }
}
