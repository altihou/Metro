package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.OrderContract;
import com.accelerator.metro.presenter.OrderPresenter;
import com.accelerator.metro.ui.fragment.OrderUnFinishFragment;
import com.accelerator.metro.ui.fragment.StationFragment;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayOrderActivity extends BaseDialogActivity implements OrderContract.View {

    private static final String TAG=PayOrderActivity.class.getName();

    public static final int PAY_REQUEST_CODE = 0;
    public static final String PAY_ORDER_NUM = "pay_order_code";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.order_date)
    TextView tvOrderDate;
    @Bind(R.id.order_station_start)
    TextView tvStationStart;
    @Bind(R.id.order_station_end)
    TextView tvStationEnd;
    @Bind(R.id.order_price)
    TextView tvOrderPrice;
    @Bind(R.id.order_btn_pay_now)
    Button orderBtnPayNow;
    @Bind(R.id.order_btn_cancel_order)
    Button orderBtnCancelOrder;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private String orderNum;
    private OrderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            Intent sendRefresh=new Intent(OrderUnFinishFragment.ACTION_NAME_REFRESH);
            LocalBroadcastManager.getInstance(PayOrderActivity.this).sendBroadcast(sendRefresh);
            finish();
        });

        Intent intent = getIntent();

        if (intent != null) {

            orderNum = intent.getStringExtra(StationFragment.ORDER_NUM);
            String orderPrice = intent.getStringExtra(StationFragment.ORDER_PRICE);
            String time = intent.getStringExtra(StationFragment.ORDER_TIME);
            String start = intent.getStringExtra(StationFragment.ORDER_START);
            String end = intent.getStringExtra(StationFragment.ORDER_END);

            tvOrderDate.setText(time);
            tvOrderPrice.setText(orderPrice + "元");
            tvStationStart.setText(start);
            tvStationEnd.setText(end);
        }

        presenter=new OrderPresenter(this);
    }

    @OnClick(R.id.order_btn_pay_now)
    public void onPayNowClick(View view) {
        Intent intent = new Intent(this, Password2PayActivity.class);
        intent.putExtra(PAY_ORDER_NUM, orderNum);
        intent.putExtra(OrderUnFinishFragment.PAY_TYPE,OrderUnFinishFragment.NORMAL);
        startActivityForResult(intent, PAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.order_btn_cancel_order)
    public void onCanaelClick(View view) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle(R.string.un_finish_order_cancel_order);
        dialog.setMessage(R.string.un_finish_order_cancel_order_ok);
        dialog.setPositiveButton(R.string.SURE, (dialogInterface, i) -> {
            setDialogCancelable(false);
            setDialogMsg(R.string.WAIT);
            setDialogShow();
            presenter.cancelOrder(orderNum);
        });
        dialog.setNegativeButton(R.string.CANCEL,null);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent sendRefresh=new Intent(OrderUnFinishFragment.ACTION_NAME_REFRESH);
        LocalBroadcastManager.getInstance(PayOrderActivity.this).sendBroadcast(sendRefresh);
        super.onBackPressed();
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(this, LoginActivity.class));
        setDialogDismiss();
    }

    @Override
    public void noOrder(Order order) {}

    @Override
    public void cancelCompleted() {
        setDialogDismiss();
    }

    @Override
    public void cancelFailure(String err) {
        Log.e(TAG,err);
        ToastUtil.Short(R.string.un_finish_order_cancel_failure);
        setDialogDismiss();
    }

    @Override
    public void cancelError() {
        ToastUtil.Short(R.string.un_finish_order_cancel_failure);
        setDialogDismiss();
    }

    @Override
    public void cancelSucceed(ResultCode resultCode) {

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, resultCode.getUser_id());
        editor.putString(Config.USER_SESSION, resultCode.getSession_id());

        editor.apply();

        ToastUtil.Short(R.string.un_finish_order_cancel_succeed);

        finish();
    }

    @Override
    public void onSucceed(Order values) {}

    @Override
    public void onFailure(String err) {}

    @Override
    public void onCompleted() {}
}
