package com.accelerator.metro.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.accelerator.metro.JavaScriptListener;
import com.accelerator.metro.R;
import com.accelerator.metro.bean.CommitOrder;
import com.accelerator.metro.contract.CommitOrderContract;
import com.accelerator.metro.presenter.CommitOrderPresenter;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zoom on 2016/5/4.
 */
public class StationFragment extends Fragment implements CommitOrderContract.View {

    private static final String TAG = StationFragment.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.web_view)
    WebView webView;

    private boolean start = false;
    private boolean end = false;

    private String startStation;
    private String startId;
    private String endStation;
    private String endId;

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

        presenter=new CommitOrderPresenter(this);

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
            public void onClick(String name, String id, int type) {

                switch (type) {

                    case 1:

                        start = true;
                        startStation = name;
                        startId = id;

                        if (end && !endStation.equals(startStation)) {

                            Log.e(TAG, "起点 终点:" + startStation + "-" + endStation);
                            commitOrder(startStation,endStation,startId,endId);
                            end = false;
                            start = false;
                        } else {
                            ToastUtil.Short("请选择终点站");
                        }

                        break;

                    case 2:

                        end = true;
                        endStation = name;
                        endId = id;

                        if (start && !startStation.equals(endStation)) {

                            Log.e(TAG, "起点 终点:" + startStation + "-" + endStation);
                            commitOrder(startStation,endStation,startId,endId);
                            end = false;
                            start = false;
                        } else {
                            ToastUtil.Short("请选择起点站");
                        }

                        break;
                }


            }
        });

    }

    private void commitOrder(String start, String end, final String sId, final String eId){

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity())
                .setTitle("提交订单")
                .setMessage("当前选择的站点为 "+start+" - "+end+" 你确定要提交订单吗?")
                .setCancelable(false)
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.commitOrder(sId,eId);
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
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onSucceed(CommitOrder values) {
        Log.e(TAG,values.getElse_info().toString());

    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG,err);
    }

    @Override
    public void onCompleted() {
        Log.e(TAG,"onCompleted");
    }
}
