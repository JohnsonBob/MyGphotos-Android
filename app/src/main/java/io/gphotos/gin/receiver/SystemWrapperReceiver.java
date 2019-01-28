package io.gphotos.gin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import io.gphotos.gin.event.StatusEvent;
import org.greenrobot.eventbus.EventBus;

public class SystemWrapperReceiver extends BroadcastReceiver {
    /* JADX WARNING: Removed duplicated region for block: B:16:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003c  */
    public void onReceive(android.content.Context r6, android.content.Intent r7) {
        /*
        r5 = this;
        r0 = r7.getAction();
        r1 = org.greenrobot.eventbus.EventBus.getDefault();
        r2 = new io.gphotos.gin.event.StatusEvent;
        r3 = "SystemWrapperReceiver";
        r4 = 0;
        r2.<init>(r4, r3);
        r1.post(r2);
        r1 = r0.hashCode();
        r2 = -1538406691; // 0xffffffffa44dc6dd float:-4.4620733E-17 double:NaN;
        r3 = 1;
        if (r1 == r2) goto L_0x002d;
    L_0x001d:
        r2 = -1172645946; // 0xffffffffba1ad7c6 float:-5.9067865E-4 double:NaN;
        if (r1 == r2) goto L_0x0023;
    L_0x0022:
        goto L_0x0037;
    L_0x0023:
        r1 = "android.net.conn.CONNECTIVITY_CHANGE";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0037;
    L_0x002b:
        r0 = 0;
        goto L_0x0038;
    L_0x002d:
        r1 = "android.intent.action.BATTERY_CHANGED";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0037;
    L_0x0035:
        r0 = 1;
        goto L_0x0038;
    L_0x0037:
        r0 = -1;
    L_0x0038:
        switch(r0) {
            case 0: goto L_0x0060;
            case 1: goto L_0x003c;
            default: goto L_0x003b;
        };
    L_0x003b:
        goto L_0x0063;
    L_0x003c:
        r6 = "level";
        r6 = r7.getIntExtra(r6, r4);
        r7 = org.greenrobot.eventbus.EventBus.getDefault();
        r0 = new io.gphotos.gin.event.StatusEvent;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r6);
        r6 = "%";
        r1.append(r6);
        r6 = r1.toString();
        r0.<init>(r3, r6);
        r7.post(r0);
        goto L_0x0063;
    L_0x0060:
        r5.checkConnection(r6);
    L_0x0063:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.receiver.SystemWrapperReceiver.onReceive(android.content.Context, android.content.Intent):void");
    }

    private void checkConnection(Context context) {
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
}
