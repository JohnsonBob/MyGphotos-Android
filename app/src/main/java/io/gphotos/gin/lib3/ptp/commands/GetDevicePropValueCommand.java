package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class GetDevicePropValueCommand extends Command {
    private final int datatype;
    private final int property;
    private int value;

    public GetDevicePropValueCommand(PtpCamera ptpCamera, int i, int i2) {
        super(ptpCamera);
        this.property = i;
        this.datatype = i2;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.responseCode == Response.Ok) {
            this.camera.onPropertyChanged(this.property, this.value);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetDevicePropValue, this.property);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        if (this.datatype == 1) {
            this.value = byteBuffer.get();
        } else if (this.datatype == 2) {
            this.value = byteBuffer.get() & 255;
        } else if (this.datatype == 4) {
            this.value = byteBuffer.getShort() & 65535;
        } else if (this.datatype == 3) {
            this.value = byteBuffer.getShort();
        } else if (this.datatype == 5 || this.datatype == 6) {
            this.value = byteBuffer.getInt();
        }
    }
}
