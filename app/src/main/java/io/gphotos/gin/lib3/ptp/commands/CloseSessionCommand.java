package io.gphotos.gin.lib3.ptp.commands;

import android.util.Log;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class CloseSessionCommand extends Command {
    private final String TAG = CloseSessionCommand.class.getSimpleName();

    public CloseSessionCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
            return;
        }
        this.camera.onSessionClosed();
        if (this.responseCode != Response.Ok) {
            Log.w(this.TAG, String.format("Error response when closing session, response %s", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, 4099);
    }
}
