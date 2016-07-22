package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.accelerator.metro.bean.Recharge;
import com.accelerator.metro.contract.RechargeContract;
import com.accelerator.metro.presenter.RechargePresenter;
import com.accelerator.metro.utils.RxBus;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseDialogActivity implements RechargeContract.View {

    private static final String TAG = RechargeActivity.class.getName();
    public static final String REFRESH = "recharge_refresh";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recharge_edit_money)
    EditText rechargeEditMoney;
    @Bind(R.id.recharge_btn_ok)
    Button rechargeBtnOk;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private RechargePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.recharge);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        presenter = new RechargePresenter(this);
    }

    @OnClick(R.id.recharge_btn_ok)
    public void onOkClick(View view) {

        String money = rechargeEditMoney.getText().toString().trim();

        if (TextUtils.isEmpty(money)) {
            Snackbar.make(coordinatorLayout, R.string.recharge_input_money, Snackbar.LENGTH_SHORT).show();
            return;
        }

        setDialogMsg(R.string.WAIT);
        dialog.show();

        presenter.recharge(money);

    }

    @Override
    public void reLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        dialog.dismiss();
    }

    @Override
    public void onSucceed(Recharge values) {

        SharedPreferences spf = MetroApp.getContext()
                .getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_MONEY, values.getElse_info().getMoney());
        editor.putBoolean(Config.USER_REFRESH, true);

        editor.apply();
        RxBus.getDefault().post(REFRESH);
        ToastUtil.Short(R.string.recharge_succeed);

        finish();
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        dialog.dismiss();
    }

    @Override
    public void onCompleted() {
        dialog.dismiss();
        Snackbar.make(coordinatorLayout, R.string.recharge_failure, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscription();
    }
}
