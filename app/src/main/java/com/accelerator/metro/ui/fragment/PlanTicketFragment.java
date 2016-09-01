package com.accelerator.metro.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.accelerator.metro.adapter.PlanTicketAdapter;
import com.accelerator.metro.bean.PlanTicket;
import com.accelerator.metro.contract.PlanTicketOrderContract;
import com.accelerator.metro.presenter.PlanTicketOrderPresenter;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nicholas on 2016/8/8.
 */
public class PlanTicketFragment extends Fragment implements PlanTicketOrderContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = PlanTicketFragment.class.getName();

    public static final String ACTION_NAME_REFRESH = "plan_ticket_refresh";

    @Bind(R.id.include_order_empty_view)
    LinearLayout emptyView;
    @Bind(R.id.plan_ticket_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private PlanTicketAdapter adapter;
    private PlanTicketOrderPresenter presenter;

    private LocalBroadcastManager localBroadcastManager;

    public static PlanTicketFragment newInstance() {
        return new PlanTicketFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_ticket, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {

        presenter=new PlanTicketOrderPresenter(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.googleColorRed,
                R.color.googleColorGreen,
                R.color.googleColorYellow,
                R.color.googleColorBlue);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new PlanTicketAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

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

    private boolean isVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void orderEmpty(PlanTicket result) {
        if (isVisibility(recyclerView)) {
            recyclerView.setVisibility(View.GONE);
        }
        if (!isVisibility(emptyView)) {
            emptyView.setVisibility(View.VISIBLE);
        }

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, result.getUser_id());
        editor.putString(Config.USER_SESSION, result.getSession_id());

        editor.apply();
    }

    @Override
    public void onSucceed(PlanTicket values) {

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

    @Override
    public void onRefresh() {
        presenter.getPlanTicket();
    }
}
