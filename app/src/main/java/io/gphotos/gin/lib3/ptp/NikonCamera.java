package io.gphotos.gin.lib3.ptp;

import io.gphotos.gin.lib3.ptp.Camera.CameraListener;
import io.gphotos.gin.lib3.ptp.Camera.WorkerListener;
import io.gphotos.gin.lib3.ptp.PtpConstants.Operation;
import io.gphotos.gin.lib3.ptp.PtpConstants.Product;
import io.gphotos.gin.lib3.ptp.PtpConstants.Property;
import io.gphotos.gin.lib3.ptp.commands.GetDevicePropDescCommand;
import io.gphotos.gin.lib3.ptp.commands.InitiateCaptureCommand;
import io.gphotos.gin.lib3.ptp.commands.RetrieveAddedObjectInfoAction;
import io.gphotos.gin.lib3.ptp.commands.SimpleCommand;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonAfDriveCommand;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonCloseSessionAction;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonEventCheckCommand;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonGetLiveViewImageAction;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonGetLiveViewImageCommand;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonOpenSessionAction;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonStartLiveViewAction;
import io.gphotos.gin.lib3.ptp.commands.nikon.NikonStopLiveViewAction;
import io.gphotos.gin.lib3.ptp.model.DevicePropDesc;
import io.gphotos.gin.lib3.ptp.model.LiveViewData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class NikonCamera extends PtpCamera {
    private int afAreaHeight;
    private int afAreaWidth;
    private int enableAfAreaPoint;
    private boolean gotNikonShutterSpeed;
    private boolean liveViewStoppedInternal;
    private Set<Integer> supportedOperations;
    private int[] vendorPropCodes;
    private int wholeHeight;
    private int wholeWidth;

    protected boolean isBulbCurrentShutterSpeed() {
        return false;
    }

    public void onEventCaptureComplete() {
    }

    public NikonCamera(PtpUsbConnection ptpUsbConnection, CameraListener cameraListener, WorkerListener workerListener) {
        super(ptpUsbConnection, cameraListener, workerListener);
        this.vendorPropCodes = new int[0];
        this.histogramSupported = false;
    }

    protected void onOperationCodesReceived(Set<Integer> set) {
        this.supportedOperations = set;
        if (set.contains(Integer.valueOf(Operation.NikonGetLiveViewImage)) && set.contains(Integer.valueOf(Operation.NikonStartLiveView)) && set.contains(Integer.valueOf(Operation.NikonEndLiveView))) {
            this.liveViewSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.NikonMfDrive))) {
            this.driveLensSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.NikonChangeAfArea))) {
            this.liveViewAfAreaSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.NikonAfDrive))) {
            this.autoFocusSupported = true;
        }
    }

    public void onPropertyChanged(int i, int i2) {
        super.onPropertyChanged(i, i2);
        if (i == PtpConstants.Property.NikonEnableAfAreaPoint) {
            this.enableAfAreaPoint = i2;
            this.handler.post(new Runnable() {
                public void run() {
                    if (NikonCamera.this.listener != null) {
                        NikonCamera.this.listener.onFocusPointsChanged();
                    }
                }
            });
        }
    }

    public void onPropertyDescChanged(int i, DevicePropDesc devicePropDesc) {
        if (!this.gotNikonShutterSpeed) {
            if (i == PtpConstants.Property.NikonShutterSpeed) {
                if (devicePropDesc.description.length > 4) {
                    addPropertyMapping(1, PtpConstants.Property.NikonShutterSpeed);
                    this.gotNikonShutterSpeed = true;
                } else {
                    return;
                }
            } else if (i == PtpConstants.Property.ExposureTime) {
                addPropertyMapping(1, PtpConstants.Property.ExposureTime);
                this.gotNikonShutterSpeed = true;
            }
        }
        super.onPropertyDescChanged(i, devicePropDesc);
    }

    private void onPropertyCodesReceived(Set<Integer> set) {
        if (set.contains(Integer.valueOf(PtpConstants.Property.NikonShutterSpeed))) {
            this.queue.add(new GetDevicePropDescCommand(this, PtpConstants.Property.NikonShutterSpeed));
        }
        if (set.contains(Integer.valueOf(PtpConstants.Property.ExposureTime))) {
            this.queue.add(new GetDevicePropDescCommand(this, PtpConstants.Property.ExposureTime));
        }
        addPropertyMapping(2, PtpConstants.Property.FNumber);
        addPropertyMapping(3, PtpConstants.Property.ExposureIndex);
        addPropertyMapping(4, PtpConstants.Property.WhiteBalance);
        addPropertyMapping(8, PtpConstants.Property.NikonWbColorTemp);
        addPropertyMapping(5, PtpConstants.Property.ExposureProgramMode);
        addPropertyMapping(6, PtpConstants.Property.BatteryLevel);
        addPropertyMapping(9, PtpConstants.Property.FocusMode);
        addPropertyMapping(10, PtpConstants.Property.NikonActivePicCtrlItem);
        addPropertyMapping(11, PtpConstants.Property.ExposureMeteringMode);
        addPropertyMapping(12, Property.FocusMeteringMode);
        addPropertyMapping(13, PtpConstants.Property.NikonFocusArea);
        addPropertyMapping(14, PtpConstants.Property.NikonExposureIndicateStatus);
        addPropertyMapping(16, PtpConstants.Property.ExposureBiasCompensation);
        if (set.contains(Integer.valueOf(PtpConstants.Property.NikonEnableAfAreaPoint))) {
            addInternalProperty(PtpConstants.Property.NikonEnableAfAreaPoint);
        }
        for (Integer num : set) {
            if (this.ptpToVirtualProperty.containsKey(num) || this.ptpInternalProperties.contains(num)) {
                this.queue.add(new GetDevicePropDescCommand(this, num.intValue()));
            }
        }
    }

    protected void openSession() {
        this.queue.add(new NikonOpenSessionAction(this));
    }

    protected void closeSession() {
        this.queue.add(new NikonCloseSessionAction(this));
    }

    protected void queueEventCheck() {
        this.queue.add(new NikonEventCheckCommand(this));
    }

    public void onSessionOpened() {
        super.onSessionOpened();
        Set hashSet = new HashSet();
        for (int valueOf : this.deviceInfo.devicePropertiesSupported) {
            hashSet.add(Integer.valueOf(valueOf));
        }
        for (int valueOf2 : this.vendorPropCodes) {
            hashSet.add(Integer.valueOf(valueOf2));
        }
        onPropertyCodesReceived(hashSet);
    }

    public void setVendorPropCodes(int[] iArr) {
        this.vendorPropCodes = iArr;
    }

    public void onEventObjectAdded(int i) {
        this.queue.add(new RetrieveAddedObjectInfoAction(this, i));
    }

    public boolean hasSupportForOperation(int i) {
        return this.supportedOperations.contains(Integer.valueOf(i));
    }

    public void driveLens(int i, int i2) {
        LinkedBlockingQueue linkedBlockingQueue = this.queue;
        int i3 = 2;
        if (i != 2) {
            i3 = 1;
        }
        linkedBlockingQueue.add(new SimpleCommand(this, Operation.NikonMfDrive, i3, i2 * 300));
    }

    public void onLiveViewStoppedInternal() {
        this.liveViewStoppedInternal = true;
    }

    public void setLiveView(boolean z) {
        this.liveViewStoppedInternal = false;
        if (z) {
            this.queue.add(new NikonStartLiveViewAction(this));
        } else {
            this.queue.add(new NikonStopLiveViewAction(this, true));
        }
    }

    public void getLiveViewPicture(LiveViewData liveViewData) {
        if (this.liveViewSupported && this.liveViewStoppedInternal) {
            this.liveViewStoppedInternal = false;
            this.queue.add(new NikonGetLiveViewImageAction(this, liveViewData));
            return;
        }
        this.queue.add(new NikonGetLiveViewImageCommand(this, liveViewData));
    }

    public boolean isSettingPropertyPossible(int i) {
        Integer num = (Integer) this.ptpProperties.get(Integer.valueOf(PtpConstants.Property.ExposureProgramMode));
        Integer num2 = (Integer) this.ptpProperties.get(Integer.valueOf(PtpConstants.Property.WhiteBalance));
        boolean z = false;
        if (num == null) {
            return false;
        }
        if (i != 8) {
            if (i != 11) {
                switch (i) {
                    case 1:
                        if (num.intValue() == 4 || num.intValue() == 1) {
                            z = true;
                        }
                        return z;
                    case 2:
                        if (num.intValue() == 3 || num.intValue() == 1) {
                            z = true;
                        }
                        return z;
                    case 3:
                    case 4:
                        break;
                    default:
                        switch (i) {
                            case 15:
                                return true;
                            case 16:
                                break;
                            default:
                                return true;
                        }
                }
            }
            if (num.intValue() < 32784) {
                z = true;
            }
            return z;
        }
        if (num2 != null && num2.intValue() == 32786) {
            z = true;
        }
        return z;
    }

    public void focus() {
        this.queue.add(new NikonAfDriveCommand(this));
    }

    public void capture() {
        if (this.liveViewOpen) {
            this.queue.add(new NikonStopLiveViewAction(this, false));
        }
        this.queue.add(new InitiateCaptureCommand(this));
    }

    public void onLiveViewReceived(LiveViewData liveViewData) {
        super.onLiveViewReceived(liveViewData);
        if (liveViewData != null) {
            this.wholeWidth = liveViewData.nikonWholeWidth;
            this.wholeHeight = liveViewData.nikonWholeHeight;
            this.afAreaWidth = liveViewData.nikonAfFrameWidth;
            this.afAreaHeight = liveViewData.nikonAfFrameHeight;
        }
    }

    public void setLiveViewAfArea(float f, float f2) {
        if (this.supportedOperations.contains(Integer.valueOf(Operation.NikonChangeAfArea))) {
            this.queue.add(new SimpleCommand(this, Operation.NikonChangeAfArea, (int) Math.min((float) (this.wholeWidth - (this.afAreaWidth >> 1)), Math.max((float) (this.afAreaWidth >> 1), f * ((float) this.wholeWidth))), (int) Math.min((float) (this.wholeHeight - (this.afAreaHeight >> 1)), Math.max((float) (this.afAreaHeight >> 1), f2 * ((float) this.wholeHeight)))));
        }
    }

    public List<FocusPoint> getFocusPoints() {
        List<FocusPoint> arrayList = new ArrayList();
        int i;
        switch (this.productId) {
            case 1040:
            case 1042:
                arrayList.add(new FocusPoint(0, 0.5f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(1, 0.5f, 0.29f, 0.04f));
                arrayList.add(new FocusPoint(2, 0.5f, 0.71f, 0.04f));
                arrayList.add(new FocusPoint(3, 0.33f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(4, 0.67f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(5, 0.22f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(6, 0.78f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(7, 0.33f, 0.39f, 0.04f));
                arrayList.add(new FocusPoint(8, 0.67f, 0.39f, 0.04f));
                arrayList.add(new FocusPoint(9, 0.33f, 0.61f, 0.04f));
                arrayList.add(new FocusPoint(10, 0.67f, 0.61f, 0.04f));
                return arrayList;
            case 1044:
                arrayList.add(new FocusPoint(0, 0.5f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(0, 0.3f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(0, 0.7f, 0.5f, 0.04f));
                return arrayList;
            case 1050:
            case 1052:
            case Product.NikonD3X /*1056*/:
            case 1061:
            case 1062:
                arrayList.add(new FocusPoint(1, 0.5f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(3, 0.5f, 0.36f, 0.035f));
                arrayList.add(new FocusPoint(5, 0.5f, 0.64f, 0.035f));
                arrayList.add(new FocusPoint(21, 0.65f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(23, 0.65f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(25, 0.65f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(31, 0.75f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(39, 0.35f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(41, 0.35f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(43, 0.35f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(49, 0.25f, 0.5f, 0.035f));
                i = this.enableAfAreaPoint;
                return arrayList;
            case 1057:
            case 1059:
                arrayList.add(new FocusPoint(1, 0.5f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(2, 0.5f, 0.3f, 0.04f));
                arrayList.add(new FocusPoint(3, 0.5f, 0.7f, 0.04f));
                arrayList.add(new FocusPoint(4, 0.33f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(5, 0.33f, 0.35f, 0.04f));
                arrayList.add(new FocusPoint(6, 0.33f, 0.65f, 0.04f));
                arrayList.add(new FocusPoint(7, 0.22f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(8, 0.67f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(9, 0.67f, 0.35f, 0.04f));
                arrayList.add(new FocusPoint(10, 0.67f, 0.65f, 0.04f));
                arrayList.add(new FocusPoint(11, 0.78f, 0.5f, 0.04f));
                return arrayList;
            case 1064:
                arrayList.add(new FocusPoint(1, 0.5f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(3, 0.5f, 0.32f, 0.035f));
                arrayList.add(new FocusPoint(5, 0.5f, 0.68f, 0.035f));
                arrayList.add(new FocusPoint(19, 0.68f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(20, 0.68f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(21, 0.68f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(25, 0.8f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(31, 0.32f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(32, 0.32f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(33, 0.32f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(37, 0.2f, 0.5f, 0.035f));
                i = this.enableAfAreaPoint;
                return arrayList;
            default:
                return arrayList;
        }
    }
}
