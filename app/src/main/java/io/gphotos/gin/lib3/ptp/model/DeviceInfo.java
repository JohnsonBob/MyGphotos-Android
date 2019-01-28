package io.gphotos.gin.lib3.ptp.model;

import io.gphotos.gin.lib3.ptp.PacketUtil;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Event;
import io.gphotos.gin.lib3.ptp.PtpConstants.ObjectFormat;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Property;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DeviceInfo {
    public int[] captureFormats;
    public int[] devicePropertiesSupported;
    public String deviceVersion;
    public int[] eventsSupported;
    public short functionalMode;
    public int[] imageFormats;
    public String manufacture;
    public String model;
    public int[] operationsSupported;
    public String serialNumber;
    public short standardVersion;
    public String vendorExtensionDesc;
    public int vendorExtensionId;
    public short vendorExtensionVersion;

    public DeviceInfo(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    public void decode(ByteBuffer byteBuffer, int i) {
        this.standardVersion = byteBuffer.getShort();
        this.vendorExtensionId = byteBuffer.getInt();
        this.vendorExtensionVersion = byteBuffer.getShort();
        this.vendorExtensionDesc = PacketUtil.readString(byteBuffer);
        this.functionalMode = byteBuffer.getShort();
        this.operationsSupported = PacketUtil.readU16Array(byteBuffer);
        this.eventsSupported = PacketUtil.readU16Array(byteBuffer);
        this.devicePropertiesSupported = PacketUtil.readU16Array(byteBuffer);
        this.captureFormats = PacketUtil.readU16Array(byteBuffer);
        this.imageFormats = PacketUtil.readU16Array(byteBuffer);
        this.manufacture = PacketUtil.readString(byteBuffer);
        this.model = PacketUtil.readString(byteBuffer);
        this.deviceVersion = PacketUtil.readString(byteBuffer);
        this.serialNumber = PacketUtil.readString(byteBuffer);
    }

    public void encode(ByteBuffer byteBuffer) {
        byteBuffer.putShort(this.standardVersion);
        byteBuffer.putInt(this.vendorExtensionId);
        byteBuffer.putInt(this.vendorExtensionVersion);
        PacketUtil.writeString(byteBuffer, "");
        byteBuffer.putShort(this.functionalMode);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeString(byteBuffer, "");
        PacketUtil.writeString(byteBuffer, "");
        PacketUtil.writeString(byteBuffer, "");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DeviceInfo\n");
        stringBuilder.append("StandardVersion: ");
        stringBuilder.append(this.standardVersion);
        stringBuilder.append(10);
        stringBuilder.append("VendorExtensionId: ");
        stringBuilder.append(this.vendorExtensionId);
        stringBuilder.append(10);
        stringBuilder.append("VendorExtensionVersion: ");
        stringBuilder.append(this.vendorExtensionVersion);
        stringBuilder.append(10);
        stringBuilder.append("VendorExtensionDesc: ");
        stringBuilder.append(this.vendorExtensionDesc);
        stringBuilder.append(10);
        stringBuilder.append("FunctionalMode: ");
        stringBuilder.append(this.functionalMode);
        stringBuilder.append(10);
        appendU16Array(stringBuilder, "OperationsSupported", Operation.class, this.operationsSupported);
        appendU16Array(stringBuilder, "EventsSupported", Event.class, this.eventsSupported);
        appendU16Array(stringBuilder, "DevicePropertiesSupported", Property.class, this.devicePropertiesSupported);
        appendU16Array(stringBuilder, "CaptureFormats", ObjectFormat.class, this.captureFormats);
        appendU16Array(stringBuilder, "ImageFormats", ObjectFormat.class, this.imageFormats);
        stringBuilder.append("Manufacture: ");
        stringBuilder.append(this.manufacture);
        stringBuilder.append(10);
        stringBuilder.append("Model: ");
        stringBuilder.append(this.model);
        stringBuilder.append(10);
        stringBuilder.append("DeviceVersion: ");
        stringBuilder.append(this.deviceVersion);
        stringBuilder.append(10);
        stringBuilder.append("SerialNumber: ");
        stringBuilder.append(this.serialNumber);
        stringBuilder.append(10);
        return stringBuilder.toString();
    }

    private static void appendU16Array(StringBuilder stringBuilder, String str, Class<?> cls, int[] iArr) {
        Arrays.sort(iArr);
        stringBuilder.append(str);
        stringBuilder.append(":\n");
        for (int constantToString : iArr) {
            stringBuilder.append("    ");
            stringBuilder.append(PtpConstants.constantToString(cls, constantToString));
            stringBuilder.append(10);
        }
    }
}
