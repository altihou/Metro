package com.accelerator.metro.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accelerator.metro.R;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.OrderContract;
import com.accelerator.metro.presenter.OrderPresenter;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.ui.activity.PayOrderActivity;
import com.accelerator.metro.utils.DateUtil;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zoom on 2016/5/19.
 */
public class OrderUnFinishFragment extends Fragment
        implements OrderContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = OrderUnFinishFragment.class.getName();

    @Bind(R.id.un_finish_order_date)
    TextView tvDate;
    @Bind(R.id.un_finish_order_station_start)
    TextView tvStationStart;
    @Bind(R.id.un_finish_order_station_end)
    TextView tvStationEnd;
    @Bind(R.id.un_finish_order_price)
    TextView tvPrice;
    @Bind(R.id.un_finish_order_btn_cancel)
    Button btnCancel;
    @Bind(R.id.un_finish_order_btn_pay)
    Button btnPay;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.include_order_empty_view)
    LinearLayout emptyView;
    @Bind(R.id.card_view)
    CardView cardView;

    private OrderPresenter presenter;

    private String date;
    private String price;
    private String orderNum;
    private String startStation;
    private String endStation;

    public static OrderUnFinishFragment newInstance() {
        return new OrderUnFinishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_unfinish, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {

        presenter = new OrderPresenter(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.googleColorRed,
                R.color.googleColorGreen,
                R.color.googleColorYellow,
                R.color.googleColorBlue);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @OnClick(R.id.un_finish_order_btn_pay)
    public void onPayClick(View view) {

        Intent intent = new Intent(getActivity(), PayOrderActivity.class);

        intent.putExtra(StationFragment.ORDER_PRICE, price);
        intent.putExtra(StationFragment.ORDER_NUM, orderNum);
        intent.putExtra(StationFragment.ORDER_TIME, date);
        intent.putExtra(StationFragment.ORDER_START, startStation);
        intent.putExtra(StationFragment.ORDER_END, endStation);

        startActivity(intent);
    }

    private boolean isVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }


    @OnClick(R.id.un_finish_order_btn_cancel)
    public void onCancelClick(View view) {

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.un_finish_order_cancel_order);
        dialog.setMessage(R.string.un_finish_order_cancel_order_ok);
        dialog.setPositiveButton(R.string.SURE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.cancelOrder(orderNum);
            }
        });
        dialog.setNegativeButton(R.string.CANCEL,null);
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        presenter.getOrder("1", "1");
    }

    @Override
    public void reLogin() {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void noOrder() {
        if (isVisibility(cardView)) {
            cardView.setVisibility(View.GONE);
        }
        if (!isVisibility(emptyView)) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void cancelCompleted() {
        ToastUtil.Short(R.string.un_finish_order_cancel_succeed);
    }

    @Override
    public void cancelFailure(String err) {
        Log.e(TAG,err);
        ToastUtil.Short(R.string.un_finish_order_cancel_failure);
    }

    @Override
    public void cancelError() {
        ToastUtil.Short(R.string.un_finish_order_cancel_failure);
    }

    @Override
    public void cancelSucceed(ResultCode resultCode) {

    }

    @Override
    public void onSucceed(Order values) {

        if (isVisibility(emptyView)) {
            emptyView.setVisibility(View.GONE);
        }

        if (!isVisibility(cardView)) {
            cardView.setVisibility(View.VISIBLE);
        }

        Order.ElseInfoBean info = values.getElse_info().get(0);

        date = DateUtil.getOrderDate(info.getTime());
        price = info.getOrder_money();
        orderNum = info.getOrder_sn();
        startStation = info.getStart_point();
        endStation = info.getEnd_point();

        tvDate.setText(date);
        tvPrice.setText(price);
        tvStationStart.setText(startStation);
        tvStationEnd.setText(endStation);
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCompleted() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
