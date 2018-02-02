package com.bzw.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yanbin
 */

public class DtUtils {

    private final static DateFormat DAY_NUMBER = new SimpleDateFormat("yyyyMMdd");
    private final static DateFormat TIME_NUMBER = new SimpleDateFormat("yyyyMMddhhmmssSSS");
    private final static DateFormat Time_STRING = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String toDateString(Date date) {
        return Time_STRING.format(date);
    }

    public static String toDayNumber(Date date) {
        return DAY_NUMBER.format(date);
    }

    public static String toTimeNumber(Date date) {
        return TIME_NUMBER.format(date);
    }


}
