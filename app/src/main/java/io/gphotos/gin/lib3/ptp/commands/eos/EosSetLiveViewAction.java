package io.gphotos.gin.lib3.ptp.commands.eos;

import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Property;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.commands.Command;

public class EosSetLiveViewAction implements PtpAction {
    private final EosCamera camera;
    private final boolean enabled;

    public void reset() {
    }

    public EosSetLiveViewAction(EosCamera eosCamera, boolean z) {
        this.camera = eosCamera;
        this.enabled = z;
    }

    public void exec(IO io) {
        int ptpProperty = this.camera.getPtpProperty(Property.EosEvfMode);
        if ((this.enabled && ptpProperty != 1) || !(this.enabled || ptpProperty == 0)) {
            Command eosSetPropertyCommand = new EosSetPropertyCommand(this.camera, Property.EosEvfMode, this.enabled == true ?1 :0);
            io.handleCommand(eosSetPropertyCommand);
            if (eosSetPropertyCommand.getResponseCode() == Response.DeviceBusy) {
                this.camera.onDeviceBusy(this, true);
                return;
            } else if (eosSetPropertyCommand.getResponseCode() != Response.Ok) {
                this.camera.onPtpWarning("Couldn't open live view");
                return;
            }
        }
        ptpProperty = this.camera.getPtpProperty(Property.EosEvfOutputDevice);
        Command eosSetPropertyCommand2 = new EosSetPropertyCommand(this.camera, Property.EosEvfOutputDevice, this.enabled ? ptpProperty | 2 : ptpProperty & -3);
        io.handleCommand(eosSetPropertyCommand2);
        if (eosSetPropertyCommand2.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (eosSetPropertyCommand2.getResponseCode() == Response.Ok) {
            if (this.enabled) {
                this.camera.onLiveViewStarted();
            } else {
                this.camera.onLiveViewStopped();
            }
        } else {
            this.camera.onPtpWarning("Couldn't open live view");
        }
    }
}
