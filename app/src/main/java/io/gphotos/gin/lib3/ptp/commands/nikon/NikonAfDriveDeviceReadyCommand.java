package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class NikonAfDriveDeviceReadyCommand extends NikonCommand {
    public NikonAfDriveDeviceReadyCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == Response.DeviceBusy) {
            reset();
            this.camera.enqueue(this, 200);
            return;
        }
        this.camera.onFocusEnded(getResponseCode() == Response.Ok);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonDeviceReady);
    }
}
