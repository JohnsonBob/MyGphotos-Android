package io.gphotos.gin.lib3.ptp.commands.eos;

import android.util.Log;
import io.gphotos.gin.lib3.ptp.EosCamera;
import io.gphotos.gin.lib3.ptp.PacketUtil;
import io.gphotos.gin.lib3.ptp.PtpCamera.IO;
import io.gphotos.gin.lib3.ptp.PtpConstants;
import io.gphotos.gin.lib3.ptp.PtpConstants.Event;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Property;
import io.gphotos.gin.lib3.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosEventCheckCommand extends EosCommand {
    private static final String TAG = "EosEventCheckCommand";

    public EosEventCheckCommand(EosCamera eosCamera) {
        super(eosCamera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, false);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosEventCheck);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        while (byteBuffer.position() < i) {
            int i2 = byteBuffer.getInt();
            int i3 = byteBuffer.getInt();
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("event length ");
            stringBuilder.append(i2);
            Log.i(str, stringBuilder.toString());
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("event type ");
            stringBuilder.append(PtpConstants.eventToString(i3));
            Log.i(str, stringBuilder.toString());
            int i4 = 0;
            int i5;
            switch (i3) {
                case Event.EosObjectAdded /*49537*/:
                case Event.EosObjectAdded5D4 /*49575*/:
                    i3 = byteBuffer.getInt();
                    i5 = byteBuffer.getInt();
                    short s = byteBuffer.getShort();
                    skip(byteBuffer, i2 - 18);
                    this.camera.onEventDirItemCreated(i3, i5, s, "TODO");
                    break;
                case Event.EosDevicePropChanged /*49545*/:
                    i3 = byteBuffer.getInt();
                    String str2 = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("property ");
                    stringBuilder2.append(PtpConstants.propertyToString(i3));
                    Log.i(str2, stringBuilder2.toString());
                    if (!(i3 == Property.EosPictureStyle || i3 == Property.EosAvailableShots || i3 == Property.EosEvfOutputDevice || i3 == Property.EosEvfMode)) {
                        switch (i3) {
                            case Property.EosApertureValue /*53505*/:
                            case Property.EosShutterSpeed /*53506*/:
                            case Property.EosIsoSpeed /*53507*/:
                            case Property.EosExposureCompensation /*53508*/:
                            case Property.EosShootingMode /*53509*/:
                                break;
                            default:
                                switch (i3) {
                                    case Property.EosMeteringMode /*53511*/:
                                    case Property.EosAfMode:
                                    case Property.EosWhitebalance /*53513*/:
                                    case Property.EosColorTemperature /*53514*/:
                                        break;
                                    default:
                                        if (i2 <= 200) {
                                            PacketUtil.logHexdump(TAG, byteBuffer.array(), byteBuffer.position(), i2 - 12);
                                        }
                                        skip(byteBuffer, i2 - 12);
                                        continue;
                                }
                        }
                    }
                    i2 = byteBuffer.getInt();
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("value ");
                    stringBuilder.append(i2);
                    Log.i(str, stringBuilder.toString());
                    this.camera.onPropertyChanged(i3, i2);
                    break;
                case Event.EosDevicePropDescChanged /*49546*/:
                    i3 = byteBuffer.getInt();
                    String str3 = TAG;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("property ");
                    stringBuilder3.append(PtpConstants.propertyToString(i3));
                    Log.i(str3, stringBuilder3.toString());
                    if (!(i3 == Property.EosMeteringMode || i3 == Property.EosPictureStyle)) {
                        switch (i3) {
                            case Property.EosApertureValue /*53505*/:
                            case Property.EosShutterSpeed /*53506*/:
                            case Property.EosIsoSpeed /*53507*/:
                            case Property.EosExposureCompensation /*53508*/:
                                break;
                            default:
                                switch (i3) {
                                    case Property.EosWhitebalance /*53513*/:
                                    case Property.EosColorTemperature /*53514*/:
                                        break;
                                    default:
                                        if (i2 <= 50) {
                                            PacketUtil.logHexdump(TAG, byteBuffer.array(), byteBuffer.position(), i2 - 12);
                                        }
                                        skip(byteBuffer, i2 - 12);
                                        continue;
                                }
                        }
                    }
                    byteBuffer.getInt();
                    i5 = byteBuffer.getInt();
                    str3 = TAG;
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("property desc with num ");
                    stringBuilder3.append(i5);
                    Log.i(str3, stringBuilder3.toString());
                    int i6 = (i5 * 4) + 20;
                    if (i2 != i6) {
                        Log.i(TAG, String.format("Event Desc length invalid should be %d but is %d", new Object[]{Integer.valueOf(i6), Integer.valueOf(i2)}));
                        PacketUtil.logHexdump(TAG, byteBuffer.array(), byteBuffer.position() - 20, i2);
                    }
                    int[] iArr = new int[i5];
                    while (i4 < i5) {
                        iArr[i4] = byteBuffer.getInt();
                        i4++;
                    }
                    if (i2 != i6) {
                        while (i6 < i2) {
                            byteBuffer.get();
                            i6++;
                        }
                    }
                    this.camera.onPropertyDescChanged(i3, iArr);
                    break;
                case Event.EosCameraStatus /*49547*/:
                    boolean z = false;
                    i2 = byteBuffer.getInt();
                    EosCamera eosCamera = this.camera;
                    if (i2 != 0) {
                        z = true;
                    }
                    eosCamera.onEventCameraCapture(z);
                    break;
                case Event.EosWillSoonShutdown /*49549*/:
                    byteBuffer.getInt();
                    break;
                case Event.EosBulbExposureTime /*49556*/:
                    this.camera.onBulbExposureTime(byteBuffer.getInt());
                    break;
                default:
                    skip(byteBuffer, i2 - 8);
                    break;
            }
        }
    }

    private void skip(ByteBuffer byteBuffer, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            byteBuffer.get();
        }
    }
}
