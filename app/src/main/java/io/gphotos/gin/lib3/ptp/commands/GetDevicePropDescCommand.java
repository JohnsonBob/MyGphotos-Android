package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.model.DevicePropDesc;
import java.nio.ByteBuffer;

public class GetDevicePropDescCommand extends Command {
    private DevicePropDesc devicePropDesc;
    private final int property;

    public GetDevicePropDescCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.property = i;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.devicePropDesc != null) {
            this.camera.onPropertyDescChanged(this.property, this.devicePropDesc);
            this.camera.onPropertyChanged(this.property, this.devicePropDesc.currentValue);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetDevicePropDesc, this.property);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.devicePropDesc = new DevicePropDesc(byteBuffer, i);
    }
}
