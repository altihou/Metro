package com.accelerator.metro.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Nicholas on 2016/4/23.
 */
public class SDCardUtil {

    private SDCardUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return 是否存在SD卡
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取系统存储路径
     *
     * @return 系统路径
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


}
