package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class NikonAfDriveCommand extends NikonCommand {
    public NikonAfDriveCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == Response.Ok) {
            this.camera.onFocusStarted();
            this.camera.enqueue(new NikonAfDriveDeviceReadyCommand(this.camera), 200);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonAfDrive);
    }
}
