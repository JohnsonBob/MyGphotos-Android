package io.gphotos.gin.Database;

import android.util.Log;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import io.gphotos.gin.service.ImageInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImageModel extends BaseModel {
    private static final String TAG = "ImageModel";
    public int compressedSize;
    public long dateCreated;
    public String filePath;
    public int format;
    public long id;
    public int imagePixDepth;
    public int imagePixHeight;
    public int imagePixWidth;
    public boolean isUploaded;
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
    public String thumbnailPath;
    public int uploadStatus;

    public void log() {
        Log.d(TAG, toString());
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%s - %s - %d - %d", new Object[]{this.name, this.keywords, Long.valueOf(this.dateCreated), Integer.valueOf(this.objectHandle)});
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

    public static void saveImageModelList(List<ImageModel> list) {
        List<ImageModel> queryList = new Select(new IProperty[0]).from(ImageModel.class).queryList();
        List<ImageModel> arrayList = new ArrayList();
        for (ImageModel imageModel : list) {
            Object obj;
            for (ImageModel imageModel2 : queryList) {
                if (imageModel2.name.equals(imageModel.name)) {
                    obj = 1;
                    break;
                }
            }
            obj = null;
            if (obj == null) {
                arrayList.add(imageModel);
            }
        }
        DatabaseWrapper writableDatabase = FlowManager.getDatabase(GinDatabase.NAME).getWritableDatabase();
        try {
            writableDatabase.beginTransaction();
            for (ImageModel save : arrayList) {
                save.save();
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            writableDatabase.endTransaction();
        }
        writableDatabase.endTransaction();
    }
}
