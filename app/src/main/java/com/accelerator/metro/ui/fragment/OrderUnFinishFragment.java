package com.accelerator.metro.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.adapter.UnFinishOrderAdapter;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.OrderContract;
import com.accelerator.metro.presenter.OrderPresenter;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.ui.activity.MainActivity;
import com.accelerator.metro.ui.activity.Password2PayActivity;
import com.accelerator.metro.ui.activity.PayOrderActivity;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zoom on 2016/5/19.
 */
public class OrderUnFinishFragment extends Fragment
        implements OrderContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = OrderUnFinishFragment.class.getName();
    private static final int REQUEST_CODE = 0;

    public static final String PAY_TYPE = "pay_type";
    public static final int NORMAL = 0;
    public static final int UN_NORMAL = 1;

    public static final String ACTION_NAME_REFRESH = "un_finish_refresh";

    @Bind(R.id.include_order_empty_view)
    LinearLayout emptyView;
    @Bind(R.id.un_finish_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private OrderPresenter presenter;
    private UnFinishOrderAdapter adapter;

    private LocalBroadcastManager localBroadcastManager;

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

        adapter = new UnFinishOrderAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setButtonClickListener(new UnFinishOrderAdapter.ButtonClickListener() {
            @Override
            public void onCancelClick(final Order.ElseInfoBean info) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.un_finish_order_cancel_order);
                dialog.setMessage(R.string.un_finish_order_cancel_order_ok);
                dialog.setPositiveButton(R.string.SURE, (dialogInterface, i) -> {

                    Intent intent = new Intent(MainActivity.ACTION_NAME_SHOW);
                    getActivity().sendBroadcast(intent);
                    presenter.cancelOrder(info.getOrder_sn());
                });
                dialog.setNegativeButton(R.string.CANCEL, null);
                dialog.show();
            }

            @Override
            public void onPayClick(Order.ElseInfoBean info) {
                Intent intent = new Intent(getActivity(), Password2PayActivity.class);
                intent.putExtra(PayOrderActivity.PAY_ORDER_NUM, info.getOrder_sn());
                if (info.getOrder_type() == 7 && info.getIs_complete() == 1) {
                    intent.putExtra(PAY_TYPE, UN_NORMAL);
                } else {
                    intent.putExtra(PAY_TYPE, NORMAL);
                }
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        });

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_REFRESH);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_NAME_REFRESH:
                    swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                    break;
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
                break;
        }
    }

    private boolean isVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        presenter.unSubscription();
    }

    @Override
    public void onRefresh() {
        presenter.getOrder("1", "1");
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void noOrder(Order order) {
        if (isVisibility(recyclerView)) {
            recyclerView.setVisibility(View.GONE);
        }
        if (!isVisibility(emptyView)) {
            emptyView.setVisibility(View.VISIBLE);
        }

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, order.getUser_id());
        editor.putString(Config.USER_SESSION, order.getSession_id());

        editor.apply();
    }

    @Override
    public void cancelCompleted() {
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
        ToastUtil.Short(R.string.un_finish_order_cancel_succeed);
    }

    @Override
    public void cancelFailure(String err) {
        Log.e(TAG, err);
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
        ToastUtil.Short(R.string.un_finish_order_cancel_failure);
    }

    @Override
    public void cancelError() {
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
        ToastUtil.Short(R.string.un_finish_order_cancel_failure);
    }

    @Override
    public void cancelSucceed(ResultCode resultCode) {
        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, resultCode.getUser_id());
        editor.putString(Config.USER_SESSION, resultCode.getSession_id());

        editor.apply();

        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onSucceed(Order values) {

        if (isVisibility(emptyView)) {
            emptyView.setVisibility(View.GONE);
        }

        if (!isVisibility(recyclerView)) {
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.onRefresh(values.getElse_info());
        adapter.notifyDataSetChanged();

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, values.getUser_id());
        editor.putString(Config.USER_SESSION, values.getSession_id());

        editor.apply();
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
