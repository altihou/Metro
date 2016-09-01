package com.accelerator.metro.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nicholas on 2016/7/14.
 */
public class DateUtil {

    DateUtil() {
                /* cannot be instantiated */
        throw new RuntimeException("Stub!");
    }

    public static String getNowTime2Save() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss", Locale.CHINA);
        return sdf.format(new Date());
    }

    public static String getNowTime2Show() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(new Date());
    }

    public static String getOrderDate(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(new Date(time * 1000L));
    }

    public static String getExpenseOrderDate(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(new Date(time * 1000L));
    }

    public static long toLongTimes(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = null;

        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (date != null ? date.getTime() : 0) / 1000L;
    }

}
