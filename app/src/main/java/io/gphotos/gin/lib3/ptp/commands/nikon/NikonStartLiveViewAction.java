package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.commands.Command;
import io.gphotos.gin.lib3.ptp.commands.SimpleCommand;

public class NikonStartLiveViewAction implements PtpAction {
    private final NikonCamera camera;

    public void reset() {
    }

    public NikonStartLiveViewAction(NikonCamera nikonCamera) {
        this.camera = nikonCamera;
    }

    public void exec(IO io) {
        Command simpleCommand = new SimpleCommand(this.camera, Operation.NikonStartLiveView);
        io.handleCommand(simpleCommand);
        if (simpleCommand.getResponseCode() == Response.Ok) {
            simpleCommand = new SimpleCommand(this.camera, Operation.NikonDeviceReady);
            int i = 0;
            while (i < 10) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException unused) {
                    simpleCommand.reset();
                    io.handleCommand(simpleCommand);
                    if (simpleCommand.getResponseCode() == Response.DeviceBusy) {
                        i++;
                    } else if (simpleCommand.getResponseCode() == Response.Ok) {
                        this.camera.onLiveViewStarted();
                        return;
                    } else {
                        return;
                    }
                }
            }
        }
    }
}
