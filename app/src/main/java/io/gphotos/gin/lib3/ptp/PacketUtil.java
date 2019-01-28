package io.gphotos.gin.lib3.ptp;

import android.util.Log;
import java.nio.ByteBuffer;

public class PacketUtil {
    public static int[] readU32Array(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getInt();
        }
        return iArr;
    }

    public static int[] readU16Array(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getShort() & 65535;
        }
        return iArr;
    }

    public static void writeU16Array(ByteBuffer byteBuffer, int[] iArr) {
        byteBuffer.putInt(iArr.length);
        for (int i : iArr) {
            byteBuffer.putShort((short) i);
        }
    }

    public static int[] readU8Array(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.get() & 255;
        }
        return iArr;
    }

    public static int[] readU32Enumeration(ByteBuffer byteBuffer) {
        int i = byteBuffer.getShort() & 65535;
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getInt();
        }
        return iArr;
    }

    public static int[] readS16Enumeration(ByteBuffer byteBuffer) {
        int i = byteBuffer.getShort() & 65535;
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getShort();
        }
        return iArr;
    }

    public static int[] readU16Enumeration(ByteBuffer byteBuffer) {
        int i = byteBuffer.getShort() & 65535;
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getShort() & 65535;
        }
        return iArr;
    }

    public static int[] readU8Enumeration(ByteBuffer byteBuffer) {
        int i = byteBuffer.getShort() & 65535;
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.get() & 255;
        }
        return iArr;
    }

    public static String readString(ByteBuffer byteBuffer) {
        int i = byteBuffer.get() & 255;
        if (i <= 0) {
            return "";
        }
        i--;
        char[] cArr = new char[i];
        for (int i2 = 0; i2 < i; i2++) {
            cArr[i2] = byteBuffer.getChar();
        }
        byteBuffer.getChar();
        return String.copyValueOf(cArr);
    }

    public static void writeString(ByteBuffer byteBuffer, String str) {
        byteBuffer.put((byte) str.length());
        if (str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                byteBuffer.putShort((short) str.charAt(i));
            }
            byteBuffer.putShort((short) 0);
        }
    }

    public static String hexDumpToString(byte[] bArr, int i, int i2) {
        int i3 = i2 / 16;
        int i4 = i2 % 16;
        StringBuilder stringBuilder = new StringBuilder((i3 + 1) * 97);
        int i5 = 0;
        int i6 = 0;
        while (true) {
            int i7 = 1;
            if (i6 >= i3) {
                break;
            }
            Object[] objArr = new Object[1];
            int i8 = i6 * 16;
            objArr[0] = Integer.valueOf(i8);
            stringBuilder.append(String.format("%04x ", objArr));
            int i9 = 0;
            while (i9 < 16) {
                Object[] objArr2 = new Object[i7];
                objArr2[0] = Byte.valueOf(bArr[(i + i8) + i9]);
                stringBuilder.append(String.format("%02x ", objArr2));
                i9++;
                i7 = 1;
            }
            for (int i10 = 0; i10 < 16; i10++) {
                char c = (char) bArr[(i + i8) + i10];
                if (c < ' ' || c > '~') {
                    c = '.';
                }
                stringBuilder.append(c);
            }
            stringBuilder.append(10);
            i6++;
        }
        if (i4 != 0) {
            Object[] objArr3 = new Object[1];
            i3 *= 16;
            objArr3[0] = Integer.valueOf(i3);
            stringBuilder.append(String.format("%04x ", objArr3));
            for (i6 = 0; i6 < i4; i6++) {
                stringBuilder.append(String.format("%02x ", new Object[]{Byte.valueOf(bArr[(i + i3) + i6])}));
            }
            for (i6 = 0; i6 < (16 - i4) * 3; i6++) {
                stringBuilder.append(' ');
            }
            while (i5 < i4) {
                char c2 = (char) bArr[(i + i3) + i5];
                if (c2 < ' ' || c2 > '~') {
                    c2 = '.';
                }
                stringBuilder.append(c2);
                i5++;
            }
            stringBuilder.append(10);
        }
        return stringBuilder.toString();
    }

    public static void logHexdump(String str, byte[] bArr, int i, int i2) {
        Log.i(str, hexDumpToString(bArr, i, i2));
    }

    public static void logHexdump(String str, byte[] bArr, int i) {
        logHexdump(str, bArr, 0, i);
    }
}
