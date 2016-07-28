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
import android.widget.LinearLayout;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.ModifyPayPwdContract;
import com.accelerator.metro.presenter.ModifyPayPwdPresenter;
import com.accelerator.metro.utils.CipherUtil;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPayPwdActivity extends BaseDialogActivity implements ModifyPayPwdContract.View {

    private static final String TAG = ModifyPayPwdActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.loading_view)
    LinearLayout loadingView;
    @Bind(R.id.modify_pay_old_pwd)
    TextInputLayout edtOldPwd;
    @Bind(R.id.modify_pay_new_pwd1)
    TextInputLayout edtNewPwd1;
    @Bind(R.id.modify_pay_new_pwd2)
    TextInputLayout edtNewPwd2;
    @Bind(R.id.modify_pay_commit)
    Button btnPayCommit;
    @Bind(R.id.include_modify_pay_pwd)
    LinearLayout includeModifyPayPwd;
    @Bind(R.id.modify_pay_add_new_pwd1)
    TextInputLayout edtAddNewPwd1;
    @Bind(R.id.modify_pay_add_new_pwd2)
    TextInputLayout edtAddNewPwd2;
    @Bind(R.id.modify_add_commit)
    Button btnAddCommit;
    @Bind(R.id.include_modify_pay_add)
    LinearLayout includeModifyPayAdd;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private ModifyPayPwdPresenter payPwdPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pay_pwd);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        payPwdPresenter = new ModifyPayPwdPresenter(this);
        payPwdPresenter.checkPayPwd();

        if (isVisibility(includeModifyPayPwd)) {
            includeModifyPayPwd.setVisibility(View.GONE);
        }

        if (isVisibility(includeModifyPayAdd)) {
            includeModifyPayAdd.setVisibility(View.GONE);
        }

        if (!isVisibility(loadingView)) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void reLogin() {
        setDialogDismiss();
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void oldPwdError() {
        Snackbar.make(coordinatorLayout, R.string.modify_pay_old_pwd_error, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.modify_add_commit)
    public void onAddBtnClick(View view) {

        EditText new1 = edtAddNewPwd1.getEditText();
        EditText new2 = edtAddNewPwd2.getEditText();

        String newPwd1 = new1 != null ? new1.getText().toString().trim() : null;
        String newPwd2 = new2 != null ? new2.getText().toString().trim() : null;

        if (checkNull(newPwd1) || checkNull(newPwd2)) {
            Snackbar.make(coordinatorLayout, R.string.modify_pay_not_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!checkEquals(newPwd1, newPwd2)) {
            Snackbar.make(coordinatorLayout, R.string.modify_pay_not_equals, Snackbar.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String phone = spf.getString(Config.USER_PHONE, "");

        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();

        payPwdPresenter.modifyPayPwd("", CipherUtil.base64Encode(phone, newPwd1)
                , CipherUtil.base64Encode(phone, newPwd2));

    }

    @OnClick(R.id.modify_pay_commit)
    public void onModifyBtnClick(View view) {

        EditText old = edtOldPwd.getEditText();
        EditText new1 = edtNewPwd1.getEditText();
        EditText new2 = edtNewPwd2.getEditText();

        String oldPwd = old != null ? old.getText().toString().trim() : null;
        String newPwd1 = new1 != null ? new1.getText().toString().trim() : null;
        String newPwd2 = new2 != null ? new2.getText().toString().trim() : null;

        if (checkNull(newPwd1) || checkNull(newPwd2) || checkNull(oldPwd)) {
            Snackbar.make(coordinatorLayout, R.string.modify_pay_not_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!checkEquals(newPwd1, newPwd2)) {
            Snackbar.make(coordinatorLayout, R.string.modify_pay_not_equals, Snackbar.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String phone = spf.getString(Config.USER_PHONE, "");

        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();

        payPwdPresenter.modifyPayPwd(CipherUtil.base64Encode(phone, oldPwd)
                , CipherUtil.base64Encode(phone, newPwd1)
                , CipherUtil.base64Encode(phone, newPwd2));
    }

    private boolean checkNull(String str) {
        return TextUtils.isEmpty(str);
    }

    private boolean checkEquals(String str1, String str2) {
        return str1.equals(str2);
    }

    @Override
    public void checkSucceed(ResultCode code) {

        switch (code.getIs_ok()) {

            case 1:
                toolbar.setTitle(R.string.modify_pay_add);
                if (isVisibility(includeModifyPayPwd)) {
                    includeModifyPayPwd.setVisibility(View.GONE);
                }

                if (isVisibility(loadingView)) {
                    loadingView.setVisibility(View.GONE);
                }

                if (!isVisibility(includeModifyPayAdd)) {
                    includeModifyPayAdd.setVisibility(View.VISIBLE);
                }

                break;
            case -1:
                toolbar.setTitle(R.string.modify_pay_pwd);
                if (isVisibility(includeModifyPayAdd)) {
                    includeModifyPayAdd.setVisibility(View.GONE);
                }

                if (isVisibility(loadingView)) {
                    loadingView.setVisibility(View.GONE);
                }

                if (!isVisibility(includeModifyPayPwd)) {
                    includeModifyPayPwd.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    private boolean isVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        payPwdPresenter.unSubscription();
    }

    @Override
    public void checkFailure(String err) {
        Log.e(TAG, err);
        if (isVisibility(loadingView)) {
            loadingView.setVisibility(View.GONE);
        }
        ToastUtil.Short(R.string.FAILURE);
    }

    @Override
    public void checkCompleted() {
        if (isVisibility(loadingView)) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSucceed(ResultCode values) {
        ToastUtil.Short(R.string.modify_pay_succeed);
        finish();
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        setDialogDismiss();
        Snackbar.make(coordinatorLayout, R.string.modify_pay_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted() {
        setDialogDismiss();
    }


}
