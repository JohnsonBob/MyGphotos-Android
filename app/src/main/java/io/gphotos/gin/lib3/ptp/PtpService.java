package io.gphotos.gin.lib3.ptp;

import android.content.Context;
import android.content.Intent;
import io.gphotos.gin.lib3.ptp.Camera.CameraListener;

public interface PtpService {

    public static class Singleton {
        private static PtpService singleton;

        public static PtpService getInstance(Context context) {
            if (singleton == null) {
                singleton = new PtpUsbService(context);
            }
            return singleton;
        }

        public static void setInstance(PtpService ptpService) {
            singleton = ptpService;
        }
    }

    void initialize(Context context, Intent intent);

    void lazyShutdown();

    void setCameraListener(CameraListener cameraListener);

    void shutdown();
}
