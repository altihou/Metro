package com.accelerator.metro.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Nicholas on 2016/7/15.
 */
public class CameraUtil {

     CameraUtil() {
        throw new RuntimeException("Stub!");
    }

    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

}
