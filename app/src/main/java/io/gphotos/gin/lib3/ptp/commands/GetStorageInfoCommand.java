package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.model.StorageInfo;
import java.nio.ByteBuffer;

public class GetStorageInfoCommand extends Command {
    private final int storageId;
    private StorageInfo storageInfo;

    public StorageInfo getStorageInfo() {
        return this.storageInfo;
    }

    public GetStorageInfoCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.storageId = i;
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        super.encodeCommand(byteBuffer, Operation.GetStorageInfo, this.storageId);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.storageInfo = new StorageInfo(byteBuffer, i);
    }
}
