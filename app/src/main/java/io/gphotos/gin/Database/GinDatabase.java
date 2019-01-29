package io.gphotos.gin.Database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = GinDatabase.NAME, version = GinDatabase.VERSION)
public class GinDatabase {
    public static final String NAME = "GinDbs";
    public static final int VERSION = 1;
}
