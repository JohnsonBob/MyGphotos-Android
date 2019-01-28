package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.commands.Command;

public abstract class NikonCommand extends Command {
    protected NikonCamera camera;

    public NikonCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
        this.camera = nikonCamera;
    }
}
