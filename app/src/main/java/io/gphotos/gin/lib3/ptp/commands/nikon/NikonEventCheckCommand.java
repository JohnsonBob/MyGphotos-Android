package io.gphotos.gin.lib3.ptp.commands.nikon;

import android.util.Log;
import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class NikonEventCheckCommand extends NikonCommand {
    private static final String TAG = "NikonEventCheckCommand";

    public NikonEventCheckCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonGetEvent);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        i = byteBuffer.getShort();
        while (i > 0) {
            i--;
            short s = byteBuffer.getShort();
            int i2 = byteBuffer.getInt();
            Log.i(TAG, String.format("event %s value %s(%04x)", new Object[]{PtpConstants.eventToString(s), PtpConstants.propertyToString(i2), Integer.valueOf(i2)}));
            if (s == (short) 16386) {
                this.camera.onEventObjectAdded(i2);
            } else if (s == (short) 16390) {
                this.camera.onEventDevicePropChanged(i2);
            } else if (s == (short) 16397) {
                this.camera.onEventCaptureComplete();
            }
        }
    }
}
