package com.accelerator.metro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.accelerator.metro.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.settings_modify_login_pwd)
    LinearLayout modifyLoginPwd;
    @Bind(R.id.settings_modify_pay_pwd)
    LinearLayout modifyPayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.settings_modify_login_pwd)
    public void onModifyLoginPwdClick(View view){
        startActivity(new Intent(this,ModifyLoginPwdActivity.class));
    }

    @OnClick(R.id.settings_modify_pay_pwd)
    public void onModifyPayPwdClick(View view){

    }

}
