package io.gphotos.gin.Database;

import android.content.ContentValues;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.sql.saveable.AutoIncrementModelSaver;
import com.raizlabs.android.dbflow.sql.saveable.ModelSaver;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

public final class ImageModel_Table extends ModelAdapter<ImageModel> {
    public static final Property<Integer> compressedSize = new Property(ImageModel.class, "compressedSize");
    public static final Property<Long> dateCreated = new Property(ImageModel.class, "dateCreated");
    public static final Property<String> filePath = new Property(ImageModel.class, "filePath");
    public static final Property<Integer> format = new Property(ImageModel.class, "format");
    public static final Property<Long> id = new Property(ImageModel.class, "id");
    public static final Property<Integer> imagePixDepth = new Property(ImageModel.class, "imagePixDepth");
    public static final Property<Integer> imagePixHeight = new Property(ImageModel.class, "imagePixHeight");
    public static final Property<Integer> imagePixWidth = new Property(ImageModel.class, "imagePixWidth");
    public static final Property<Boolean> isUploaded = new Property(ImageModel.class, "isUploaded");
    public static final Property<String> keywords = new Property(ImageModel.class, "keywords");
    public static final Property<String> name = new Property(ImageModel.class, "name");
    public static final Property<Integer> objectHandle = new Property(ImageModel.class, "objectHandle");
    public static final Property<Integer> parent = new Property(ImageModel.class, "parent");
    public static final Property<Integer> sequenceNumber = new Property(ImageModel.class, "sequenceNumber");
    public static final Property<Integer> storageId = new Property(ImageModel.class, "storageId");
    public static final Property<Integer> thumbCompressedSize = new Property(ImageModel.class, "thumbCompressedSize");
    public static final Property<Integer> thumbFormat = new Property(ImageModel.class, "thumbFormat");
    public static final Property<Integer> thumbPixHeight = new Property(ImageModel.class, "thumbPixHeight");
    public static final Property<Integer> thumbPixWidth = new Property(ImageModel.class, "thumbPixWidth");
    public static final Property<String> thumbnailPath = new Property(ImageModel.class, "thumbnailPath");
    public static final Property<Integer> uploadStatus = new Property(ImageModel.class, "uploadStatus");
    public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{id, name, dateCreated,
            imagePixDepth, imagePixHeight, imagePixWidth, format, compressedSize, keywords,
            objectHandle, parent, sequenceNumber, storageId, thumbFormat, thumbCompressedSize,
            thumbPixHeight, thumbPixWidth, filePath, thumbnailPath, isUploaded, uploadStatus};
    public final String getAutoIncrementingColumnName() {
        return "id";
    }

    public final String getCompiledStatementQuery() {
        return "INSERT INTO `ImageModel`(`id`,`name`,`dateCreated`,`imagePixDepth`,`imagePixHeight`,`imagePixWidth`,`format`,`compressedSize`,`keywords`,`objectHandle`,`parent`,`sequenceNumber`,`storageId`,`thumbFormat`,`thumbCompressedSize`,`thumbPixHeight`,`thumbPixWidth`,`filePath`,`thumbnailPath`,`isUploaded`,`uploadStatus`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getCreationQuery() {
        return "CREATE TABLE IF NOT EXISTS `ImageModel`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT, `dateCreated` INTEGER, `imagePixDepth` INTEGER, `imagePixHeight` INTEGER, `imagePixWidth` INTEGER, `format` INTEGER, `compressedSize` INTEGER, `keywords` TEXT, `objectHandle` INTEGER, `parent` INTEGER, `sequenceNumber` INTEGER, `storageId` INTEGER, `thumbFormat` INTEGER, `thumbCompressedSize` INTEGER, `thumbPixHeight` INTEGER, `thumbPixWidth` INTEGER, `filePath` TEXT, `thumbnailPath` TEXT, `isUploaded` INTEGER, `uploadStatus` INTEGER)";
    }

    public final String getDeleteStatementQuery() {
        return "DELETE FROM `ImageModel` WHERE `id`=?";
    }

    public final String getInsertStatementQuery() {
        return "INSERT INTO `ImageModel`(`name`,`dateCreated`,`imagePixDepth`,`imagePixHeight`,`imagePixWidth`,`format`,`compressedSize`,`keywords`,`objectHandle`,`parent`,`sequenceNumber`,`storageId`,`thumbFormat`,`thumbCompressedSize`,`thumbPixHeight`,`thumbPixWidth`,`filePath`,`thumbnailPath`,`isUploaded`,`uploadStatus`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getTableName() {
        return "`ImageModel`";
    }

    public final String getUpdateStatementQuery() {
        return "UPDATE `ImageModel` SET `id`=?,`name`=?,`dateCreated`=?,`imagePixDepth`=?,`imagePixHeight`=?,`imagePixWidth`=?,`format`=?,`compressedSize`=?,`keywords`=?,`objectHandle`=?,`parent`=?,`sequenceNumber`=?,`storageId`=?,`thumbFormat`=?,`thumbCompressedSize`=?,`thumbPixHeight`=?,`thumbPixWidth`=?,`filePath`=?,`thumbnailPath`=?,`isUploaded`=?,`uploadStatus`=? WHERE `id`=?";
    }

