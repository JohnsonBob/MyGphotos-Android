package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class SetDevicePropValueCommand extends Command {
    private final int datatype;
    private final int property;
    private final int value;

    public SetDevicePropValueCommand(PtpCamera ptpCamera, int i, int i2, int i3) {
        super(ptpCamera);
        this.property = i;
        this.value = i2;
        this.datatype = i3;
        this.hasDataToSend = true;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
            return;
        }
        if (this.responseCode == Response.Ok) {
            this.camera.onPropertyChanged(this.property, this.value);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.SetDevicePropValue, this.property);
    }

    public void encodeData(ByteBuffer byteBuffer) {
        byteBuffer.putInt(PtpConstants.getDatatypeSize(this.datatype) + 12);
        byteBuffer.putShort((short) 2);
        byteBuffer.putShort((short) 4118);
        byteBuffer.putInt(this.camera.currentTransactionId());
        if (this.datatype == 1 || this.datatype == 2) {
            byteBuffer.put((byte) this.value);
        } else if (this.datatype == 3 || this.datatype == 4) {
            byteBuffer.putShort((short) this.value);
        } else if (this.datatype == 5 || this.datatype == 6) {
            byteBuffer.putInt(this.value);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
