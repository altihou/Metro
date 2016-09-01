package com.accelerator.metro.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.adapter.FinishOrderAdapter;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.FinishOrderContract;
import com.accelerator.metro.presenter.FinishOrderPresenter;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.ui.activity.MainActivity;
import com.accelerator.metro.ui.activity.QRCodeActivity;
import com.accelerator.metro.utils.ToastUtil;
import com.accelerator.metro.widget.OrderExpandableListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nicholas on 2016/7/19.
 */
public class OrderFinishFragment extends Fragment implements FinishOrderContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = OrderFinishFragment.class.getName();
    public static final String ORDER_NUM = "order_num";
    public static final String START_ID = "start_id";
    public static final String END_ID = "end_id";
    public static final String ORDER_PRICE = "order_price";

    public static final String ACTION_NAME_REFRESH = "finish_refresh";

    @Bind(R.id.order_finish_expandableListView)
    OrderExpandableListView expandableListView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.ll_empty)
    LinearLayout llEmpty;
    @Bind(R.id.divider)
    View dividerView;

    private FinishOrderPresenter presenter;
    private FinishOrderAdapter adapter;
    private LocalBroadcastManager localBroadcastManager;

    public static OrderFinishFragment newInstance() {
        return new OrderFinishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_finish, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {

        presenter = new FinishOrderPresenter(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.googleColorRed,
                R.color.googleColorGreen,
                R.color.googleColorYellow,
                R.color.googleColorBlue);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new FinishOrderAdapter(getActivity());

        adapter.setButtonClickListener(new FinishOrderAdapter.OnButtonClickListener() {
            @Override
            public void deleteOrder(final String orderSn) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.finish_order_delete);
                dialog.setMessage(R.string.finish_order_delete_check);
                dialog.setPositiveButton(R.string.SURE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.ACTION_NAME_SHOW);
                        getActivity().sendBroadcast(intent);
                        presenter.deletedOrder(orderSn);
                    }
                });
                dialog.setNegativeButton(R.string.CANCEL, null);
                dialog.show();
            }

            @Override
            public void refundOrder(final String orderSn) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.finish_order_refund);
                dialog.setMessage(R.string.finish_order_refund_check);
                dialog.setPositiveButton(R.string.SURE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.ACTION_NAME_SHOW);
                        getActivity().sendBroadcast(intent);
                        presenter.refundOrder(orderSn);
                    }
                });
                dialog.setNegativeButton(R.string.CANCEL, null);
                dialog.show();
            }
        });
        expandableListView.setAdapter(adapter);
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                boolean enable = false;
                if (expandableListView != null && expandableListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = expandableListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = expandableListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    Log.e(TAG, adapter.getDatas().get(groupPosition).get(childPosition).toString());
                    Order.ElseInfoBean info = adapter.getDatas().get(groupPosition).get(childPosition);
                    Intent intent = new Intent(getActivity(), QRCodeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ORDER_NUM, info.getOrder_sn());
                    bundle.putString(START_ID, info.getStart_id());
                    bundle.putString(END_ID, info.getEnd_id());
                    bundle.putString(ORDER_PRICE, info.getOrder_money());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return true;
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_REFRESH);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive");
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        presenter.unSubscription();
    }

    @Override
    public void onSucceed(List<List<Order.ElseInfoBean>> values) {

        Log.e(TAG, values.toString());

        if (values.get(0).size() == 0 && values.get(1).size() == 0) {

            if (!isVisibility(llEmpty)) {
                llEmpty.setVisibility(View.VISIBLE);
            }

            if (isVisibility(dividerView)) {
                dividerView.setVisibility(View.GONE);
            }

            if (isVisibility(expandableListView)) {
                expandableListView.setVisibility(View.GONE);
            }
            return;
        }

        if (isVisibility(llEmpty)) {
            llEmpty.setVisibility(View.GONE);
        }

        if (!isVisibility(expandableListView)) {
            expandableListView.setVisibility(View.VISIBLE);
        }

        if (!isVisibility(dividerView)) {
            dividerView.setVisibility(View.VISIBLE);
        }

        adapter.onRefresh(values);
    }

    private boolean isVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
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

    @Override
    public void onRefresh() {
        presenter.getFinishOrder();
    }

    @Override
    public void onGetNoTripFail(String err) {
        Log.e(TAG, err);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onRefundSucceed(ResultCode resultCode) {
        SharedPreferences sp = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Config.USER_ID, resultCode.getUser_id());
        editor.putString(Config.USER_SESSION, resultCode.getSession_id());
        editor.apply();
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        Intent sendRefresh=new Intent(MineFragment.ACTION_NAME_REFRESH);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sendRefresh);

    }

    @Override
    public void onRefundFailure(String err) {
        Log.e(TAG, err);
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
        ToastUtil.Short(R.string.finish_order_refund_failure);
    }

    @Override
    public void onRefundConplete() {
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
        ToastUtil.Short(R.string.finish_order_refund_succeed);
    }

    @Override
    public void onDeleteSucceed(ResultCode resultCode) {
        SharedPreferences sp = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Config.USER_ID, resultCode.getUser_id());
        editor.putString(Config.USER_SESSION, resultCode.getSession_id());
        editor.apply();
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onDeleteFailure(String err) {
        Log.e(TAG, err);
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
        ToastUtil.Short(R.string.finish_order_delete_failure);
    }

    @Override
    public void onDeleteConplete() {
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
        ToastUtil.Short(R.string.finish_order_delete_succeed);
    }
}
