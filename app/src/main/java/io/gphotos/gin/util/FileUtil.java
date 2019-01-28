package io.gphotos.gin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Operator.Operation;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import io.gphotos.gin.Database.ImageModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

public class FileUtil {
    private static final String FOLDER = "Gphotos";

    public static boolean isValidFileType(String str) {
        String str2 = "";
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf > 0) {
            str2 = str.substring(lastIndexOf + 1);
        }
        if (str2.toLowerCase().equals("cr2")) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Missing block: B:20:0x0049, code:
            return null;
     */
    public static java.lang.String getMD5(java.io.File r5) {
        /*
        r0 = 0;
        r1 = "MD5";
        r1 = java.security.MessageDigest.getInstance(r1);	 Catch:{ NoSuchAlgorithmException -> 0x004b }
        r2 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x004a }
        r2.<init>(r5);	 Catch:{ FileNotFoundException -> 0x004a }
        r5 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r5 = new byte[r5];
    L_0x0010:
        r3 = r2.read(r5);	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r4 = 0;
        if (r3 <= 0) goto L_0x001b;
    L_0x0017:
        r1.update(r5, r4, r3);	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        goto L_0x0010;
    L_0x001b:
        r5 = r1.digest();	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r1 = new java.math.BigInteger;	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r3 = 1;
        r1.<init>(r3, r5);	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r5 = 16;
        r5 = r1.toString(r5);	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r1 = "%32s";
        r3 = new java.lang.Object[r3];	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r3[r4] = r5;	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r5 = java.lang.String.format(r1, r3);	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r1 = 32;
        r3 = 48;
        r5 = r5.replace(r1, r3);	 Catch:{ IOException -> 0x0046, all -> 0x0041 }
        r2.close();	 Catch:{ IOException -> 0x0040 }
    L_0x0040:
        return r5;
    L_0x0041:
        r5 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x0045 }
    L_0x0045:
        throw r5;
    L_0x0046:
        r2.close();	 Catch:{ IOException -> 0x0049 }
    L_0x0049:
        return r0;
    L_0x004a:
        return r0;
    L_0x004b:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.util.FileUtil.getMD5(java.io.File):java.lang.String");
    }

    public static String createFileNameForCameraImageOriginal(String str, String str2, String str3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append(File.separator);
        stringBuilder.append(FOLDER);
        stringBuilder.append(File.separator);
        stringBuilder.append("cache");
        stringBuilder.append(File.separator);
        stringBuilder.append("bitmap");
        stringBuilder.append(File.separator);
        stringBuilder.append(str3);
        stringBuilder.append(Operation.MINUS);
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }

    public static String createFileNameForCameraImage(String str, String str2, String str3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append(File.separator);
        stringBuilder.append(FOLDER);
        stringBuilder.append(File.separator);
        stringBuilder.append("cache");
        stringBuilder.append(File.separator);
        stringBuilder.append("thumbnail");
        stringBuilder.append(File.separator);
        stringBuilder.append(str3);
        stringBuilder.append(Operation.MINUS);
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }

    public static File saveBitmap2File(Bitmap bitmap, String str) {
        return saveBitmap2File(bitmap, str, 100);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0056 A:{SYNTHETIC, Splitter: B:38:0x0056} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0050 A:{SYNTHETIC, Splitter: B:34:0x0050} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0050 A:{SYNTHETIC, Splitter: B:34:0x0050} */
    public static java.io.File saveBitmap2File(android.graphics.Bitmap r3, java.lang.String r4, int r5) {
        /*
        r0 = 0;
        r1 = new java.io.File;	 Catch:{ Exception -> 0x0049 }
        r1.<init>(r4);	 Catch:{ Exception -> 0x0049 }
        r4 = r1.exists();	 Catch:{ Exception -> 0x0045 }
        if (r4 == 0) goto L_0x000d;
    L_0x000c:
        return r1;
    L_0x000d:
        r4 = 1;
        r2 = r1.getParentFile();	 Catch:{ Exception -> 0x0045 }
        r2 = r2.exists();	 Catch:{ Exception -> 0x0045 }
        if (r2 != 0) goto L_0x0020;
    L_0x0018:
        r4 = r1.getParentFile();	 Catch:{ Exception -> 0x0045 }
        r4 = r4.mkdirs();	 Catch:{ Exception -> 0x0045 }
    L_0x0020:
        if (r4 != 0) goto L_0x0023;
    L_0x0022:
        return r1;
    L_0x0023:
        r4 = r1.createNewFile();	 Catch:{ Exception -> 0x0045 }
        if (r4 != 0) goto L_0x002a;
    L_0x0029:
        return r1;
    L_0x002a:
        r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0045 }
        r4.<init>(r1);	 Catch:{ Exception -> 0x0045 }
        r0 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0042, all -> 0x003f }
        r3.compress(r0, r5, r4);	 Catch:{ Exception -> 0x0042, all -> 0x003f }
        if (r4 == 0) goto L_0x0053;
    L_0x0036:
        r4.close();	 Catch:{ Exception -> 0x003a }
        goto L_0x0053;
    L_0x003a:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0053;
    L_0x003f:
        r3 = move-exception;
        r0 = r4;
        goto L_0x0054;
    L_0x0042:
        r3 = move-exception;
        r0 = r4;
        goto L_0x004b;
    L_0x0045:
        r3 = move-exception;
        goto L_0x004b;
    L_0x0047:
        r3 = move-exception;
        goto L_0x0054;
    L_0x0049:
        r3 = move-exception;
        r1 = r0;
    L_0x004b:
        r3.printStackTrace();	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x0053;
    L_0x0050:
        r0.close();	 Catch:{ Exception -> 0x003a }
    L_0x0053:
        return r1;
    L_0x0054:
        if (r0 == 0) goto L_0x005e;
    L_0x0056:
        r0.close();	 Catch:{ Exception -> 0x005a }
        goto L_0x005e;
    L_0x005a:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x005e:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.util.FileUtil.saveBitmap2File(android.graphics.Bitmap, java.lang.String, int):java.io.File");
    }

    public static File saveByte2File(byte[] bArr) {
        return saveBytes2File(bArr, createFileNameForCameraImageOriginal("", "test2.JPG", "today"));
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x005f A:{SYNTHETIC, Splitter: B:45:0x005f} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0069 A:{SYNTHETIC, Splitter: B:50:0x0069} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0070 A:{SYNTHETIC, Splitter: B:55:0x0070} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x007a A:{SYNTHETIC, Splitter: B:60:0x007a} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x005f A:{SYNTHETIC, Splitter: B:45:0x005f} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0069 A:{SYNTHETIC, Splitter: B:50:0x0069} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0070 A:{SYNTHETIC, Splitter: B:55:0x0070} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x007a A:{SYNTHETIC, Splitter: B:60:0x007a} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0070 A:{SYNTHETIC, Splitter: B:55:0x0070} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x007a A:{SYNTHETIC, Splitter: B:60:0x007a} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x005f A:{SYNTHETIC, Splitter: B:45:0x005f} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0069 A:{SYNTHETIC, Splitter: B:50:0x0069} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0054 A:{ExcHandler: all (th java.lang.Throwable), Splitter: B:1:0x0001} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:38:0x0054, code:
            r3 = th;
     */
    /* JADX WARNING: Missing block: B:39:0x0055, code:
            r2 = null;
     */
    /* JADX WARNING: Missing block: B:40:0x0057, code:
            r3 = e;
     */
    /* JADX WARNING: Missing block: B:41:0x0058, code:
            r1 = null;
            r2 = r1;
     */
    /* JADX WARNING: Missing block: B:46:?, code:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:47:0x0063, code:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:48:0x0064, code:
            r3.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:51:?, code:
            r2.close();
     */
    /* JADX WARNING: Missing block: B:56:?, code:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:57:0x0074, code:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:58:0x0075, code:
            r4.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:61:?, code:
            r2.close();
     */
    /* JADX WARNING: Missing block: B:62:0x007e, code:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:63:0x007f, code:
            r4.printStackTrace();
     */
    public static java.io.File saveBytes2File(byte[] r3, java.lang.String r4) {
        /*
        r0 = 0;
        r1 = new java.io.File;	 Catch:{ Exception -> 0x0057, all -> 0x0054 }
        r1.<init>(r4);	 Catch:{ Exception -> 0x0057, all -> 0x0054 }
        r4 = 1;
        r2 = r1.getParentFile();	 Catch:{ Exception -> 0x0051, all -> 0x0054 }
        r2 = r2.exists();	 Catch:{ Exception -> 0x0051, all -> 0x0054 }
        if (r2 != 0) goto L_0x0019;
    L_0x0011:
        r4 = r1.getParentFile();	 Catch:{ Exception -> 0x0051, all -> 0x0054 }
        r4 = r4.mkdirs();	 Catch:{ Exception -> 0x0051, all -> 0x0054 }
    L_0x0019:
        if (r4 != 0) goto L_0x001c;
    L_0x001b:
        return r1;
    L_0x001c:
        r4 = r1.createNewFile();	 Catch:{ Exception -> 0x0051, all -> 0x0054 }
        if (r4 != 0) goto L_0x0023;
    L_0x0022:
        return r1;
    L_0x0023:
        r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0051, all -> 0x0054 }
        r4.<init>(r1);	 Catch:{ Exception -> 0x0051, all -> 0x0054 }
        r2 = new java.io.BufferedOutputStream;	 Catch:{ Exception -> 0x004d, all -> 0x0049 }
        r2.<init>(r4);	 Catch:{ Exception -> 0x004d, all -> 0x0049 }
        r2.write(r3);	 Catch:{ Exception -> 0x0047, all -> 0x0045 }
        if (r4 == 0) goto L_0x003a;
    L_0x0032:
        r4.close();	 Catch:{ Exception -> 0x0036 }
        goto L_0x003a;
    L_0x0036:
        r3 = move-exception;
        r3.printStackTrace();
    L_0x003a:
        if (r2 == 0) goto L_0x006c;
    L_0x003c:
        r2.close();	 Catch:{ Exception -> 0x0040 }
        goto L_0x006c;
    L_0x0040:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x006c;
    L_0x0045:
        r3 = move-exception;
        goto L_0x004b;
    L_0x0047:
        r3 = move-exception;
        goto L_0x004f;
    L_0x0049:
        r3 = move-exception;
        r2 = r0;
    L_0x004b:
        r0 = r4;
        goto L_0x006e;
    L_0x004d:
        r3 = move-exception;
        r2 = r0;
    L_0x004f:
        r0 = r4;
        goto L_0x005a;
    L_0x0051:
        r3 = move-exception;
        r2 = r0;
        goto L_0x005a;
    L_0x0054:
        r3 = move-exception;
        r2 = r0;
        goto L_0x006e;
    L_0x0057:
        r3 = move-exception;
        r1 = r0;
        r2 = r1;
    L_0x005a:
        r3.printStackTrace();	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x0067;
    L_0x005f:
        r0.close();	 Catch:{ Exception -> 0x0063 }
        goto L_0x0067;
    L_0x0063:
        r3 = move-exception;
        r3.printStackTrace();
    L_0x0067:
        if (r2 == 0) goto L_0x006c;
    L_0x0069:
        r2.close();	 Catch:{ Exception -> 0x0040 }
    L_0x006c:
        return r1;
    L_0x006d:
        r3 = move-exception;
    L_0x006e:
        if (r0 == 0) goto L_0x0078;
    L_0x0070:
        r0.close();	 Catch:{ Exception -> 0x0074 }
        goto L_0x0078;
    L_0x0074:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x0078:
        if (r2 == 0) goto L_0x0082;
    L_0x007a:
        r2.close();	 Catch:{ Exception -> 0x007e }
        goto L_0x0082;
    L_0x007e:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x0082:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.util.FileUtil.saveBytes2File(byte[], java.lang.String):java.io.File");
    }

    public static void clearDB() {
        Delete.table(ImageModel.class, new SQLOperator[0]);
    }

    public static void clearFileCache() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append(File.separator);
        stringBuilder.append(FOLDER);
        stringBuilder.append(File.separator);
        stringBuilder.append("cache");
        deleteDir(new File(stringBuilder.toString()));
    }

    public static void deleteDir(File file) {
        if (file.isDirectory()) {
            for (File deleteDir : file.listFiles()) {
                deleteDir(deleteDir);
            }
        }
        file.delete();
    }

    public static void copyFile(File file, File file2) throws IOException {
        if (!file2.getParentFile().exists() ? file2.getParentFile().mkdirs() : true) {
            InputStream fileInputStream = new FileInputStream(file);
            OutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file2);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.close();
                        fileInputStream.close();
                        return;
                    }
                }
            } catch (Throwable th) {
                fileInputStream.close();
            }
        } else {
            throw new IOException("parent file not exists");
        }
    }

    public static Bitmap decodeFile(String str, int i, int i2) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = getSampleSize(i, i2, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    private static int getSampleSize(int i, int i2, Options options) {
        if (options.outWidth > i || options.outHeight > i2) {
            return Math.max(Math.round(((float) options.outWidth) / ((float) i)), Math.round(((float) options.outHeight) / ((float) i2)));
        }
        return 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x00c8 A:{SYNTHETIC, Splitter: B:55:0x00c8} */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00da A:{SYNTHETIC, Splitter: B:64:0x00da} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00c8 A:{SYNTHETIC, Splitter: B:55:0x00c8} */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00da A:{SYNTHETIC, Splitter: B:64:0x00da} */
    public static boolean copyExifData(java.io.File r10, java.io.File r11, java.util.List<org.apache.sanselan.formats.tiff.constants.TagInfo> r12) {
        /*
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r11.getAbsolutePath();
        r0.append(r1);
        r1 = ".tmp";
        r0.append(r1);
        r0 = r0.toString();
        r1 = 0;
        r2 = 0;
        r3 = new java.io.File;	 Catch:{ Exception -> 0x00c1, all -> 0x00be }
        r3.<init>(r0);	 Catch:{ Exception -> 0x00c1, all -> 0x00be }
        r0 = 73;
        r10 = getSanselanOutputSet(r10, r0);	 Catch:{ Exception -> 0x00bc }
        r0 = r10.byteOrder;	 Catch:{ Exception -> 0x00bc }
        r0 = getSanselanOutputSet(r11, r0);	 Catch:{ Exception -> 0x00bc }
        r4 = r10.byteOrder;	 Catch:{ Exception -> 0x00bc }
        r5 = r0.byteOrder;	 Catch:{ Exception -> 0x00bc }
        if (r4 == r5) goto L_0x003a;
    L_0x002e:
        if (r3 == 0) goto L_0x0039;
    L_0x0030:
        r10 = r3.exists();
        if (r10 == 0) goto L_0x0039;
    L_0x0036:
        r3.delete();
    L_0x0039:
        return r2;
    L_0x003a:
        r0.getOrCreateExifDirectory();	 Catch:{ Exception -> 0x00bc }
        r10 = r10.getDirectories();	 Catch:{ Exception -> 0x00bc }
        r4 = 0;
    L_0x0042:
        r5 = r10.size();	 Catch:{ Exception -> 0x00bc }
        if (r4 >= r5) goto L_0x0084;
    L_0x0048:
        r5 = r10.get(r4);	 Catch:{ Exception -> 0x00bc }
        r5 = (org.apache.sanselan.formats.tiff.write.TiffOutputDirectory) r5;	 Catch:{ Exception -> 0x00bc }
        r6 = getOrCreateExifDirectory(r0, r5);	 Catch:{ Exception -> 0x00bc }
        if (r6 != 0) goto L_0x0055;
    L_0x0054:
        goto L_0x0081;
    L_0x0055:
        r5 = r5.getFields();	 Catch:{ Exception -> 0x00bc }
        r7 = 0;
    L_0x005a:
        r8 = r5.size();	 Catch:{ Exception -> 0x00bc }
        if (r7 >= r8) goto L_0x0081;
    L_0x0060:
        r8 = r5.get(r7);	 Catch:{ Exception -> 0x00bc }
        r8 = (org.apache.sanselan.formats.tiff.write.TiffOutputField) r8;	 Catch:{ Exception -> 0x00bc }
        if (r12 == 0) goto L_0x0076;
    L_0x0068:
        r9 = r8.tagInfo;	 Catch:{ Exception -> 0x00bc }
        r9 = r12.contains(r9);	 Catch:{ Exception -> 0x00bc }
        if (r9 == 0) goto L_0x0076;
    L_0x0070:
        r8 = r8.tagInfo;	 Catch:{ Exception -> 0x00bc }
        r6.removeField(r8);	 Catch:{ Exception -> 0x00bc }
        goto L_0x007e;
    L_0x0076:
        r9 = r8.tagInfo;	 Catch:{ Exception -> 0x00bc }
        r6.removeField(r9);	 Catch:{ Exception -> 0x00bc }
        r6.add(r8);	 Catch:{ Exception -> 0x00bc }
    L_0x007e:
        r7 = r7 + 1;
        goto L_0x005a;
    L_0x0081:
        r4 = r4 + 1;
        goto L_0x0042;
    L_0x0084:
        r10 = new java.io.BufferedOutputStream;	 Catch:{ Exception -> 0x00bc }
        r12 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00bc }
        r12.<init>(r3);	 Catch:{ Exception -> 0x00bc }
        r10.<init>(r12);	 Catch:{ Exception -> 0x00bc }
        r12 = new org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;	 Catch:{ Exception -> 0x00b8, all -> 0x00b4 }
        r12.<init>();	 Catch:{ Exception -> 0x00b8, all -> 0x00b4 }
        r12.updateExifMetadataLossless(r11, r10, r0);	 Catch:{ Exception -> 0x00b8, all -> 0x00b4 }
        r10.close();	 Catch:{ Exception -> 0x00b8, all -> 0x00b4 }
        r12 = r11.delete();	 Catch:{ Exception -> 0x00b8, all -> 0x00b4 }
        if (r12 == 0) goto L_0x00a2;
    L_0x009f:
        r3.renameTo(r11);	 Catch:{ Exception -> 0x00b8, all -> 0x00b4 }
    L_0x00a2:
        if (r10 == 0) goto L_0x00a7;
    L_0x00a4:
        r10.close();	 Catch:{ IOException -> 0x00a7 }
    L_0x00a7:
        if (r3 == 0) goto L_0x00b2;
    L_0x00a9:
        r10 = r3.exists();
        if (r10 == 0) goto L_0x00b2;
    L_0x00af:
        r3.delete();
    L_0x00b2:
        r10 = 1;
        return r10;
    L_0x00b4:
        r11 = move-exception;
        r1 = r10;
        r10 = r11;
        goto L_0x00d8;
    L_0x00b8:
        r11 = move-exception;
        r1 = r10;
        r10 = r11;
        goto L_0x00c3;
    L_0x00bc:
        r10 = move-exception;
        goto L_0x00c3;
    L_0x00be:
        r10 = move-exception;
        r3 = r1;
        goto L_0x00d8;
    L_0x00c1:
        r10 = move-exception;
        r3 = r1;
    L_0x00c3:
        r10.printStackTrace();	 Catch:{ all -> 0x00d7 }
        if (r1 == 0) goto L_0x00cb;
    L_0x00c8:
        r1.close();	 Catch:{ IOException -> 0x00cb }
    L_0x00cb:
        if (r3 == 0) goto L_0x00d6;
    L_0x00cd:
        r10 = r3.exists();
        if (r10 == 0) goto L_0x00d6;
    L_0x00d3:
        r3.delete();
    L_0x00d6:
        return r2;
    L_0x00d7:
        r10 = move-exception;
    L_0x00d8:
        if (r1 == 0) goto L_0x00dd;
    L_0x00da:
        r1.close();	 Catch:{ IOException -> 0x00dd }
    L_0x00dd:
        if (r3 == 0) goto L_0x00e8;
    L_0x00df:
        r11 = r3.exists();
        if (r11 == 0) goto L_0x00e8;
    L_0x00e5:
        r3.delete();
    L_0x00e8:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.util.FileUtil.copyExifData(java.io.File, java.io.File, java.util.List):boolean");
    }

    private static TiffOutputSet getSanselanOutputSet(File file, int i) throws IOException, ImageReadException, ImageWriteException {
        TiffImageMetadata exif;
        JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) Sanselan.getMetadata(file);
        TiffOutputSet tiffOutputSet = null;
        if (jpegImageMetadata != null) {
            exif = jpegImageMetadata.getExif();
            if (exif != null) {
                tiffOutputSet = exif.getOutputSet();
            }
        } else {
            exif = null;
        }
        if (tiffOutputSet == null) {
            if (exif != null) {
                i = exif.contents.header.byteOrder;
            }
            tiffOutputSet = new TiffOutputSet(i);
        }
        return tiffOutputSet;
    }

    private static TiffOutputDirectory getOrCreateExifDirectory(TiffOutputSet tiffOutputSet, TiffOutputDirectory tiffOutputDirectory) {
        TiffOutputDirectory findDirectory = tiffOutputSet.findDirectory(tiffOutputDirectory.type);
        if (findDirectory != null) {
            return findDirectory;
        }
        findDirectory = new TiffOutputDirectory(tiffOutputDirectory.type);
        try {
            tiffOutputSet.addDirectory(findDirectory);
            return findDirectory;
        } catch (ImageWriteException unused) {
            return null;
        }
    }
}
