package io.gphotos.gin.lib3.ptp.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import io.gphotos.gin.lib3.ptp.PtpCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class GetThumb extends Command {
    private static final String TAG = "GetThumb";
    private Bitmap inBitmap;
    private final int objectHandle;

    public GetThumb(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.objectHandle = i;
    }

    public Bitmap getBitmap() {
        return this.inBitmap;
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        super.reset();
        this.inBitmap = null;
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetThumb, this.objectHandle);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        try {
            this.inBitmap = BitmapFactory.decodeByteArray(byteBuffer.array(), 12, i - 12);
        } catch (RuntimeException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("exception on decoding picture : ");
            stringBuilder.append(e.toString());
            Log.i(str, stringBuilder.toString());
        } catch (OutOfMemoryError unused) {
            System.gc();
        }
    }
}
