package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Property;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.commands.CloseSessionCommand;
import io.gphotos.gin.lib3.ptp.commands.Command;
import io.gphotos.gin.lib3.ptp.commands.SetDevicePropValueCommand;

public class NikonCloseSessionAction implements PtpAction {
    private final NikonCamera camera;

    public void reset() {
    }

    public NikonCloseSessionAction(NikonCamera nikonCamera) {
        this.camera = nikonCamera;
    }

    public void exec(IO io) {
        Command setDevicePropValueCommand = new SetDevicePropValueCommand(this.camera, Property.NikonRecordingMedia, 0, 2);
        io.handleCommand(setDevicePropValueCommand);
        if (setDevicePropValueCommand.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
            return;
        }
        io.handleCommand(new CloseSessionCommand(this.camera));
        this.camera.onSessionClosed();
    }
}
