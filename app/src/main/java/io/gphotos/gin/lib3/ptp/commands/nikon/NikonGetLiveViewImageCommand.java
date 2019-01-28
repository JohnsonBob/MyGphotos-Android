package io.gphotos.gin.lib3.ptp.commands.nikon;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import io.gphotos.gin.lib3.ptp.NikonCamera;
import io.gphotos.gin.lib3.ptp.PacketUtil;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Product;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NikonGetLiveViewImageCommand extends NikonCommand {
    private static final String TAG = "NikonGetLiveViewImageCommand";
    private static boolean haveAddedDumpToAcra = false;
    private static byte[] tmpStorage = new byte[16384];
    private LiveViewData data;
    private final Options options;

    public NikonGetLiveViewImageCommand(NikonCamera nikonCamera, LiveViewData liveViewData) {
        super(nikonCamera);
        this.data = liveViewData;
        if (liveViewData == null) {
            this.data = new LiveViewData();
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
        if (this.camera.isLiveViewOpen()) {
            io.handleCommand(this);
            if (this.responseCode == Response.DeviceBusy) {
                this.camera.onDeviceBusy(this, true);
                return;
            }
            this.data.hasHistogram = false;
            if (this.data.bitmap == null || this.responseCode != Response.Ok) {
                this.camera.onLiveViewReceived(null);
            } else {
                this.camera.onLiveViewReceived(this.data);
            }
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonGetLiveViewImage);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        if (i > 128) {
            this.data.hasAfFrame = false;
            int productId = this.camera.getProductId();
            int position = byteBuffer.position();
            switch (productId) {
                case 1050:
                case 1052:
                case Product.NikonD3X /*1056*/:
                case 1058:
                case 1061:
                    productId = 64;
                    break;
                case 1057:
                case 1059:
                case 1062:
                    productId = 128;
                    break;
                case 1064:
                case 1065:
                    productId = 384;
                    break;
                default:
                    return;
            }
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            this.data.hasAfFrame = true;
            int i2 = byteBuffer.getShort() & 65535;
            int i3 = byteBuffer.getShort() & 65535;
            float f = ((float) (byteBuffer.getShort() & 65535)) / ((float) i2);
            float f2 = ((float) (byteBuffer.getShort() & 65535)) / ((float) i3);
            byteBuffer.position(position + 16);
            this.data.nikonWholeWidth = i2;
            this.data.nikonWholeHeight = i3;
            this.data.nikonAfFrameWidth = (int) (((float) (byteBuffer.getShort() & 65535)) * f);
            this.data.nikonAfFrameHeight = (int) (((float) (byteBuffer.getShort() & 65535)) * f2);
            this.data.nikonAfFrameCenterX = (int) (((float) (byteBuffer.getShort() & 65535)) * f);
            this.data.nikonAfFrameCenterY = (int) (((float) (65535 & byteBuffer.getShort())) * f2);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.position(productId + position);
            if (byteBuffer.remaining() <= 128) {
                this.data.bitmap = null;
                return;
            }
            try {
                this.data.bitmap = BitmapFactory.decodeByteArray(byteBuffer.array(), byteBuffer.position(), i - byteBuffer.position(), this.options);
            } catch (RuntimeException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("decoding failed ");
                stringBuilder.append(e.toString());
                Log.e(str, stringBuilder.toString());
                Log.e(TAG, e.getLocalizedMessage());
                PacketUtil.logHexdump(TAG, byteBuffer.array(), position, 512);
            }
        }
    }
}
