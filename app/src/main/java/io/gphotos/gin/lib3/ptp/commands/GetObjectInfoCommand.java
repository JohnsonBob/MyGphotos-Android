package io.gphotos.gin.lib3.ptp.commands;

import android.util.Log;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.model.ObjectInfo;
import java.nio.ByteBuffer;

public class GetObjectInfoCommand extends Command {
    private final String TAG = GetObjectInfoCommand.class.getSimpleName();
    private ObjectInfo inObjectInfo;
    private final int outObjectHandle;

    public GetObjectInfoCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.outObjectHandle = i;
    }

    public ObjectInfo getObjectInfo() {
        return this.inObjectInfo;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.inObjectInfo != null) {
            Log.i(this.TAG, this.inObjectInfo.toString());
        }
    }

    public void reset() {
        super.reset();
        this.inObjectInfo = null;
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetObjectInfo, this.outObjectHandle);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.inObjectInfo = new ObjectInfo(byteBuffer, i);
    }
}
