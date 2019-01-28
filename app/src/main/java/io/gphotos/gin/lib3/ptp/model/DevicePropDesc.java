package io.gphotos.gin.lib3.ptp.model;

import io.gphotos.gin.lib3.ptp.PacketUtil;
import java.nio.ByteBuffer;

public class DevicePropDesc {
    public int code;
    public int currentValue;
    public int datatype;
    public int[] description;
    public int factoryDefault;
    public boolean readOnly;

    public DevicePropDesc(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    public void decode(ByteBuffer byteBuffer, int i) {
        this.code = byteBuffer.getShort() & 65535;
        this.datatype = byteBuffer.getShort() & 65535;
        this.readOnly = byteBuffer.get() == (byte) 0;
        byte b;
        int i2;
        if (this.datatype == 1 || this.datatype == 2) {
            this.factoryDefault = byteBuffer.get() & 255;
            this.currentValue = byteBuffer.get() & 255;
            b = byteBuffer.get();
            if (b == (byte) 2) {
                this.description = PacketUtil.readU8Enumeration(byteBuffer);
            } else if (b == (byte) 1) {
                b = byteBuffer.get();
                byte b2 = byteBuffer.get();
                byte b3 = byteBuffer.get();
                this.description = new int[(((b2 - b) / b3) + 1)];
                for (i2 = 0; i2 < this.description.length; i2++) {
                    this.description[i2] = (b3 * i2) + b;
                }
            }
        } else if (this.datatype == 4) {
            this.factoryDefault = byteBuffer.getShort() & 65535;
            this.currentValue = byteBuffer.getShort() & 65535;
            b = byteBuffer.get();
            if (b == (byte) 2) {
                this.description = PacketUtil.readU16Enumeration(byteBuffer);
            } else if (b == (byte) 1) {
                i = byteBuffer.getShort() & 65535;
                int i3 = byteBuffer.getShort() & 65535;
                int i4 = byteBuffer.getShort() & 65535;
                this.description = new int[(((i3 - i) / i4) + 1)];
                for (i2 = 0; i2 < this.description.length; i2++) {
                    this.description[i2] = (i4 * i2) + i;
                }
            }
        } else if (this.datatype == 3) {
            this.factoryDefault = byteBuffer.getShort();
            this.currentValue = byteBuffer.getShort();
            b = byteBuffer.get();
            if (b == (byte) 2) {
                this.description = PacketUtil.readS16Enumeration(byteBuffer);
            } else if (b == (byte) 1) {
                short s = byteBuffer.getShort();
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                this.description = new int[(((s2 - s) / s3) + 1)];
                for (i2 = 0; i2 < this.description.length; i2++) {
                    this.description[i2] = (s3 * i2) + s;
                }
            }
        } else if (this.datatype == 5 || this.datatype == 6) {
            this.factoryDefault = byteBuffer.getInt();
            this.currentValue = byteBuffer.getInt();
            if (byteBuffer.get() == (byte) 2) {
                this.description = PacketUtil.readU32Enumeration(byteBuffer);
            }
        }
        if (this.description == null) {
            this.description = new int[0];
        }
    }
}
