package io.gphotos.gin.lib3.ptp.model;

import io.gphotos.gin.lib3.ptp.PacketUtil;
import java.nio.ByteBuffer;

public class StorageInfo {
    public int accessCapability;
    public int filesystemType;
    public long freeSpaceInBytes;
    public int freeSpaceInImages;
    public long maxCapacity;
    public String storageDescription;
    public int storageType;
    public String volumeLabel;

    public StorageInfo(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    private void decode(ByteBuffer byteBuffer, int i) {
        this.storageType = byteBuffer.getShort() & 65535;
        this.filesystemType = byteBuffer.getShort() & 65535;
        this.accessCapability = byteBuffer.getShort() & 255;
        this.maxCapacity = byteBuffer.getLong();
        this.freeSpaceInBytes = byteBuffer.getLong();
        this.freeSpaceInImages = byteBuffer.getInt();
        this.storageDescription = PacketUtil.readString(byteBuffer);
        this.volumeLabel = PacketUtil.readString(byteBuffer);
    }
}
