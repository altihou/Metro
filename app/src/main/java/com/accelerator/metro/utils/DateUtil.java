package com.accelerator.metro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nicholas on 2016/7/14.
 */
public class DateUtil {

    private DateUtil() {
                /* cannot be instantiated */
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    public static String getNowTime2Save() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss", Locale.CHINA);
        return sdf.format(new Date());
    }

    public static String getNowTime2Show() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return sdf.format(new Date());
    }

}
