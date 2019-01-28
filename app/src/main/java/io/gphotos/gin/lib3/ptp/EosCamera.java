package io.gphotos.gin.lib3.ptp;

import io.gphotos.gin.lib3.ptp.Camera.CameraListener;
import io.gphotos.gin.lib3.ptp.Camera.WorkerListener;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Property;
import io.gphotos.gin.lib3.ptp.commands.SimpleCommand;
import io.gphotos.gin.lib3.ptp.commands.eos.EosEventCheckCommand;
import io.gphotos.gin.lib3.ptp.commands.eos.EosGetLiveViewPictureCommand;
import io.gphotos.gin.lib3.ptp.commands.eos.EosOpenSessionAction;
import io.gphotos.gin.lib3.ptp.commands.eos.EosSetLiveViewAction;
import io.gphotos.gin.lib3.ptp.commands.eos.EosSetPropertyCommand;
import io.gphotos.gin.lib3.ptp.commands.eos.EosTakePictureCommand;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EosCamera extends PtpCamera {
    public void focus() {
    }

    public void setLiveViewAfArea(float f, float f2) {
    }

    public EosCamera(PtpUsbConnection ptpUsbConnection, CameraListener cameraListener, WorkerListener workerListener) {
        super(ptpUsbConnection, cameraListener, workerListener);
        addPropertyMapping(1, PtpConstants.Property.EosShutterSpeed);
        addPropertyMapping(2, PtpConstants.Property.EosApertureValue);
        addPropertyMapping(3, PtpConstants.Property.EosIsoSpeed);
        addPropertyMapping(4, PtpConstants.Property.EosWhitebalance);
        addPropertyMapping(5, PtpConstants.Property.EosShootingMode);
        addPropertyMapping(7, PtpConstants.Property.EosAvailableShots);
        addPropertyMapping(8, PtpConstants.Property.EosColorTemperature);
        addPropertyMapping(9, PtpConstants.Property.EosAfMode);
        addPropertyMapping(10, PtpConstants.Property.EosPictureStyle);
        addPropertyMapping(11, PtpConstants.Property.EosMeteringMode);
        addPropertyMapping(16, PtpConstants.Property.EosExposureCompensation);
        this.histogramSupported = true;
    }

    protected void onOperationCodesReceived(Set<Integer> set) {
        if (set.contains(Integer.valueOf(Operation.EosGetLiveViewPicture))) {
            this.liveViewSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.EosBulbStart)) && set.contains(Integer.valueOf(Operation.EosBulbEnd))) {
            this.bulbSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.EosDriveLens))) {
            this.driveLensSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.EosRemoteReleaseOn))) {
            set.contains(Integer.valueOf(Operation.EosRemoteReleaseOff));
        }
    }

    public void onEventDirItemCreated(int i, int i2, int i3, String str) {
        onEventObjectAdded(i, i3);
    }

    protected void openSession() {
        this.queue.add(new EosOpenSessionAction(this));
    }

    protected void queueEventCheck() {
        this.queue.add(new EosEventCheckCommand(this));
    }

    public void capture() {
        if (isBulbCurrentShutterSpeed()) {
            this.queue.add(new SimpleCommand(this, this.cameraIsCapturing ? Operation.EosBulbEnd : Operation.EosBulbStart));
        } else {
            this.queue.add(new EosTakePictureCommand(this));
        }
    }

    public void setProperty(int i, int i2) {
        if (this.properties.containsKey(Integer.valueOf(i))) {
            this.queue.add(new EosSetPropertyCommand(this, ((Integer) this.virtualToPtpProperty.get(Integer.valueOf(i))).intValue(), i2));
        }
    }

    public void setLiveView(boolean z) {
        if (this.liveViewSupported) {
            this.queue.add(new EosSetLiveViewAction(this, z));
        }
    }

    public void getLiveViewPicture(LiveViewData liveViewData) {
        if (this.liveViewOpen) {
            this.queue.add(new EosGetLiveViewPictureCommand(this, liveViewData));
        }
    }

    protected boolean isBulbCurrentShutterSpeed() {
        Integer num = (Integer) this.ptpProperties.get(Integer.valueOf(PtpConstants.Property.EosShutterSpeed));
        return this.bulbSupported && num != null && num.intValue() == 12;
    }

    public void driveLens(int i, int i2) {
        if (this.driveLensSupported && this.liveViewOpen) {
            i = i == 1 ? 0 : 32768;
            switch (i2) {
                case 2:
                    i |= 2;
                    break;
                case 3:
                    i |= 3;
                    break;
                default:
                    i |= 1;
                    break;
            }
            this.queue.add(new SimpleCommand(this, Operation.EosDriveLens, i));
        }
    }

    public boolean isSettingPropertyPossible(int i) {
        Integer num = (Integer) this.ptpProperties.get(Integer.valueOf(PtpConstants.Property.EosShootingMode));
        Integer num2 = (Integer) this.ptpProperties.get(Integer.valueOf(PtpConstants.Property.WhiteBalance));
        boolean z = false;
        if (num == null) {
            return false;
        }
        if (i != 8) {
            if (i != 11) {
                switch (i) {
                    case 1:
                        if (num.intValue() == 3 || num.intValue() == 1) {
                            z = true;
                        }
                        return z;
                    case 2:
                        if (num.intValue() == 3 || num.intValue() == 2) {
                            z = true;
                        }
                        return z;
                    case 3:
                    case 4:
                        break;
                    default:
                        switch (i) {
                            case 15:
                                return false;
                            case 16:
                                if (num.intValue() == 0 || num.intValue() == 1 || num.intValue() == 2 || num.intValue() == 5 || num.intValue() == 6) {
                                    z = true;
                                }
                                return z;
                            default:
                                return true;
                        }
                }
            }
            if (num.intValue() >= 0 && num.intValue() <= 6) {
                z = true;
            }
            return z;
        }
        if (num2 != null && num2.intValue() == 9) {
            z = true;
        }
        return z;
    }

    public List<FocusPoint> getFocusPoints() {
        return new ArrayList();
    }
}
