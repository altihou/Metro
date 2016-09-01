package com.accelerator.metro.utils;


import android.util.Base64;

/**
 * 加密工具类
 *
 * Created by Nicholas on 2016/7/17.
 */
public class CipherUtil {

    CipherUtil() {
        throw new RuntimeException("Stub!");
    }

    public static String base64Encode(String phone, String str) {

        int times = Integer.parseInt(phone.substring(0,10)) % 4 + 2;

        for (int i = 0; i < times; i++) {
           str= Base64.encodeToString(str.getBytes(),Base64.DEFAULT);
        }

        return str;
    }

}
