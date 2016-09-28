package com.accelerator.metro.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.bean.CommitOrder;
import com.accelerator.metro.contract.CommitOrderContract;
import com.accelerator.metro.presenter.CommitOrderPresenter;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.ui.activity.MainActivity;
import com.accelerator.metro.ui.activity.PayOrderActivity;
import com.accelerator.metro.ui.activity.SearchActivity;
import com.accelerator.metro.utils.ToastUtil;

import me.himanshusoni.quantityview.QuantityView;

/**
 * Created by Nicholas on 2016/8/5.
 */
public class TicketPickerDialogFragment extends DialogFragment implements CommitOrderContract.View {

    private static final String TAG = TicketPickerDialogFragment.class.getName();

    public static final String START_STATION = "start_station";
    public static final String END_STATION = "end_station";
    public static final String START_ID = "start_id";
    public static final String END_ID = "end_id";
    public static final String PRICE = "price";
    public static final String TYPE = "type";

    public static final int TYPE_SEARCH = 0;
    public static final int TYPE_STATION = 1;

    private ImageView imgClose;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvPrice;
    private QuantityView pickerView;
    private Button btnOk;

    private String startStation;
    private String endStation;
    private String startId;
    private String endId;
    private int price;
    private int totalPrice;
    private int count;
    private int type;

    private CommitOrderPresenter presenter;

    public static TicketPickerDialogFragment newInstance(String startStation, String endStation, String startId, String endId, int price, int type) {
        TicketPickerDialogFragment dialog = new TicketPickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(START_STATION, startStation);
        bundle.putString(END_STATION, endStation);
        bundle.putString(START_ID, startId);
        bundle.putString(END_ID, endId);
        bundle.putInt(PRICE, price);
        bundle.putInt(TYPE, type);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startStation = getArguments().getString(START_STATION);
            endStation = getArguments().getString(END_STATION);
            startId = getArguments().getString(START_ID);
            endId = getArguments().getString(END_ID);
            price = getArguments().getInt(PRICE);
            type = getArguments().getInt(TYPE);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_ticket_picker, null);
        dialog.setView(view);
        initViews(view);
        return dialog;

    }

    private void initViews(View view) {

        presenter = new CommitOrderPresenter(this);

        imgClose = (ImageView) view.findViewById(R.id.dialog_img_close);
        tvStart = (TextView) view.findViewById(R.id.dialog_station_start);
        tvEnd = (TextView) view.findViewById(R.id.dialog_station_end);
        tvPrice = (TextView) view.findViewById(R.id.dialog_tv_price);
        pickerView = (QuantityView) view.findViewById(R.id.dialog_count_picker_view);
        btnOk = (Button) view.findViewById(R.id.dialog_btn_ok);

        imgClose.setOnClickListener(view1 -> dismiss());

        tvStart.setText(startStation);
        tvEnd.setText(endStation);
        tvPrice.setText(price + "元");
        totalPrice = price;
        count = 1;

        pickerView.setLabelDialogTitle(MetroApp.getContext().getResources().getString(R.string.ticket_picker_dialog_count1));
        pickerView.setLabelPositiveButton(MetroApp.getContext().getResources().getString(R.string.SURE));

        pickerView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                tvPrice.setText(price * newQuantity + "元");
                count = newQuantity;
                totalPrice = price * newQuantity;
            }

            @Override
            public void onLimitReached() {
            }
        });

        btnOk.setOnClickListener(view12 -> {
            switch (type) {
                case TYPE_SEARCH:
                    Intent search = new Intent(SearchActivity.ACTION_SHOW_DIALOG);
                    getActivity().sendBroadcast(search);
                    break;
                case TYPE_STATION:
                    Intent main = new Intent(MainActivity.ACTION_NAME_SHOW);
                    getActivity().sendBroadcast(main);
                    break;
            }

            presenter.commitOrder(startId, endId, String.valueOf(totalPrice), String.valueOf(count));
        });

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
        Intent intent = new Intent(OrderUnFinishFragment.ACTION_NAME_REFRESH);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
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
        intent.putExtra(StationFragment.ORDER_PRICE, info.getMoney());
        intent.putExtra(StationFragment.ORDER_NUM, info.getOrder_sn());
        intent.putExtra(StationFragment.ORDER_TIME, info.getTime());
        intent.putExtra(StationFragment.ORDER_START, startStation);
        intent.putExtra(StationFragment.ORDER_END, endStation);
        startActivity(intent);
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        switch (type) {
            case TYPE_SEARCH:
                Intent search = new Intent(SearchActivity.ACTION_HIDE_DIALOG);
                getActivity().sendBroadcast(search);
                break;
            case TYPE_STATION:
                Intent main = new Intent(MainActivity.ACTION_NAME_HIDE);
                getActivity().sendBroadcast(main);
                break;

        }
        ToastUtil.Short(R.string.ticket_picker_dialog_commit_fail);
    }

    @Override
    public void onCompleted() {
        switch (type) {
            case TYPE_SEARCH:
                Intent search = new Intent(SearchActivity.ACTION_HIDE_DIALOG);
                getActivity().sendBroadcast(search);
                break;
            case TYPE_STATION:
                Intent main = new Intent(MainActivity.ACTION_NAME_HIDE);
                getActivity().sendBroadcast(main);
                break;

        }
        dismiss();
        Intent sendRefresh = new Intent(OrderFinishFragment.ACTION_NAME_REFRESH);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sendRefresh);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscription();
    }
}
