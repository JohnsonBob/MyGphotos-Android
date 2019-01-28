package io.gphotos.gin.lib3.ptp.commands;

import android.util.Log;
import io.gphotos.gin.lib3.ptp.PtpAction;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import java.nio.ByteBuffer;

public abstract class Command implements PtpAction {
    private static final String TAG = "Command";
    protected final PtpCamera camera;
    protected boolean hasDataToSend;
    private boolean hasResponseReceived;
    protected int responseCode;

    protected void decodeResponse(ByteBuffer byteBuffer, int i) {
    }

    public abstract void encodeCommand(ByteBuffer byteBuffer);

    public void encodeData(ByteBuffer byteBuffer) {
    }

    public abstract void exec(IO io);

    public Command(PtpCamera ptpCamera) {
        this.camera = ptpCamera;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        Log.w(TAG, "Received data packet but handler not implemented");
    }

    public boolean hasDataToSend() {
        return this.hasDataToSend;
    }

    public boolean hasResponseReceived() {
        return this.hasResponseReceived;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void receivedRead(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int i2 = byteBuffer.getShort() & 65535;
        int i3 = 65535 & byteBuffer.getShort();
        int i4 = byteBuffer.getInt();
        Log.i(TAG, String.format("Received %s packet for %s, length %d, code %s, tx %d", new Object[]{PtpConstants.typeToString(i2), getClass().getSimpleName(), Integer.valueOf(i), PtpConstants.codeToString(i2, i3), Integer.valueOf(i4)}));
        if (i2 == 2) {
            decodeData(byteBuffer, i);
        } else if (i2 == 3) {
            this.hasResponseReceived = true;
            this.responseCode = i3;
            decodeResponse(byteBuffer, i);
        } else {
            this.hasResponseReceived = true;
        }
    }

    public void reset() {
        this.responseCode = 0;
        this.hasResponseReceived = false;
    }

    protected void encodeCommand(ByteBuffer byteBuffer, int i) {
        byteBuffer.putInt(12);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
    }

    protected void encodeCommand(ByteBuffer byteBuffer, int i, int i2) {
        byteBuffer.putInt(16);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
        byteBuffer.putInt(i2);
    }

    protected void encodeCommand(ByteBuffer byteBuffer, int i, int i2, int i3) {
        byteBuffer.putInt(20);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
        byteBuffer.putInt(i2);
        byteBuffer.putInt(i3);
    }

    protected void encodeCommand(ByteBuffer byteBuffer, int i, int i2, int i3, int i4) {
        byteBuffer.putInt(24);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
        byteBuffer.putInt(i2);
        byteBuffer.putInt(i3);
        byteBuffer.putInt(i4);
    }
}
