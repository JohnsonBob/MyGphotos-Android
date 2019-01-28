package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.Camera.RetrieveImageListener;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;

public class RetrieveImageAction implements PtpAction {
    private final PtpCamera camera;
    private final RetrieveImageListener listener;
    private final int objectHandle;
    private final int sampleSize;

    public void reset() {
    }

    public RetrieveImageAction(PtpCamera ptpCamera, RetrieveImageListener retrieveImageListener, int i, int i2) {
        this.camera = ptpCamera;
        this.listener = retrieveImageListener;
        this.objectHandle = i;
        this.sampleSize = i2;
    }

    public void exec(IO io) {
        GetObjectCommand getObjectCommand = new GetObjectCommand(this.camera, this.objectHandle, this.sampleSize);
        io.handleCommand(getObjectCommand);
        if (getObjectCommand.getResponseCode() != Response.Ok || getObjectCommand.getBytes() == null) {
            this.listener.onBytesRetrieved(0, null);
        } else {
            this.listener.onBytesRetrieved(this.objectHandle, getObjectCommand.getBytes());
        }
    }
}
