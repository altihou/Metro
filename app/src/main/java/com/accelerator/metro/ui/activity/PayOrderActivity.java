package com.accelerator.metro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.accelerator.metro.R;
import com.accelerator.metro.ui.fragment.StationFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayOrderActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
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
    }

    @OnClick(R.id.order_btn_pay_now)
    public void onPayNowClick(View view) {
        Intent intent = new Intent(this, Password2PayActivity.class);
        intent.putExtra(PAY_ORDER_NUM, orderNum);
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
        // TODO: 2016/7/27 取消订单
    }

}
