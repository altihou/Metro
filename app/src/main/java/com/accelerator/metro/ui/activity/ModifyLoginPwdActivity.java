package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.ModifyLoginPwdContract;
import com.accelerator.metro.presenter.ModifyLoginPwdPresenter;
import com.accelerator.metro.utils.CipherUtil;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLoginPwdActivity extends BaseDialogActivity implements ModifyLoginPwdContract.View {

    private static final String TAG = ModifyLoginPwdActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.modify_login_old_pwd)
    TextInputLayout oldPwd;
    @Bind(R.id.modify_login_new_pwd1)
    TextInputLayout newPwd1;
    @Bind(R.id.modify_login_new_pwd2)
    TextInputLayout newPwd2;
    @Bind(R.id.modify_login_commit)
    Button commit;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private ModifyLoginPwdPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_pwd);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        presenter = new ModifyLoginPwdPresenter(this);
    }

    @OnClick(R.id.modify_login_commit)
    public void onSaveClick(View view) {

        EditText oldEdit = oldPwd.getEditText();
        EditText newEdit1 = newPwd1.getEditText();
        EditText newEdit2 = newPwd2.getEditText();

        String old = oldEdit != null ? oldEdit.getText().toString().trim() : null;
        String new1 = newEdit1 != null ? newEdit1.getText().toString() : null;
        String new2 = newEdit2 != null ? newEdit2.getText().toString() : null;

        if (checkNull(old)) {
            Snackbar.make(coordinatorLayout, R.string.modify_login_input_old_pwd, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (checkNull(old)) {
            Snackbar.make(coordinatorLayout, R.string.modify_login_input_new1_pwd, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (checkNull(old)) {
            Snackbar.make(coordinatorLayout, R.string.modify_login_input_new2_pwd, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!checkEquals(new1, new2)) {
            Snackbar.make(coordinatorLayout, R.string.modify_login_pwd_not_equals, Snackbar.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String userName = spf.getString(Config.USER_PHONE, "");

        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();

        presenter.modifyLoginPwd(CipherUtil.base64Encode(userName, old)
                , CipherUtil.base64Encode(userName, new1)
                , CipherUtil.base64Encode(userName, new2));
    }


    private boolean checkNull(String str) {
        return TextUtils.isEmpty(str);
    }

    private boolean checkEquals(String str1, String str2) {
        return str1.equals(str2);
    }

    @Override
    public void reLogin() {
        setDialogDismiss();
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void oldPwdError() {
        setDialogDismiss();
        Snackbar.make(coordinatorLayout, R.string.modify_login_old_pwd_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSucceed(ResultCode info) {

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, info.getUser_id());
        editor.putString(Config.USER_SESSION, info.getSession_id());

        editor.apply();

        ToastUtil.Short(R.string.modify_login_pwd_succeed);
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        setDialogDismiss();
    }

    @Override
    public void onCompleted() {
        setDialogDismiss();
    }
}
