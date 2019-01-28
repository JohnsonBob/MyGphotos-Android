package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera;

public class RetrievePictureAction implements PtpAction {
    private final PtpCamera camera;
    private final int objectHandle;
    private final int sampleSize;

    public void reset() {
    }

    public RetrievePictureAction(PtpCamera ptpCamera, int i, int i2) {
        this.camera = ptpCamera;
        this.objectHandle = i;
        this.sampleSize = i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0055 A:{RETURN} */
    public void exec(io.gphotos.gin.lib3.ptp.PtpCamera.IO r9) {
        /*
        r8 = this;
        r0 = new io.gphotos.gin.lib3.ptp.commands.GetObjectInfoCommand;
        r1 = r8.camera;
        r2 = r8.objectHandle;
        r0.<init>(r1, r2);
        r9.handleCommand(r0);
        r1 = r0.getResponseCode();
        r2 = 8193; // 0x2001 float:1.1481E-41 double:4.048E-320;
        if (r1 == r2) goto L_0x0015;
    L_0x0014:
        return;
    L_0x0015:
        r1 = r0.getObjectInfo();
        if (r1 != 0) goto L_0x001c;
    L_0x001b:
        return;
    L_0x001c:
        r3 = r1.thumbFormat;
        r4 = 14344; // 0x3808 float:2.01E-41 double:7.087E-320;
        r5 = 0;
        if (r3 == r4) goto L_0x0029;
    L_0x0023:
        r1 = r1.thumbFormat;
        r3 = 14337; // 0x3801 float:2.009E-41 double:7.0834E-320;
        if (r1 != r3) goto L_0x0040;
    L_0x0029:
        r1 = new io.gphotos.gin.lib3.ptp.commands.GetThumb;
        r3 = r8.camera;
        r4 = r8.objectHandle;
        r1.<init>(r3, r4);
        r9.handleCommand(r1);
        r3 = r1.getResponseCode();
        if (r3 != r2) goto L_0x0040;
    L_0x003b:
        r1 = r1.getBitmap();
        goto L_0x0041;
    L_0x0040:
        r1 = r5;
    L_0x0041:
        r3 = new io.gphotos.gin.lib3.ptp.commands.GetObjectCommand;
        r4 = r8.camera;
        r6 = r8.objectHandle;
        r7 = r8.sampleSize;
        r3.<init>(r4, r6, r7);
        r9.handleCommand(r3);
        r9 = r3.getResponseCode();
        if (r9 == r2) goto L_0x0056;
    L_0x0055:
        return;
    L_0x0056:
        r9 = r3.getBitmap();
        if (r9 != 0) goto L_0x0070;
    L_0x005c:
        r9 = r3.isOutOfMemoryError();
        if (r9 == 0) goto L_0x006f;
    L_0x0062:
        r9 = r8.camera;
        r2 = r8.objectHandle;
        r0 = r0.getObjectInfo();
        r0 = r0.filename;
        r9.onPictureReceived(r2, r0, r1, r5);
    L_0x006f:
        return;
    L_0x0070:
        r9 = r8.camera;
        r2 = r8.objectHandle;
        r0 = r0.getObjectInfo();
        r0 = r0.filename;
        r3 = r3.getBitmap();
        r9.onPictureReceived(r2, r0, r1, r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.lib3.ptp.commands.RetrievePictureAction.exec(io.gphotos.gin.lib3.ptp.PtpCamera$IO):void");
    }
}
