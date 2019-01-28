package io.gphotos.gin.lib3.ptp;

import io.gphotos.gin.lib3.ptp.PtpCamera.IO;

public interface PtpAction {
    void exec(IO io);

    void reset();
}
