package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.adapter.ExpenseCalendarAdapter;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.contract.ExpenseCalendarContract;
import com.accelerator.metro.presenter.ExpenseCalendarPresenter;
import com.accelerator.metro.utils.ToastUtil;
import com.accelerator.metro.widget.DividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExpenseCalendarActivity extends AppCompatActivity
        implements ExpenseCalendarContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ExpenseCalendarActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private ExpenseCalendarPresenter presenter;
    private ExpenseCalendarAdapter adapter;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_calendar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        presenter = new ExpenseCalendarPresenter(this);
        initViews();
    }

    private void initViews() {

        adapter = new ExpenseCalendarAdapter(this);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.googleColorRed,
                R.color.googleColorGreen,
                R.color.googleColorYellow,
                R.color.googleColorBlue);
        swipeRefreshLayout.setOnRefreshListener(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                boolean isBottom = lastVisibleItem >= totalItemCount - 2;
                //lastVisibleItem >= totalItemCount - 2 表示剩下2个item时自动加载
                // dy>0 表示向下滑动
                if (isBottom && dy > 0 && !swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                    loadMore();
                }

            }
        });

    }

    private void loadMore() {
        page++;
        presenter.getRechargeOrder(String.valueOf(page), ExpenseCalendarContract.MORE);
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void empty() {
        Snackbar.make(coordinatorLayout, R.string.expense_calendar_empty, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loadMoreSucceed(Order order) {
        if (order.getElse_info() != null) {
            adapter.onLoadMore(order.getElse_info());
            adapter.notifyItemInserted(adapter.getItemCount());
        }
    }

    @Override
    public void onSucceed(Order values) {

        SharedPreferences sp = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Config.USER_ID, values.getUser_id());
        editor.putString(Config.USER_SESSION, values.getSession_id());
        editor.apply();

        adapter.onRefresh(values.getElse_info());
        adapter.notifyDataSetChanged();

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
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscription();
    }

    @Override
    public void onRefresh() {
        presenter.getRechargeOrder("1", ExpenseCalendarContract.REFRESH);
    }
}
