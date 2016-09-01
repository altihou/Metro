package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
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
import com.accelerator.metro.contract.PayOrderContract;
import com.accelerator.metro.presenter.PayOrderPresenter;
import com.accelerator.metro.ui.fragment.MineFragment;
import com.accelerator.metro.ui.fragment.OrderFinishFragment;
import com.accelerator.metro.ui.fragment.OrderUnFinishFragment;
import com.accelerator.metro.utils.CipherUtil;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Password2PayActivity extends BaseDialogActivity implements PayOrderContract.View {

    private static final String TAG = Password2PayActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.password_btn_ok)
    Button passwordBtnOk;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.edt_pay_pwd)
    EditText edtPwd;

    private PayOrderPresenter presenter;
    private String orderNum;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password2_pay);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            orderNum = intent.getStringExtra(PayOrderActivity.PAY_ORDER_NUM);
            type = intent.getIntExtra(OrderUnFinishFragment.PAY_TYPE, -1);
        }

        presenter = new PayOrderPresenter(this);

    }

    @OnClick(R.id.password_btn_ok)
    public void onOKClick(View view) {

        String password = edtPwd.getText().toString().trim();

        if (TextUtils.isEmpty(password)){
            ToastUtil.Short(R.string.login_not_empty_password);
            return;
        }

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String phone = spf.getString(Config.USER_PHONE, "");

        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();

        switch (type) {

            case OrderUnFinishFragment.NORMAL:
                presenter.payOrder(orderNum, CipherUtil.base64Encode(phone, password), "");
                break;
            case OrderUnFinishFragment.UN_NORMAL:
                presenter.payOrder(orderNum, CipherUtil.base64Encode(phone, password), "8");
                break;

        }

    }

    @Override
    public void reLogin() {
        setDialogDismiss();
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void setPayPwd() {
        setDialogDismiss();
        ToastUtil.Short(R.string.password2_pay_set_pay_pwd);
        startActivity(new Intent(this, ModifyPayPwdActivity.class));
    }

    @Override
    public void orderError() {
        setDialogDismiss();
        ToastUtil.Short(R.string.password2_pay_order_error);
    }

    @Override
    public void payPwdError() {
        setDialogDismiss();
        ToastUtil.Short(R.string.password2_pay_pwd_error);
    }

    @Override
    public void notSufficientFunds() {
        setDialogDismiss();
        ToastUtil.Short(R.string.password2_pay_not_sufficient_funds);
    }

    @Override
    public void onSucceed(ResultCode info) {

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, info.getUser_id());
        editor.putString(Config.USER_SESSION, info.getSession_id());

        editor.apply();

        ToastUtil.Short(R.string.password2_pay_succeed);

        Intent sendRefresh = new Intent(OrderFinishFragment.ACTION_NAME_REFRESH);
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendRefresh);

        Intent sendMineRefresh = new Intent(MineFragment.ACTION_NAME_REFRESH);
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendMineRefresh);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        ToastUtil.Short(R.string.password2_pay_failure);
        setDialogDismiss();
    }

    @Override
    public void onCompleted() {
        setDialogDismiss();
    }
}
