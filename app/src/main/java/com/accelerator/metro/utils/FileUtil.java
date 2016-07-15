package com.accelerator.metro.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nicholas on 2016/7/14.
 */
public class FileUtil {

    private FileUtil() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    public static File createImageFile() {
        // Create an image file name
        String timeStamp = DateUtil.getNowTime2Save();
        String imageFileName = "METRO_" + timeStamp + "_";
        try {
            File imageFile = File.createTempFile(imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    Environment.getExternalStorageDirectory()      /* directory */);
            return imageFile;
        } catch (IOException e) {
            return null;
        }
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
}
