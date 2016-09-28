package com.accelerator.metro.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.ui.fragment.TicketPickerDialogFragment;
import com.accelerator.metro.utils.ToastUtil;
import com.accelerator.metro.utils.XiAnTicketUtil;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseDialogActivity {

    private static final String TAG = SearchActivity.class.getName();

    public static final String ACTION_SHOW_DIALOG = "action_show";
    public static final String ACTION_HIDE_DIALOG = "action_hide";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_auto_tv_start_station)
    AppCompatAutoCompleteTextView autoTvStartStation;
    @Bind(R.id.search_auto_tv_end_station)
    AppCompatAutoCompleteTextView autoTvEndStation;
    @Bind(R.id.search_btn_ok)
    Button btnOk;

    private String[] stations;
    private String[] priceId;
    private String[] searchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        init();

    }

    private void init() {

        searchId=MetroApp.getContext().getResources().getStringArray(R.array.search_id);
        stations = MetroApp.getContext().getResources().getStringArray(R.array.search_station);
        priceId = MetroApp.getContext().getResources().getStringArray(R.array.price_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stations);
        autoTvStartStation.setAdapter(adapter);
        autoTvEndStation.setAdapter(adapter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_HIDE_DIALOG);
        intentFilter.addAction(ACTION_SHOW_DIALOG);
        registerReceiver(receiver, intentFilter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_SHOW_DIALOG:
                    setDialogMsg(R.string.WAIT);
                    setDialogCancelable(false);
                    setDialogShow();
                    break;
                case ACTION_HIDE_DIALOG:
                    setDialogDismiss();
                    break;
            }

        }
    };

    @OnClick(R.id.search_btn_ok)
    public void onOKClick(View view) {

        String start = autoTvStartStation.getText().toString().trim();
        String end = autoTvEndStation.getText().toString().trim();

        if (TextUtils.isEmpty(start)) {
            ToastUtil.Short(R.string.search_start_station_not_empty);
            return;
        }

        if (TextUtils.isEmpty(end)) {
            ToastUtil.Short(R.string.search_end_station_not_empty);
            return;
        }

        if (!isExist(stations, start)) {
            ToastUtil.Short(R.string.search_start_station_not_exist);
            return;
        }

        if (!isExist(stations, end)) {
            ToastUtil.Short(R.string.search_end_station_not_exist);
            return;
        }


        int startPos = Arrays.asList(stations).indexOf(start);
        int endPos = Arrays.asList(stations).indexOf(end);

        String startPriceId = priceId[startPos];
        String endPriceId = priceId[endPos];

        String startId = searchId[startPos];
        String endId = searchId[endPos];

        Log.e(TAG, "STATION:" + start + " " + startId + "," + end + " " + endId);

        int price = XiAnTicketUtil.XiAnTicket(Integer.parseInt(startPriceId), Integer.parseInt(endPriceId));

        TicketPickerDialogFragment dialog = TicketPickerDialogFragment.newInstance(start, end, startId, endId, price,
                TicketPickerDialogFragment.TYPE_SEARCH);
        dialog.show(getSupportFragmentManager(), "TicketPickerDialogFragment");
        dialog.setCancelable(false);

    }

    private boolean isExist(String[] strings, String str) {
        return Arrays.asList(strings).contains(str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
