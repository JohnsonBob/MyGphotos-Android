package io.gphotos.gin.lib3.ptp.model;

import io.gphotos.gin.lib3.ptp.PacketUtil;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import java.nio.ByteBuffer;

public class ObjectInfo {
    public int associationDesc;
    public int associationType;
    public String captureDate;
    public String filename;
    public int imageBitDepth;
    public int imagePixHeight;
    public int imagePixWidth;
    public int keywords;
    public String modificationDate;
    public int objectCompressedSize;
    public int objectFormat;
    public int parentObject;
    public int protectionStatus;
    public int sequenceNumber;
    public int storageId;
    public int thumbCompressedSize;
    public int thumbFormat;
    public int thumbPixHeight;
    public int thumbPixWidth;

    public ObjectInfo(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    public void decode(ByteBuffer byteBuffer, int i) {
        this.storageId = byteBuffer.getInt();
        this.objectFormat = byteBuffer.getShort();
        this.protectionStatus = byteBuffer.getShort();
        this.objectCompressedSize = byteBuffer.getInt();
        this.thumbFormat = byteBuffer.getShort();
        this.thumbCompressedSize = byteBuffer.getInt();
        this.thumbPixWidth = byteBuffer.getInt();
        this.thumbPixHeight = byteBuffer.getInt();
        this.imagePixWidth = byteBuffer.getInt();
        this.imagePixHeight = byteBuffer.getInt();
        this.imageBitDepth = byteBuffer.getInt();
        this.parentObject = byteBuffer.getInt();
        this.associationType = byteBuffer.getShort();
        this.associationDesc = byteBuffer.getInt();
        this.sequenceNumber = byteBuffer.getInt();
        this.filename = PacketUtil.readString(byteBuffer);
        this.captureDate = PacketUtil.readString(byteBuffer);
        this.modificationDate = PacketUtil.readString(byteBuffer);
        this.keywords = byteBuffer.get();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ObjectInfo\n");
        stringBuilder.append("StorageId: ");
        stringBuilder.append(String.format("0x%08x\n", new Object[]{Integer.valueOf(this.storageId)}));
        stringBuilder.append("ObjectFormat: ");
        stringBuilder.append(PtpConstants.objectFormatToString(this.objectFormat));
        stringBuilder.append(10);
        stringBuilder.append("ProtectionStatus: ");
        stringBuilder.append(this.protectionStatus);
        stringBuilder.append(10);
        stringBuilder.append("ObjectCompressedSize: ");
        stringBuilder.append(this.objectCompressedSize);
        stringBuilder.append(10);
        stringBuilder.append("ThumbFormat: ");
        stringBuilder.append(PtpConstants.objectFormatToString(this.thumbFormat));
        stringBuilder.append(10);
        stringBuilder.append("ThumbCompressedSize: ");
        stringBuilder.append(this.thumbCompressedSize);
        stringBuilder.append(10);
        stringBuilder.append("ThumbPixWdith: ");
        stringBuilder.append(this.thumbPixWidth);
        stringBuilder.append(10);
        stringBuilder.append("ThumbPixHeight: ");
        stringBuilder.append(this.thumbPixHeight);
        stringBuilder.append(10);
        stringBuilder.append("ImagePixWidth: ");
        stringBuilder.append(this.imagePixWidth);
        stringBuilder.append(10);
        stringBuilder.append("ImagePixHeight: ");
        stringBuilder.append(this.imagePixHeight);
        stringBuilder.append(10);
        stringBuilder.append("ImageBitDepth: ");
        stringBuilder.append(this.imageBitDepth);
        stringBuilder.append(10);
        stringBuilder.append("ParentObject: ");
        stringBuilder.append(String.format("0x%08x", new Object[]{Integer.valueOf(this.parentObject)}));
        stringBuilder.append(10);
        stringBuilder.append("AssociationType: ");
        stringBuilder.append(this.associationType);
        stringBuilder.append(10);
        stringBuilder.append("AssociatonDesc: ");
        stringBuilder.append(this.associationDesc);
        stringBuilder.append(10);
        stringBuilder.append("Filename: ");
        stringBuilder.append(this.filename);
        stringBuilder.append(10);
        stringBuilder.append("CaptureDate: ");
        stringBuilder.append(this.captureDate);
        stringBuilder.append(10);
        stringBuilder.append("ModificationDate: ");
        stringBuilder.append(this.modificationDate);
        stringBuilder.append(10);
        stringBuilder.append("Keywords: ");
        stringBuilder.append(this.keywords);
        stringBuilder.append(10);
        return stringBuilder.toString();
    }
}
