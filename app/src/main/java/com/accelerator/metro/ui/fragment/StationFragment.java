package com.accelerator.metro.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.accelerator.metro.JavaScriptListener;
import com.accelerator.metro.R;
import com.accelerator.metro.ui.activity.SearchActivity;
import com.accelerator.metro.utils.ToastUtil;
import com.accelerator.metro.utils.XiAnTicketUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zoom on 2016/5/4.
 */
public class StationFragment extends Fragment {

    private static final String TAG = StationFragment.class.getName();
    public static final String ORDER_NUM = "order_num";
    public static final String ORDER_PRICE = "order_price";
    public static final String ORDER_START = "order_start";
    public static final String ORDER_END = "order_end";
    public static final String ORDER_TIME = "time";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
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

    private int price;

    public static StationFragment newInstance() {
        return new StationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {

        toolbar.setTitle(R.string.bottombar_tab1);
        toolbar.inflateMenu(R.menu.menu_station);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_station_search) {
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                }
                return true;
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        if (Build.VERSION.SDK_INT > 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        webView.setWebChromeClient(new MyWebChromeClient());

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
                        startPriceId = priceId;
                        if (end && !endStation.equals(startStation)) {
                            Log.e(TAG, "起点 终点:" + startStation + "-" + endStation);
                            showDialog(startStation, endStation, startId, endId);
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
                        endPriceId = priceId;
                        if (start && !startStation.equals(endStation)) {
                            Log.e(TAG, "起点 终点:" + startStation + "-" + endStation);
                            showDialog(startStation, endStation, startId, endId);
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


    private class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.e(TAG,"progress:"+newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    private void showDialog(String start, String end, String sId, String eId) {

        Log.e(TAG, "SSSSSSSSTATION:" + start + " " + sId + "," + end + " " + eId);

        price = XiAnTicketUtil.XiAnTicket(Integer.parseInt(startPriceId), Integer.parseInt(endPriceId));
        TicketPickerDialogFragment dialog = TicketPickerDialogFragment.newInstance(start, end, sId, eId, price,
                TicketPickerDialogFragment.TYPE_STATION);
        dialog.show(getChildFragmentManager(), "TicketPickerDialogFragment");
        dialog.setCancelable(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



}
