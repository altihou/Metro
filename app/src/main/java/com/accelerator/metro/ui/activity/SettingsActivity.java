package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.SignOutContract;
import com.accelerator.metro.presenter.SignOutPresenter;
import com.accelerator.metro.utils.NFCUtil;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseDialogActivity implements SignOutContract.View {

    private static final String TAG = SettingsActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.settings_exit)
    Button settingsExit;
    @Bind(R.id.settings_modify_login_pwd)
    RelativeLayout settingsModifyLoginPwd;
    @Bind(R.id.settings_modify_pay_pwd)
    RelativeLayout settingsModifyPayPwd;
    @Bind(R.id.settings_nfc)
    RelativeLayout settingsNfc;

    private SignOutPresenter presenter;

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
        presenter = new SignOutPresenter(this);
    }

    @OnClick(R.id.settings_modify_login_pwd)
    public void onModifyLoginPwdClick(View view) {
        startActivity(new Intent(this, ModifyLoginPwdActivity.class));
    }

    @OnClick(R.id.settings_modify_pay_pwd)
    public void onModifyPayPwdClick(View view) {
        startActivity(new Intent(this, ModifyPayPwdActivity.class));
    }

    @OnClick(R.id.settings_nfc)
    public void onNFCClick(View view) {
        if (NFCUtil.hasNFC(this)) {
            startActivity(new Intent(this, NFCSettingsActivity.class));
        } else {
            ToastUtil.Short(R.string.nfc_not_had);
        }
    }

    @OnClick(R.id.settings_exit)
    public void exitBtnClick(View view) {
        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();
        presenter.signOut();
    }

    @Override
    public void onSucceed(ResultCode values) {
        if (values.getIs_ok() == 1) {
            finish();
            Intent main = new Intent(MainActivity.ACTION_NAME_FINIFSH);
            sendBroadcast(main);

            SharedPreferences sp= MetroApp.getContext().getSharedPreferences(Config.FIRST, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();

            editor.putBoolean(Config.FIRST_TIME,true);

            editor.apply();

            startActivity(new Intent(this, SplashActivity.class));
        } else {
            Log.e(TAG, "退出错误，错误码：" + values.getIs_ok());
        }
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        ToastUtil.Short(R.string.FAILURE);
        setDialogDismiss();
    }

    @Override
    public void onCompleted() {
        setDialogDismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscription();
    }
}
