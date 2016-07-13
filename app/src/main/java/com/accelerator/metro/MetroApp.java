package com.accelerator.metro;

import android.app.Application;
import android.content.Context;

/**
 * Created by zoom on 2016/5/6.
 */
public class MetroApp extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
