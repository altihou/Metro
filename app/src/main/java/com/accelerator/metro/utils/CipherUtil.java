package com.accelerator.metro.utils;


import android.util.Base64;

/**
 * 加密工具类
 * <p/>
 * Created by Nicholas on 2016/7/17.
 */
public class CipherUtil {

    private CipherUtil() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * Base64加密
     *
     * @param phone 手机号
     * @param str   需要加密的内容
     * @return 加密后的字符串
     */
    public static String base64Encode(String phone, String str) {

        int times = Integer.parseInt(phone.substring(0,10)) % 4 + 2;

//        try {
//
//            for (int i = 0; i < times; i++) {
//
//                byte[] code = str.getBytes("utf-8");
//                str = new String(Base64.encode(code,0,code.length,Base64.DEFAULT),"utf-8");
//
//                Log.e("CipherUtil", "Base64_Str" + "第" + i + "次：" + str);
//            }
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


        for (int i = 0; i < times; i++) {
           str= Base64.encodeToString(str.getBytes(),Base64.DEFAULT);
        }

        return str;
    }

}
