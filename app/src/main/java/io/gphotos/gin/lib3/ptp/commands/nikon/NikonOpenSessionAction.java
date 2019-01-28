package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.commands.Command;
import io.gphotos.gin.lib3.ptp.commands.OpenSessionCommand;

public class NikonOpenSessionAction implements PtpAction {
    private final NikonCamera camera;

    public void reset() {
    }

    public NikonOpenSessionAction(NikonCamera nikonCamera) {
        this.camera = nikonCamera;
    }

    public void exec(IO io) {
        Command openSessionCommand = new OpenSessionCommand(this.camera);
        io.handleCommand(openSessionCommand);
        if (openSessionCommand.getResponseCode() == Response.Ok) {
            this.camera.onSessionOpened();
            return;
        }
        this.camera.onPtpError(String.format("Couldn't open session! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
    }
}