    public ImageModel_Table(DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
    }

    public final Class<ImageModel> getModelClass() {
        return ImageModel.class;
    }

    public final ImageModel newInstance() {
        return new ImageModel();
    }

    public final com.raizlabs.android.dbflow.sql.language.property.Property getProperty(java.lang.String r2) {

        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.Database.ImageModel_Table.getProperty(java.lang.String):com.raizlabs.android.dbflow.sql.language.property.Property");
    }

    public final void updateAutoIncrement(ImageModel imageModel, Number number) {
        imageModel.id = number.longValue();
    }

    public final Number getAutoIncrementingId(ImageModel imageModel) {
        return Long.valueOf(imageModel.id);
    }

    public final ModelSaver<ImageModel> createSingleModelSaver() {
        return new AutoIncrementModelSaver();
    }

    public final IProperty[] getAllColumnProperties() {
        return ALL_COLUMN_PROPERTIES;
    }

    public final void bindToInsertValues(ContentValues contentValues, ImageModel imageModel) {
        contentValues.put("`name`", imageModel.name);
        contentValues.put("`dateCreated`", Long.valueOf(imageModel.dateCreated));
        contentValues.put("`imagePixDepth`", Integer.valueOf(imageModel.imagePixDepth));
        contentValues.put("`imagePixHeight`", Integer.valueOf(imageModel.imagePixHeight));
        contentValues.put("`imagePixWidth`", Integer.valueOf(imageModel.imagePixWidth));
        contentValues.put("`format`", Integer.valueOf(imageModel.format));
        contentValues.put("`compressedSize`", Integer.valueOf(imageModel.compressedSize));
        contentValues.put("`keywords`", imageModel.keywords);
        contentValues.put("`objectHandle`", Integer.valueOf(imageModel.objectHandle));
        contentValues.put("`parent`", Integer.valueOf(imageModel.parent));
        contentValues.put("`sequenceNumber`", Integer.valueOf(imageModel.sequenceNumber));
        contentValues.put("`storageId`", Integer.valueOf(imageModel.storageId));
        contentValues.put("`thumbFormat`", Integer.valueOf(imageModel.thumbFormat));
        contentValues.put("`thumbCompressedSize`", Integer.valueOf(imageModel.thumbCompressedSize));
        contentValues.put("`thumbPixHeight`", Integer.valueOf(imageModel.thumbPixHeight));
        contentValues.put("`thumbPixWidth`", Integer.valueOf(imageModel.thumbPixWidth));
        contentValues.put("`filePath`", imageModel.filePath);
        contentValues.put("`thumbnailPath`", imageModel.thumbnailPath);
        contentValues.put("`isUploaded`", Integer.valueOf(imageModel.isUploaded == true ? 1 : 0));
        contentValues.put("`uploadStatus`", Integer.valueOf(imageModel.uploadStatus));
    }

    public final void bindToContentValues(ContentValues contentValues, ImageModel imageModel) {
        contentValues.put("`id`", Long.valueOf(imageModel.id));
        bindToInsertValues(contentValues, imageModel);
    }

    public final void bindToInsertStatement(DatabaseStatement databaseStatement, ImageModel imageModel, int i) {
        databaseStatement.bindStringOrNull(i + 1, imageModel.name);
        databaseStatement.bindLong(i + 2, imageModel.dateCreated);
        databaseStatement.bindLong(i + 3, (long) imageModel.imagePixDepth);
        databaseStatement.bindLong(i + 4, (long) imageModel.imagePixHeight);
        databaseStatement.bindLong(i + 5, (long) imageModel.imagePixWidth);
        databaseStatement.bindLong(i + 6, (long) imageModel.format);
        databaseStatement.bindLong(i + 7, (long) imageModel.compressedSize);
        databaseStatement.bindStringOrNull(i + 8, imageModel.keywords);
        databaseStatement.bindLong(i + 9, (long) imageModel.objectHandle);
        databaseStatement.bindLong(i + 10, (long) imageModel.parent);
        databaseStatement.bindLong(i + 11, (long) imageModel.sequenceNumber);
        databaseStatement.bindLong(i + 12, (long) imageModel.storageId);
        databaseStatement.bindLong(i + 13, (long) imageModel.thumbFormat);
        databaseStatement.bindLong(i + 14, (long) imageModel.thumbCompressedSize);
        databaseStatement.bindLong(i + 15, (long) imageModel.thumbPixHeight);
        databaseStatement.bindLong(i + 16, (long) imageModel.thumbPixWidth);
        databaseStatement.bindStringOrNull(i + 17, imageModel.filePath);
        databaseStatement.bindStringOrNull(i + 18, imageModel.thumbnailPath);
        databaseStatement.bindLong(i + 19, imageModel.isUploaded ? 1 : 0);
        databaseStatement.bindLong(i + 20, (long) imageModel.uploadStatus);
    }

