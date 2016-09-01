package com.accelerator.metro.utils;

import android.widget.Toast;

import com.accelerator.metro.MetroApp;


/**
 * Created by Nicholas on 2016/4/24.
 */
public class ToastUtil {

     ToastUtil() {
        throw new RuntimeException("Stub!");
    }

    public static void Short(String msg) {
        Toast.makeText(MetroApp.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void Short(int id) {
        Toast.makeText(MetroApp.getContext(), id, Toast.LENGTH_SHORT).show();
    }

    public static void Long(String msg) {
        Toast.makeText(MetroApp.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void Long(int id) {
        Toast.makeText(MetroApp.getContext(), id, Toast.LENGTH_LONG).show();
    }
}
