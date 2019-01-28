package io.gphotos.gin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtil {
    private static List<String> formats = Arrays.asList(new String[]{"yyyyMMdd'T'HHmmss", "yyyyMMdd'T'HHmmss.SSS", "yyyyMMdd'T'HHmmss.SSST"});

    public static long getTimeOfDateStr(String str) {
        for (String simpleDateFormat : formats) {
            try {
                return new SimpleDateFormat(simpleDateFormat, Locale.getDefault()).parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static String getTimeOfDateString(long j) {
        return new SimpleDateFormat((String) formats.get(0), Locale.getDefault()).format(new Date(j));
    }
}
