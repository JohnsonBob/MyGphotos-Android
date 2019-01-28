package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.model.DeviceInfo;
import java.nio.ByteBuffer;

public class GetDeviceInfoCommand extends Command {
    private DeviceInfo info;

    public GetDeviceInfoCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode != Response.Ok) {
            this.camera.onPtpError(String.format("Couldn't read device information, error code \"%s\"", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        } else if (this.info == null) {
            this.camera.onPtpError("Couldn't retrieve device information");
        }
    }

    public void reset() {
        super.reset();
        this.info = null;
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, 4097);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.info = new DeviceInfo(byteBuffer, i);
        this.camera.setDeviceInfo(this.info);
    }
}
