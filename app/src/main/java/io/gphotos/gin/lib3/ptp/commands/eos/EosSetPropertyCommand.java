package io.gphotos.gin.lib3.ptp.commands.eos;

import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosSetPropertyCommand extends EosCommand {
    private final int property;
    private final int value;

    public EosSetPropertyCommand(EosCamera eosCamera, int i, int i2) {
        super(eosCamera);
        this.hasDataToSend = true;
        this.property = i;
        this.value = i2;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosSetDevicePropValue);
    }

    public void encodeData(ByteBuffer byteBuffer) {
        byteBuffer.putInt(24);
        byteBuffer.putShort((short) 2);
        byteBuffer.putShort((short) -28400);
        byteBuffer.putInt(this.camera.currentTransactionId());
        byteBuffer.putInt(12);
        byteBuffer.putInt(this.property);
        byteBuffer.putInt(this.value);
    }
}
