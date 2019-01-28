package io.gphotos.gin.lib3.ptp.commands.eos;

import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.commands.Command;
import io.gphotos.gin.lib3.ptp.commands.OpenSessionCommand;

public class EosOpenSessionAction implements PtpAction {
    private final EosCamera camera;

    public void reset() {
    }

    public EosOpenSessionAction(EosCamera eosCamera) {
        this.camera = eosCamera;
    }

    public void exec(IO io) {
        Command openSessionCommand = new OpenSessionCommand(this.camera);
        io.handleCommand(openSessionCommand);
        if (openSessionCommand.getResponseCode() == Response.Ok) {
            openSessionCommand = new EosSetPcModeCommand(this.camera);
            io.handleCommand(openSessionCommand);
            if (openSessionCommand.getResponseCode() == Response.Ok) {
                openSessionCommand = new EosSetExtendedEventInfoCommand(this.camera);
                io.handleCommand(openSessionCommand);
                if (openSessionCommand.getResponseCode() == Response.Ok) {
                    this.camera.onSessionOpened();
                    return;
                }
                this.camera.onPtpError(String.format("Couldn't open session! Setting extended event info failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
            } else {
                this.camera.onPtpError(String.format("Couldn't open session! Setting PcMode property failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
            }
        } else {
            this.camera.onPtpError(String.format("Couldn't open session! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
        }
    }
}
