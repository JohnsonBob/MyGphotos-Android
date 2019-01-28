package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.commands.Command;
import io.gphotos.gin.lib3.ptp.commands.SimpleCommand;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;

public class NikonGetLiveViewImageAction implements PtpAction {
    private final NikonCamera camera;
    private final LiveViewData reuse;

    public void reset() {
    }

    public NikonGetLiveViewImageAction(NikonCamera nikonCamera, LiveViewData liveViewData) {
        this.camera = nikonCamera;
        this.reuse = liveViewData;
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
                        this.camera.onLiveViewRestarted();
                        io.handleCommand(new NikonGetLiveViewImageCommand(this.camera, this.reuse));
                        return;
                    } else {
                        return;
                    }
                }
            }
        }
    }
}
