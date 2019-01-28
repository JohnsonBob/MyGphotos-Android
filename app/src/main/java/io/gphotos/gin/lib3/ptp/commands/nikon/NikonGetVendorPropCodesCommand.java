package io.gphotos.gin.lib3.ptp.commands.nikon;

import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PacketUtil;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class NikonGetVendorPropCodesCommand extends NikonCommand {
    private int[] propertyCodes = new int[0];

    public NikonGetVendorPropCodesCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    public int[] getPropertyCodes() {
        return this.propertyCodes;
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonGetVendorPropCodes);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.propertyCodes = PacketUtil.readU16Array(byteBuffer);
    }
}
