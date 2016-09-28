package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.accelerator.metro.Config;

/**
 * Created by Nicholas on 2016/8/9.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //应用秒开
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        SharedPreferences spf = getSharedPreferences(Config.FIRST, Context.MODE_PRIVATE);
        boolean isFirst = spf.getBoolean(Config.FIRST_TIME, true);

        if (isFirst) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }
}
