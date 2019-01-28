package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.commands.Command;
import io.gphotos.gin.lib3.ptp.commands.SimpleCommand;

public class NikonStopLiveViewAction implements PtpAction {
    private final NikonCamera camera;
    private final boolean notifyUser;

    public void reset() {
    }

    public NikonStopLiveViewAction(NikonCamera nikonCamera, boolean z) {
        this.camera = nikonCamera;
        this.notifyUser = z;
    }

    public void exec(IO io) {
        Command simpleCommand = new SimpleCommand(this.camera, Operation.NikonEndLiveView);
        io.handleCommand(simpleCommand);
        if (simpleCommand.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (this.notifyUser) {
            this.camera.onLiveViewStopped();
        } else {
            this.camera.onLiveViewStoppedInternal();
        }
    }
}
