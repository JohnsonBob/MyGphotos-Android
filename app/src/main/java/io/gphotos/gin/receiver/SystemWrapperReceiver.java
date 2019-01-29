package io.gphotos.gin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import io.gphotos.gin.event.StatusEvent;
import org.greenrobot.eventbus.EventBus;

public class SystemWrapperReceiver extends BroadcastReceiver {

    public void onReceive(android.content.Context context, android.content.Intent intent) {
        int actionCode;
        String action = intent.getAction();
        EventBus.getDefault().post(new StatusEvent(0, "SystemWrapperReceiver"));
        int hashCode = action.hashCode();
        if(hashCode != -1538406691) {
            if(hashCode != -1172645946) {
                actionCode = -1;
            }
            else if(action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                actionCode = 0;
            }
            else {
                actionCode = -1;
            }
        }
        else if(action.equals("android.intent.action.BATTERY_CHANGED")) {
            actionCode = 1;
        }
        else {
            actionCode = -1;
        }

        switch(actionCode) {
            case 0: {
                this.checkConnection(context);
                break;
            }
            case 1: {
                int level = intent.getIntExtra("level", 0);
                EventBus eventBus = EventBus.getDefault();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(level);
                stringBuilder.append("%");
                eventBus.post(new StatusEvent(1, stringBuilder.toString()));
                break;
            }
            default: {
                break;
            }
        }
    }

    private void checkConnection(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
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
}
