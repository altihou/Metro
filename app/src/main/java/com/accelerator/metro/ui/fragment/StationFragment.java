package com.accelerator.metro.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.accelerator.metro.Config;
import com.accelerator.metro.JavaScriptListener;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.bean.CommitOrder;
import com.accelerator.metro.contract.CommitOrderContract;
import com.accelerator.metro.presenter.CommitOrderPresenter;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.ui.activity.MainActivity;
import com.accelerator.metro.ui.activity.PayOrderActivity;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zoom on 2016/5/4.
 */
public class StationFragment extends Fragment implements CommitOrderContract.View {

    private static final String TAG = StationFragment.class.getName();
    public static final String ORDER_NUM = "order_num";
    public static final String ORDER_PRICE = "order_price";
    public static final String ORDER_START = "order_start";
    public static final String ORDER_END = "order_end";
    public static final String ORDER_TIME = "time";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.web_view)
    WebView webView;

    private boolean start = false;
    private boolean end = false;

    private String startStation;
    private String endStation;
    private String startId;
    private String endId;

    private String startPriceId;
    private String endPriceId;

    private String price;

    private CommitOrderPresenter presenter;

    public static StationFragment newInstance() {
        return new StationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.bottombar_tab1);
        initViews();
        return view;
    }

    private void initViews() {

        presenter = new CommitOrderPresenter(this);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        webView.loadUrl("file:///android_asset/index.html");

        JavaScriptListener object = new JavaScriptListener(getActivity());
        webView.addJavascriptInterface(object, "android");
        object.setPointClickListener(new JavaScriptListener.onPointClickListener() {
            @Override
            public void onClick(String name, String id, String priceId, int type) {
                switch (type) {
                    case JavaScriptListener.TYPE_CODE_START:
                        start = true;
                        startStation = name;
                        startId = id;
                        startPriceId=priceId;
                        if (end && !endStation.equals(startStation)) {
                            Log.e(TAG, "起点 终点:" + startStation + "-" + endStation);
                            commitOrder(startStation, endStation, startId, endId);
                            end = false;
                            start = false;
                        } else {
                            ToastUtil.Short(R.string.station_end);
                        }
                        break;
                    case JavaScriptListener.TYPE_CODE_END:
                        end = true;
                        endStation = name;
                        endId = id;
                        endPriceId=priceId;
                        if (start && !startStation.equals(endStation)) {
                            Log.e(TAG, "起点 终点:" + startStation + "-" + endStation);
                            commitOrder(startStation, endStation, startId, endId);
                            end = false;
                            start = false;
                        } else {
                            ToastUtil.Short(R.string.station_start);
                        }
                        break;
                }
            }
        });
    }

    private void commitOrder(String start, String end, final String sId, final String eId) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.station_commit_order)
                .setMessage("当前选择的站点为 " + start + " - " + end + " 你确定要提交订单吗?")
                .setCancelable(false)
                .setNegativeButton(R.string.CANCEL, null)
                .setPositiveButton(R.string.SURE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.ACTION_NAME_SHOW);
                        getActivity().sendBroadcast(intent);
                        price=String.valueOf(XiAnTicket(Integer.parseInt(startPriceId),Integer.parseInt(endPriceId)));
                        presenter.commitOrder(sId,eId,price);
                    }
                });
        dialog.show();
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.unSubscription();
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void existNotPayOrder() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.station_commit_failure);
        dialog.setMessage(R.string.station_commit_attention);
        dialog.setPositiveButton(R.string.SURE, null);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onSucceed(CommitOrder values) {

        CommitOrder.ElseInfoBean info = values.getElse_info();

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, values.getUser_id());
        editor.putString(Config.USER_SESSION, values.getSession_id());

        editor.apply();

        Intent intent = new Intent(getActivity(), PayOrderActivity.class);
        intent.putExtra(ORDER_PRICE, price);
        intent.putExtra(ORDER_NUM, info.getOrder_sn());
        intent.putExtra(ORDER_TIME, info.getTime());
        intent.putExtra(ORDER_START, startStation);
        intent.putExtra(ORDER_END, endStation);
        startActivity(intent);
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onCompleted() {
        Intent intent = new Intent(MainActivity.ACTION_NAME_HIDE);
        getActivity().sendBroadcast(intent);
    }


    /**
     * 计算价格
     * @param startId 起点站id
     * @param endId 终点站id
     * @return 价格
     */
    private int XiAnTicket(int startId, int endId) {

        int stationCount = XiAnSection(startId, endId);

        if (stationCount <= 6) {
            return 2;
        } else if (stationCount <= 10 && stationCount > 6) {
            return 3;
        } else if (stationCount <= 16 && stationCount > 10) {
            return 4;
        } else if (stationCount > 17) {
            return 5;
        }

        return -1;

    }

    /**
     * 计算站点
     * @param startId 起点站id
     * @param endId 终点站id
     * @return 经过的站点数
     */
    private int XiAnSection(int startId, int endId) {

        if (startId < 20 && endId < 20) {
            return Math.abs(startId - endId);
        }

        if (startId >= 20 && endId >= 20 && startId <= 40 && endId <= 40) {
            return Math.abs(startId - endId);
        } else {

            if (startId <= 19) {
                int a = Math.abs(startId - 10);
                int b = Math.abs(endId - 29);
                return a + b;
            } else {
                int a = Math.abs(startId - 29);
                int b = Math.abs(endId - 10);
                return a + b;
            }

        }

    }

}
