package io.gphotos.gin.service;

import android.content.Context;
import android.content.Intent;

public interface MtpService {

    public static class Singleton {
        private static MtpService singleton;

        public static MtpService getInstance(Context context) {
            if (singleton == null) {
                singleton = new MtpUsbService(context);
            }
            return singleton;
        }

        public static void setInstance(MtpService mtpService) {
            singleton = mtpService;
        }
    }

    void initialize(Context context, Intent intent);

    void shutdown();

    void testJob();
}
