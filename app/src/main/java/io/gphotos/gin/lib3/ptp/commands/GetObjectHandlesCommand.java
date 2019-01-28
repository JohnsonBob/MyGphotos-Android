package io.gphotos.gin.lib3.ptp.commands;

import io.gphotos.gin.lib3.ptp.Camera.StorageInfoListener;
import io.gphotos.gin.lib3.ptp.PacketUtil;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class GetObjectHandlesCommand extends Command {
    private final int associationHandle;
    private final StorageInfoListener listener;
    private final int objectFormat;
    private int[] objectHandles;
    private final int storageId;

    public int[] getObjectHandles() {
        if (this.objectHandles == null) {
            return new int[0];
        }
        return this.objectHandles;
    }

    public GetObjectHandlesCommand(PtpCamera ptpCamera, StorageInfoListener storageInfoListener, int i) {
        this(ptpCamera, storageInfoListener, i, 0, 0);
    }

    public GetObjectHandlesCommand(PtpCamera ptpCamera, StorageInfoListener storageInfoListener, int i, int i2) {
        this(ptpCamera, storageInfoListener, i, i2, 0);
    }

    public GetObjectHandlesCommand(PtpCamera ptpCamera, StorageInfoListener storageInfoListener, int i, int i2, int i3) {
        super(ptpCamera);
        this.listener = storageInfoListener;
        this.storageId = i;
        this.objectFormat = i2;
        this.associationHandle = i3;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() != Response.Ok) {
            this.listener.onImageHandlesRetrieved(new int[0]);
        } else {
            this.listener.onImageHandlesRetrieved(this.objectHandles);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        super.encodeCommand(byteBuffer, Operation.GetObjectHandles, this.storageId, this.objectFormat, this.associationHandle);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.objectHandles = PacketUtil.readU32Array(byteBuffer);
    }
}
