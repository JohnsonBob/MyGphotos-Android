package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;

public class RetrieveAddedObjectInfoAction implements PtpAction {
    private final PtpCamera camera;
    private final int objectHandle;

    public void reset() {
    }

    public RetrieveAddedObjectInfoAction(PtpCamera ptpCamera, int i) {
        this.camera = ptpCamera;
        this.objectHandle = i;
    }

    public void exec(IO io) {
        GetObjectInfoCommand getObjectInfoCommand = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectInfoCommand);
        if (getObjectInfoCommand.getResponseCode() == Response.Ok && getObjectInfoCommand.getObjectInfo() != null) {
            this.camera.onEventObjectAdded(this.objectHandle, getObjectInfoCommand.getObjectInfo().objectFormat);
        }
    }
}
