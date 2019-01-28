package io.gphotos.gin.lib3.ptp.commands.eos;

import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosTakePictureCommand extends EosCommand {
    public EosTakePictureCommand(EosCamera eosCamera) {
        super(eosCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosTakePicture);
    }
}
