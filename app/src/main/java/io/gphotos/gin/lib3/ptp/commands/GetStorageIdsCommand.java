package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PacketUtil;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class GetStorageIdsCommand extends Command {
    private int[] storageIds;

    public int[] getStorageIds() {
        if (this.storageIds == null) {
            return new int[0];
        }
        return this.storageIds;
    }

    public GetStorageIdsCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        super.encodeCommand(byteBuffer, Operation.GetStorageIDs);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.storageIds = PacketUtil.readU32Array(byteBuffer);
    }
}