    public final void bindToStatement(DatabaseStatement databaseStatement, ImageModel imageModel) {
        databaseStatement.bindLong(1, imageModel.id);
        bindToInsertStatement(databaseStatement, imageModel, 1);
    }

    public final void bindToUpdateStatement(DatabaseStatement databaseStatement, ImageModel imageModel) {
        databaseStatement.bindLong(1, imageModel.id);
        databaseStatement.bindStringOrNull(2, imageModel.name);
        databaseStatement.bindLong(3, imageModel.dateCreated);
        databaseStatement.bindLong(4, (long) imageModel.imagePixDepth);
        databaseStatement.bindLong(5, (long) imageModel.imagePixHeight);
        databaseStatement.bindLong(6, (long) imageModel.imagePixWidth);
        databaseStatement.bindLong(7, (long) imageModel.format);
        databaseStatement.bindLong(8, (long) imageModel.compressedSize);
        databaseStatement.bindStringOrNull(9, imageModel.keywords);
        databaseStatement.bindLong(10, (long) imageModel.objectHandle);
        databaseStatement.bindLong(11, (long) imageModel.parent);
        databaseStatement.bindLong(12, (long) imageModel.sequenceNumber);
        databaseStatement.bindLong(13, (long) imageModel.storageId);
        databaseStatement.bindLong(14, (long) imageModel.thumbFormat);
        databaseStatement.bindLong(15, (long) imageModel.thumbCompressedSize);
        databaseStatement.bindLong(16, (long) imageModel.thumbPixHeight);
        databaseStatement.bindLong(17, (long) imageModel.thumbPixWidth);
        databaseStatement.bindStringOrNull(18, imageModel.filePath);
        databaseStatement.bindStringOrNull(19, imageModel.thumbnailPath);
        databaseStatement.bindLong(20, imageModel.isUploaded ? 1 : 0);
        databaseStatement.bindLong(21, (long) imageModel.uploadStatus);
        databaseStatement.bindLong(22, imageModel.id);
    }

    public final void bindToDeleteStatement(DatabaseStatement databaseStatement, ImageModel imageModel) {
        databaseStatement.bindLong(1, imageModel.id);
    }

    public final void loadFromCursor(FlowCursor flowCursor, ImageModel imageModel) {
        imageModel.id = flowCursor.getLongOrDefault("id");
        imageModel.name = flowCursor.getStringOrDefault("name");
        imageModel.dateCreated = flowCursor.getLongOrDefault("dateCreated");
        imageModel.imagePixDepth = flowCursor.getIntOrDefault("imagePixDepth");
        imageModel.imagePixHeight = flowCursor.getIntOrDefault("imagePixHeight");
        imageModel.imagePixWidth = flowCursor.getIntOrDefault("imagePixWidth");
        imageModel.format = flowCursor.getIntOrDefault("format");
        imageModel.compressedSize = flowCursor.getIntOrDefault("compressedSize");
        imageModel.keywords = flowCursor.getStringOrDefault("keywords");
        imageModel.objectHandle = flowCursor.getIntOrDefault("objectHandle");
        imageModel.parent = flowCursor.getIntOrDefault("parent");
        imageModel.sequenceNumber = flowCursor.getIntOrDefault("sequenceNumber");
        imageModel.storageId = flowCursor.getIntOrDefault("storageId");
        imageModel.thumbFormat = flowCursor.getIntOrDefault("thumbFormat");
        imageModel.thumbCompressedSize = flowCursor.getIntOrDefault("thumbCompressedSize");
        imageModel.thumbPixHeight = flowCursor.getIntOrDefault("thumbPixHeight");
        imageModel.thumbPixWidth = flowCursor.getIntOrDefault("thumbPixWidth");
        imageModel.filePath = flowCursor.getStringOrDefault("filePath");
        imageModel.thumbnailPath = flowCursor.getStringOrDefault("thumbnailPath");
        int columnIndex = flowCursor.getColumnIndex("isUploaded");
        if (columnIndex == -1 || flowCursor.isNull(columnIndex)) {
            imageModel.isUploaded = false;
        } else {
            imageModel.isUploaded = flowCursor.getBoolean(columnIndex);
        }
        imageModel.uploadStatus = flowCursor.getIntOrDefault("uploadStatus");
    }

    public final boolean exists(ImageModel imageModel, DatabaseWrapper databaseWrapper) {
        if (imageModel.id > 0) {
            if (SQLite.selectCountOf(new IProperty[0]).from(ImageModel.class).where(getPrimaryConditionClause(imageModel)).hasData(databaseWrapper)) {
                return true;
            }
        }
        return false;
    }

    public final OperatorGroup getPrimaryConditionClause(ImageModel imageModel) {
        OperatorGroup clause = OperatorGroup.clause();
        clause.and(id.eq(Long.valueOf(imageModel.id)));
        return clause;
    }
}
