package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.PlanTicketContract;
import com.accelerator.metro.presenter.PlanTicketPresenter;
import com.accelerator.metro.ui.fragment.PlanTicketFragment;
import com.accelerator.metro.utils.DateUtil;
import com.accelerator.metro.utils.ToastUtil;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlanTicketActivity extends BaseDialogActivity
        implements DatePickerDialog.OnDateSetListener, PlanTicketContract.View {

    private static final String TAG = PlanTicketActivity.class.getName();

    public static final int TYPE_WEEK = 0;
    public static final int TYPE_MONTH = 1;
    public static final int TYPE_OTHERS = 2;

    public static final int REQUEST_CODE_MAP = 3;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.plan_ticket_auto_tv_start_station)
    AppCompatAutoCompleteTextView autoTvStartStation;
    @Bind(R.id.plan_ticket_auto_tv_end_station)
    AppCompatAutoCompleteTextView autoTvEndStation;
    @Bind(R.id.plan_ticket_rb_week)
    RadioButton rbWeek;
    @Bind(R.id.plan_ticket_rb_month)
    RadioButton rbMonth;
    @Bind(R.id.plan_ticket_rb_others)
    RadioButton rbOthers;
    @Bind(R.id.plan_ticket_rg)
    RadioGroup radioGroup;
    @Bind(R.id.plan_ticket_edt_days)
    EditText edtDays;
    @Bind(R.id.plan_ticket_days_view)
    RelativeLayout daysView;
    @Bind(R.id.plan_ticket_start_time)
    TextView tvStartTime;
    @Bind(R.id.plan_ticket_btn_ok)
    Button btnOk;
    @Bind(R.id.plan_ticket_pick_date_view)
    RelativeLayout pickDateView;

    private String[] stations;
    private String[] searchId;

    private int type = TYPE_WEEK;
    private String longTimes;
    private DatePickerDialog datePickerDialog;
    private PlanTicketPresenter presenter;

    private String startStation;
    private String endStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_ticket);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initViews();
    }

    private void initViews() {

        presenter = new PlanTicketPresenter(this);

        longTimes = String.valueOf(DateUtil.toLongTimes(DateUtil.getNowTime2Show()));
        searchId = MetroApp.getContext().getResources().getStringArray(R.array.search_id);
        stations = MetroApp.getContext().getResources().getStringArray(R.array.search_station);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stations);
        autoTvStartStation.setAdapter(adapter);
        autoTvEndStation.setAdapter(adapter);

        tvStartTime.setText(DateUtil.getNowTime2Show());

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                true);

        rbWeek.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(checkId);
                String t = rb.getText().toString();
                switch (t) {
                    case "周票":
                        if (isVisibility(daysView)) {
                            daysView.setVisibility(View.GONE);
                        }
                        type = TYPE_WEEK;
                        break;
                    case "月票":
                        if (isVisibility(daysView)) {
                            daysView.setVisibility(View.GONE);
                        }
                        type = TYPE_MONTH;
                        break;
                    case "其它":
                        if (!isVisibility(daysView)) {
                            daysView.setVisibility(View.VISIBLE);
                        }
                        type = TYPE_OTHERS;
                        break;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plan_ticket,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menu_plan_ticket_map){
            startActivityForResult(new Intent(this,MapActivity.class),REQUEST_CODE_MAP);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case REQUEST_CODE_MAP:
                if (resultCode==RESULT_OK){
                    startStation=data.getStringExtra(MapActivity.START_STATION);
                    endStation=data.getStringExtra(MapActivity.END_STATION);
                    autoTvStartStation.setText(startStation);
                    autoTvEndStation.setText(endStation);
                }
                break;
        }

    }

    @OnClick(R.id.plan_ticket_pick_date_view)
    public void onPickDateClick(View view) {
        datePickerDialog.setYearRange(1985, 2036);
        datePickerDialog.setVibrate(true);
        datePickerDialog.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    @OnClick(R.id.plan_ticket_btn_ok)
    public void onOKClick(View view) {

        String start = autoTvStartStation.getText().toString().trim();
        String end = autoTvEndStation.getText().toString().trim();

        String days = edtDays.getText().toString().trim();

        if (TextUtils.isEmpty(start)) {
            ToastUtil.Short(R.string.search_start_station_not_empty);
            return;
        }

        if (TextUtils.isEmpty(end)) {
            ToastUtil.Short(R.string.search_end_station_not_empty);
            return;
        }

        if (type == TYPE_OTHERS) {
            if (TextUtils.isEmpty(days)) {
                ToastUtil.Short(R.string.plan_ticket_day_not_empty);
                return;
            }
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

        String startId = searchId[startPos];
        String endId = searchId[endPos];

        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();

        if (type == TYPE_OTHERS) {
            presenter.autoBuy(type, startId, endId, longTimes, days);
        } else {
            presenter.autoBuy(type, startId, endId, longTimes, "");
        }

    }

    private boolean isVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private boolean isExist(String[] strings, String str) {
        return Arrays.asList(strings).contains(str);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String date = year + "-" + (month + 1) + "-" + day;
        tvStartTime.setText(date);
        longTimes = String.valueOf(DateUtil.toLongTimes(date));
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        setDialogDismiss();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void orderExist(ResultCode resultCode) {
        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(Config.USER_ID, resultCode.getUser_id());
        editor.putString(Config.USER_SESSION, resultCode.getSession_id());
        editor.apply();
        ToastUtil.Short(R.string.plan_ticket_exist_failure);
        setDialogDismiss();
    }

    @Override
    public void onSucceed(ResultCode values) {
        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(Config.USER_ID, values.getUser_id());
        editor.putString(Config.USER_SESSION, values.getSession_id());
        editor.apply();
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        setDialogDismiss();
        ToastUtil.Short(R.string.plan_ticket_failure);
    }

    @Override
    public void onCompleted() {
        setDialogDismiss();
        ToastUtil.Short(R.string.plan_ticket_succeed);

        Intent intent = new Intent(PlanTicketFragment.ACTION_NAME_REFRESH);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscription();
    }
}
