package io.gphotos.gin.lib3.ptp.commands.eos;

import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosSetPcModeCommand extends EosCommand {
    public EosSetPcModeCommand(EosCamera eosCamera) {
        super(eosCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode != Response.Ok) {
            this.camera.onPtpError(String.format("Couldn't initialize session! setting PC Mode failed, error code %s", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosSetPCConnectMode, 1);
    }
}
