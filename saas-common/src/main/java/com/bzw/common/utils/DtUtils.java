package com.bzw.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yanbin
 */

public class DtUtils {

    public static String toString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
