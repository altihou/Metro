package com.accelerator.metro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.ModifyLoginPwdContract;
import com.accelerator.metro.presenter.ModifyLoginPwdPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLoginPwdActivity extends BaseDialogActivity implements ModifyLoginPwdContract.View {

    private static final String TAG=ModifyLoginPwdActivity.class.getName();

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

    private ModifyLoginPwdPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_pwd);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presenter=new ModifyLoginPwdPresenter(this);
    }

    @OnClick(R.id.modify_login_commit)
    public void onSaveClick(View view){

        EditText oldEdit=oldPwd.getEditText();
        EditText newEdit1=newPwd1.getEditText();
        EditText newEdit2=newPwd2.getEditText();

        String old= oldEdit != null ? oldEdit.getText().toString().trim() : null;
        String new1= newEdit1 != null ? newEdit1.getText().toString() : null;
        String new2= newEdit2 != null ? newEdit2.getText().toString() : null;

        if (!checkNotNull(old)){

        }

        if (!checkNotNull(old)){

        }

        if (!checkNotNull(old)){

        }


    }


    private boolean checkNotNull(String str){
        return !TextUtils.isEmpty(str);
    }

    private boolean checkEquals(String str1,String str2){
        return str1.equals(str2);
    }

    @Override
    public void reLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        dialog.dismiss();
    }

    @Override
    public void onSucceed(ResultCode values) {

    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG,err);
        dialog.dismiss();
    }

    @Override
    public void onCompleted() {
        dialog.dismiss();
    }
}
