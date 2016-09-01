package com.accelerator.metro.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nicholas on 2016/7/14.
 */
public class FileUtil {

     FileUtil() {
        throw new RuntimeException("Stub!");
    }

    public static File ImageUriFilePath(){

        String dir;

        if (SDCardUtil.isSDCardEnable()) {
            dir = SDCardUtil.getSDCardPath();
        } else {
            dir = SDCardUtil.getRootDirectoryPath();
        }

        File folder = new File(dir, "METRO");

        //建立文件夹
        if (!folder.exists()) {
            folder.mkdir();
        }

        return new File(folder, "JPG_" + DateUtil.getNowTime2Save() + ".JPG");
    }

    public static File avatarUriPath(){

        File file = new File(Environment.getExternalStorageDirectory(), "avatar.jpg");

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
