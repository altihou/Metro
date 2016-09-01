package com.accelerator.metro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.accelerator.metro.R;
import com.accelerator.metro.ui.fragment.MineFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {


    public static final int REQUEST_CODE_LOGIN = 0;
    public static final int REQUEST_CODE_REGISTER = 1;

    @Bind(R.id.logon)
    Button logon;
    @Bind(R.id.register)
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.logon)
    public void onloginClick(View view) {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN);
    }

    @OnClick(R.id.register)
    public void onRegisterClick(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_CODE_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
            case REQUEST_CODE_REGISTER:
                if (resultCode == RESULT_OK) {

                    startActivity(new Intent(GuideActivity.this,MainActivity.class));

                    Intent intent=new Intent(MineFragment.ACTION_NAME_REFRESH);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
