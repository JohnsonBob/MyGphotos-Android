package io.gphotos.gin.lib3.ptp.commands.eos;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EosGetLiveViewPictureCommand extends EosCommand {
    private static final String TAG = "EosGetLiveViewPictureCommand";
    private static byte[] tmpStorage = new byte[16384];
    private LiveViewData data;
    private final Options options;

    public EosGetLiveViewPictureCommand(EosCamera eosCamera, LiveViewData liveViewData) {
        super(eosCamera);
        if (liveViewData == null) {
            this.data = new LiveViewData();
            this.data.histogram = ByteBuffer.allocate(4096);
            this.data.histogram.order(ByteOrder.LITTLE_ENDIAN);
        } else {
            this.data = liveViewData;
        }
        this.options = new Options();
        this.options.inBitmap = this.data.bitmap;
        this.options.inSampleSize = 1;
        this.options.inTempStorage = tmpStorage;
        this.data.bitmap = null;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
            return;
        }
        if (this.data.bitmap == null || this.responseCode != Response.Ok) {
            this.camera.onLiveViewReceived(null);
        } else {
            this.camera.onLiveViewReceived(this.data);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosGetLiveViewPicture, 1048576);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.data.hasHistogram = false;
        this.data.hasAfFrame = false;
        if (i < 1000) {
            Log.w(TAG, String.format("liveview data size too small %d", new Object[]{Integer.valueOf(i)}));
            return;
        }
        do {
            try {
                if (byteBuffer.hasRemaining()) {
                    int i2 = byteBuffer.getInt();
                    int i3 = byteBuffer.getInt();
                    if (i2 < 8) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid sub size ");
                        stringBuilder.append(i2);
                        throw new RuntimeException(stringBuilder.toString());
                    } else if (i3 != 1) {
                        String str;
                        StringBuilder stringBuilder2;
                        String str2;
                        StringBuilder stringBuilder3;
                        switch (i3) {
                            case 3:
                                this.data.hasHistogram = true;
                                byteBuffer.get(this.data.histogram.array(), 0, 4096);
                                break;
                            case 4:
                                this.data.zoomFactor = byteBuffer.getInt();
                                break;
                            case 5:
                                this.data.zoomRectRight = byteBuffer.getInt();
                                this.data.zoomRectBottom = byteBuffer.getInt();
                                str = TAG;
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("header 5 ");
                                stringBuilder2.append(this.data.zoomRectRight);
                                stringBuilder2.append(" ");
                                stringBuilder2.append(this.data.zoomRectBottom);
                                Log.i(str, stringBuilder2.toString());
                                break;
                            case 6:
                                this.data.zoomRectLeft = byteBuffer.getInt();
                                this.data.zoomRectTop = byteBuffer.getInt();
                                str = TAG;
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("header 6 ");
                                stringBuilder2.append(this.data.zoomRectLeft);
                                stringBuilder2.append(" ");
                                stringBuilder2.append(this.data.zoomRectTop);
                                Log.i(str, stringBuilder2.toString());
                                break;
                            case 7:
                                i3 = byteBuffer.getInt();
                                str2 = TAG;
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("header 7 ");
                                stringBuilder3.append(i3);
                                stringBuilder3.append(" ");
                                stringBuilder3.append(i2);
                                Log.i(str2, stringBuilder3.toString());
                                break;
                            default:
                                byteBuffer.position((byteBuffer.position() + i2) - 8);
                                str2 = TAG;
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("unknown header ");
                                stringBuilder3.append(i3);
                                stringBuilder3.append(" size ");
                                stringBuilder3.append(i2);
                                Log.i(str2, stringBuilder3.toString());
                                break;
                        }
                    } else {
                        this.data.bitmap = BitmapFactory.decodeByteArray(byteBuffer.array(), byteBuffer.position(), i2 - 8, this.options);
                        byteBuffer.position((byteBuffer.position() + i2) - 8);
                    }
                }
            } catch (RuntimeException e) {
                String str3 = TAG;
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("");
                stringBuilder4.append(e.toString());
                Log.e(str3, stringBuilder4.toString());
                str3 = TAG;
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append("");
                stringBuilder4.append(e.getLocalizedMessage());
                Log.e(str3, stringBuilder4.toString());
            }
        } while (i - byteBuffer.position() >= 8);
    }
}
