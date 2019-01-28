package io.gphotos.gin.lib3.ptp.commands.eos;

import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.commands.Command;

public abstract class EosCommand extends Command {
    protected EosCamera camera;

    public EosCommand(EosCamera eosCamera) {
        super(eosCamera);
        this.camera = eosCamera;
    }
}
