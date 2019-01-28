package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class OpenSessionCommand extends Command {
    public OpenSessionCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.Ok) {
            this.camera.onSessionOpened();
            return;
        }
        this.camera.onPtpError(String.format("Couldn't open session, error code \"%s\"", new Object[]{PtpConstants.responseToString(this.responseCode)}));
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        this.camera.resetTransactionId();
        encodeCommand(byteBuffer, 4098, 1);
    }
}
