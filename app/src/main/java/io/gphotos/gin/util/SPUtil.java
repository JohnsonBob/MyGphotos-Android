package io.gphotos.gin.util;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import io.gphotos.gin.model.ActivityInfo;
import io.gphotos.gin.model.HeartResponse;

public class SPUtil {
    private static final String KEY_ACCOUNT_CODE = "gCode";
    private static final String KEY_ACCOUNT_MOBILE = "gMobile";
    private static final String KEY_HEART = "gHeart";
    private static final String KEY_INFO = "gInfo";
    private static final String KEY_TOKEN = "gToken";
    private static final String KEY_UPLOAD_IMAGE = "gUpload";

    public static void saveUploadImageSetting(int i) {
        MMKV.mmkvWithID("gin", 2).encode(KEY_UPLOAD_IMAGE, i);
    }

    public static int getUploadImageSetting() {
        return MMKV.mmkvWithID("gin", 2).decodeInt(KEY_UPLOAD_IMAGE);
    }

    public static void saveToken(String str) {
        MMKV.mmkvWithID("gin", 2).encode(KEY_TOKEN, str);
    }

    public static void saveAccount(String str, String str2) {
        MMKV mmkvWithID = MMKV.mmkvWithID("gin", 2);
        mmkvWithID.encode(KEY_ACCOUNT_MOBILE, str);
        mmkvWithID.encode(KEY_ACCOUNT_CODE, str2);
    }

    public static String getAccountMobile() {
        return MMKV.mmkvWithID("gin", 2).decodeString(KEY_ACCOUNT_MOBILE);
    }

    public static String[] getAccount() {
        MMKV mmkvWithID = MMKV.mmkvWithID("gin", 2);
        String decodeString = mmkvWithID.decodeString(KEY_ACCOUNT_MOBILE);
        String decodeString2 = mmkvWithID.decodeString(KEY_ACCOUNT_CODE);
        return new String[]{decodeString, decodeString2};
    }

    public static void clearAccount() {
        MMKV mmkvWithID = MMKV.mmkvWithID("gin", 2);
        mmkvWithID.removeValueForKey(KEY_ACCOUNT_MOBILE);
        mmkvWithID.removeValueForKey(KEY_ACCOUNT_CODE);
    }

    public static String getToken() {
        return MMKV.mmkvWithID("gin", 2).decodeString(KEY_TOKEN);
    }

    public static void clearToken() {
        MMKV.mmkvWithID("gin", 2).removeValueForKey(KEY_TOKEN);
    }

    public static void saveHeartBeat(HeartResponse heartResponse) {
        if (heartResponse != null) {
            MMKV.mmkvWithID("gin", 2).encode(KEY_HEART, new Gson().toJson((Object) heartResponse));
        }
    }

    public static HeartResponse getHeartBeat() {
        String decodeString = MMKV.mmkvWithID("gin", 2).decodeString(KEY_HEART);
        return (decodeString == null || decodeString.length() <= 0) ? null : (HeartResponse) new Gson().fromJson(decodeString, HeartResponse.class);
    }

    public static void clearHeartBeat() {
        MMKV.mmkvWithID("gin", 2).removeValueForKey(KEY_HEART);
    }

    public static void saveCurrentActivityInfo(ActivityInfo activityInfo) {
        if (activityInfo != null) {
            MMKV.mmkvWithID("gin", 2).encode(KEY_INFO, new Gson().toJson((Object) activityInfo));
        }
    }

    public static ActivityInfo getCurrentActivityInfo() {
        String decodeString = MMKV.mmkvWithID("gin", 2).decodeString(KEY_INFO);
        return (decodeString == null || decodeString.length() <= 0) ? null : (ActivityInfo) new Gson().fromJson(decodeString, ActivityInfo.class);
    }

    public static void clearCurrentActivitInfo() {
        MMKV.mmkvWithID("gin", 2).removeValueForKey(KEY_INFO);
    }
}
