package com.accelerator.metro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.accelerator.metro.JavaScriptListener;
import com.accelerator.metro.R;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {

    public static final String START_STATION="start";
    public static final String END_STATION="end";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView webView;

    private boolean start = false;
    private boolean end = false;

    private String startStation;
    private String endStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initViews();

    }

    private void initViews() {

        toolbar.setNavigationOnClickListener(view -> finish());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
       // settings.setDomStorageEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");

        JavaScriptListener object = new JavaScriptListener(this);
        webView.addJavascriptInterface(object, "android");
        object.setPointClickListener((name, id, priceId, type) -> {
            switch (type) {
                case JavaScriptListener.TYPE_CODE_START:
                    start = true;
                    startStation = name;

                    if (end && !endStation.equals(startStation)) {

                        Intent intent=new Intent();
                        intent.putExtra(START_STATION,startStation);
                        intent.putExtra(END_STATION,endStation);
                        setResult(RESULT_OK,intent);
                        finish();
                        end = false;
                        start = false;
                    } else {
                        ToastUtil.Short(R.string.station_end);
                    }
                    break;
                case JavaScriptListener.TYPE_CODE_END:
                    end = true;
                    endStation = name;

                    if (start && !startStation.equals(endStation)) {

                        Intent intent=new Intent();
                        intent.putExtra(START_STATION,startStation);
                        intent.putExtra(END_STATION,endStation);
                        setResult(RESULT_OK,intent);
                        finish();

                        end = false;
                        start = false;
                    } else {
                        ToastUtil.Short(R.string.station_start);
                    }
                    break;
            }
        });

    }

}
