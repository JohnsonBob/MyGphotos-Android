package io.gphotos.gin.service;

import android.util.Log;
import java.util.Locale;

public class ImageInfo {
    private static final String TAG = "ImageInfo";
    public int compressedSize;
    public long dateCreated;
    public int format;
    public int imagePixDepth;
    public int imagePixHeight;
    public int imagePixWidth;
    public String keywords;
    public String name;
    public int objectHandle;
    public int parent;
    public int sequenceNumber;
    public int storageId;
    public int thumbCompressedSize;
    public int thumbFormat;
    public int thumbPixHeight;
    public int thumbPixWidth;

    public void log() {
        Log.d(TAG, toString());
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%s - %s - %d", new Object[]{this.name, this.keywords, Long.valueOf(this.dateCreated)});
    }

    public boolean equals(Object obj) {
        if (obj instanceof ImageInfo) {
            ImageInfo imageInfo = (ImageInfo) obj;
            if (this.dateCreated == imageInfo.dateCreated && this.name.equals(imageInfo.name)) {
                return true;
            }
        }
        return false;
    }
}
