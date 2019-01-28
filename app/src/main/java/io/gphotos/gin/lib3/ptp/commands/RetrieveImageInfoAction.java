package io.gphotos.gin.lib3.ptp.commands;

import android.graphics.Bitmap;
import io.gphotos.gin.lib3.ptp.Camera.RetrieveImageInfoListener;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.ObjectFormat;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.model.ObjectInfo;

public class RetrieveImageInfoAction implements PtpAction {
    private final PtpCamera camera;
    private final RetrieveImageInfoListener listener;
    private final int objectHandle;

    public void reset() {
    }

    public RetrieveImageInfoAction(PtpCamera ptpCamera, RetrieveImageInfoListener retrieveImageInfoListener, int i) {
        this.camera = ptpCamera;
        this.listener = retrieveImageInfoListener;
        this.objectHandle = i;
    }

    public void exec(IO io) {
        GetObjectInfoCommand getObjectInfoCommand = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectInfoCommand);
        if (getObjectInfoCommand.getResponseCode() == Response.Ok) {
            ObjectInfo objectInfo = getObjectInfoCommand.getObjectInfo();
            if (objectInfo != null) {
                Bitmap bitmap = null;
                if (objectInfo.thumbFormat == ObjectFormat.JFIF || objectInfo.thumbFormat == ObjectFormat.EXIF_JPEG) {
                    GetThumb getThumb = new GetThumb(this.camera, this.objectHandle);
                    io.handleCommand(getThumb);
                    if (getThumb.getResponseCode() == Response.Ok) {
                        bitmap = getThumb.getBitmap();
                    }
                }
                this.listener.onImageInfoRetrieved(this.objectHandle, getObjectInfoCommand.getObjectInfo(), bitmap);
            }
        }
    }
}
