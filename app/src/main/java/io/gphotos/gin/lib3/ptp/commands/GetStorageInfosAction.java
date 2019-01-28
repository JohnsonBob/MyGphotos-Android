package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.Camera.StorageInfoListener;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;

public class GetStorageInfosAction implements PtpAction {
    private final PtpCamera camera;
    private final StorageInfoListener listener;

    public void reset() {
    }

    public GetStorageInfosAction(PtpCamera ptpCamera, StorageInfoListener storageInfoListener) {
        this.camera = ptpCamera;
        this.listener = storageInfoListener;
    }

    public void exec(IO io) {
        GetStorageIdsCommand getStorageIdsCommand = new GetStorageIdsCommand(this.camera);
        io.handleCommand(getStorageIdsCommand);
        if (getStorageIdsCommand.getResponseCode() != Response.Ok) {
            this.listener.onAllStoragesFound();
            return;
        }
        int[] storageIds = getStorageIdsCommand.getStorageIds();
        for (int i = 0; i < storageIds.length; i++) {
            int i2 = storageIds[i];
            GetStorageInfoCommand getStorageInfoCommand = new GetStorageInfoCommand(this.camera, i2);
            io.handleCommand(getStorageInfoCommand);
            if (getStorageInfoCommand.getResponseCode() == Response.Ok) {
                String str;
                if (getStorageInfoCommand.getStorageInfo().volumeLabel.isEmpty()) {
                    str = getStorageInfoCommand.getStorageInfo().storageDescription;
                } else {
                    str = getStorageInfoCommand.getStorageInfo().volumeLabel;
                }
                if (str == null || str.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Storage ");
                    stringBuilder.append(i);
                    str = stringBuilder.toString();
                }
                this.listener.onStorageFound(i2, str);
            }
        }
        this.listener.onAllStoragesFound();
    }
}
