package io.gphotos.gin.lib3.ptp.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class GetObjectCommand extends Command {
    private static final String TAG = "GetObjectCommand";
    private byte[] byteBuffer;
    private Bitmap inBitmap;
    private final int objectHandle;
    private final Options options = new Options();
    private boolean outOfMemoryError;

    public GetObjectCommand(PtpCamera ptpCamera, int i, int i2) {
        super(ptpCamera);
        this.objectHandle = i;
        if (i2 < 1 || i2 > 4) {
            this.options.inSampleSize = 2;
        } else {
            this.options.inSampleSize = i2;
        }
    }

    public Bitmap getBitmap() {
        return this.inBitmap;
    }

    public byte[] getBytes() {
        return this.byteBuffer;
    }

    public boolean isOutOfMemoryError() {
        return this.outOfMemoryError;
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        super.reset();
        this.inBitmap = null;
        this.outOfMemoryError = false;
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetObject, this.objectHandle);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        try {
            this.byteBuffer = Arrays.copyOfRange(byteBuffer.array(), 12, i);
        } catch (RuntimeException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("exception on decoding picture : ");
            stringBuilder.append(e.toString());
            Log.i(str, stringBuilder.toString());
        } catch (OutOfMemoryError unused) {
            System.gc();
            this.outOfMemoryError = true;
        }
    }
}
