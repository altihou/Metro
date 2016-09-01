package com.accelerator.metro.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.MineInfo;
import com.accelerator.metro.contract.MineContract;
import com.accelerator.metro.presenter.MinePresenter;
import com.accelerator.metro.ui.activity.AboutActivity;
import com.accelerator.metro.ui.activity.ExpenseCalendarActivity;
import com.accelerator.metro.ui.activity.FeedbackActivity;
import com.accelerator.metro.ui.activity.LoginActivity;
import com.accelerator.metro.ui.activity.ModifyUserActivity;
import com.accelerator.metro.ui.activity.NFCActivity;
import com.accelerator.metro.ui.activity.RechargeActivity;
import com.accelerator.metro.ui.activity.SettingsActivity;
import com.accelerator.metro.ui.activity.StationNavigationActivity;
import com.accelerator.metro.utils.NFCUtil;
import com.accelerator.metro.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zoom on 2016/5/4.
 */
public class MineFragment extends Fragment implements MineContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MineFragment.class.getName();
    private static final int REQUEST_CODE_EDIT = 0;
    private static final int REQUEST_CODE_RECHARGE = 1;

    public static final String ACTION_NAME_REFRESH = "mine_refresh";
    public static final String ACTION_NAME_SHOW_NFC = "mine_show_nfc";
    public static final String ACTION_NAME_HIDE_NFC = "mine_hide_nfc";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.mine_user_avatar)
    CircleImageView imgAvatar;
    @Bind(R.id.mine_user_name)
    TextView tvUserName;
    @Bind(R.id.mine_user_id)
    TextView tvUserId;
    @Bind(R.id.mine_user_money)
    TextView tvUserMoney;
    @Bind(R.id.mine_user_money_info)
    RelativeLayout mineUserInfoDetail;
    @Bind(R.id.mine_user_wallet)
    RelativeLayout mineUserWallet;
    @Bind(R.id.mine_user_expense_calendar)
    RelativeLayout mineUserExpenseCalendar;
    @Bind(R.id.mine_settings)
    RelativeLayout mineSettings;
    @Bind(R.id.mine_feedback)
    RelativeLayout mineFeedback;
    @Bind(R.id.mine_about)
    RelativeLayout mineAbout;
    @Bind(R.id.mine_fab)
    FloatingActionButton fabEditUser;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.mine_nfc)
    RelativeLayout mineNfc;
    @Bind(R.id.mine_station_navigation)
    RelativeLayout stationNavigation;

    private MinePresenter presenter;
    private LocalBroadcastManager localBroadcastManager;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
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
        intentFilter.addAction(ACTION_NAME_SHOW_NFC);
        intentFilter.addAction(ACTION_NAME_HIDE_NFC);
        localBroadcastManager.registerReceiver(receiver, intentFilter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_NAME_REFRESH:
                    swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                    break;
                case ACTION_NAME_SHOW_NFC:
                    if (!isVisibility(mineNfc)) {
                        mineNfc.setVisibility(View.VISIBLE);
                    }
                    break;
                case ACTION_NAME_HIDE_NFC:
                    if (isVisibility(mineNfc)) {
                        mineNfc.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    private void initViews() {

        toolbar.setTitle(R.string.bottombar_tab4);
        presenter = new MinePresenter(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.googleColorRed,
                R.color.googleColorGreen,
                R.color.googleColorYellow,
                R.color.googleColorBlue);
        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sp = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        boolean showNFC = sp.getBoolean(Config.NFC_STATE, false);

        if (showNFC) {
            if (!isVisibility(mineNfc)) {
                mineNfc.setVisibility(View.VISIBLE);
            }
        } else {
            if (isVisibility(mineNfc)) {
                mineNfc.setVisibility(View.GONE);
            }
        }

    }

    private boolean isVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @OnClick(R.id.mine_fab)
    public void onFabClick(View view) {
        startActivityForResult(new Intent(getActivity(),
                ModifyUserActivity.class), REQUEST_CODE_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDIT:
            case REQUEST_CODE_RECHARGE:
                if (resultCode == Activity.RESULT_OK) {
                    onRefresh();
                }
                break;
        }
    }

    @OnClick(R.id.mine_user_money_info)
    public void onMoneyInfoClick(View view) {
    }

    @OnClick(R.id.mine_user_wallet)
    public void onWalletClick(View view) {
        startActivityForResult(new Intent(getActivity(),
                RechargeActivity.class), REQUEST_CODE_RECHARGE);
    }

    @OnClick(R.id.mine_user_expense_calendar)
    public void onExpenseCalendarClick(View view) {
        startActivity(new Intent(getActivity(), ExpenseCalendarActivity.class));
    }

    @OnClick(R.id.mine_station_navigation)
    public void onStationNavigationClick(View view){
        startActivity(new Intent(getActivity(), StationNavigationActivity.class));
    }

    @OnClick(R.id.mine_settings)
    public void onSettingsClick(View view) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    @OnClick(R.id.mine_nfc)
    public void onNFCClick(View view) {

        if (!NFCUtil.hasNFC(getActivity())) {
            ToastUtil.Short(R.string.qrcode_not_nfc);
            return;
        }

        if (!NFCUtil.isOpen(getActivity())) {
            ToastUtil.Short(R.string.qrcode_open_nfc);
            return;
        }

        startActivity(new Intent(getActivity(), NFCActivity.class));
    }

    @OnClick(R.id.mine_feedback)
    public void onFeedbackClick(View view) {
        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    @OnClick(R.id.mine_about)
    public void onAboutClick(View view) {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        localBroadcastManager.unregisterReceiver(receiver);
        presenter.unSubscription();
    }

    @Override
    public void onSucceed(MineInfo mineInfo) {
        saveUser2SP(mineInfo);
        setViews(mineInfo.getElse_info());
    }

    private void setViews(MineInfo.ElseInfoBean info) {

        Glide.with(getActivity())
                .load(ApiStore.BASE_URL_IMG + info.getUser_headpic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgAvatar);

        if (!TextUtils.isEmpty(info.getNickname())) {
            tvUserName.setText(info.getNickname());
        }

        tvUserId.setText(info.getPhone_no());
        tvUserMoney.setText(info.getUser_money());
    }

    private void saveUser2SP(MineInfo mineInfo) {

        MineInfo.ElseInfoBean info = mineInfo.getElse_info();

        SharedPreferences spf = MetroApp.getContext()
                .getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, mineInfo.getUser_id());
        editor.putString(Config.USER_SESSION, mineInfo.getSession_id());
        editor.putString(Config.USER_AVATAR, info.getUser_headpic());
        editor.putString(Config.USER_MONEY, info.getUser_money());
        editor.putString(Config.USER_NICKNAME, info.getNickname());

        editor.apply();
    }

    private MineInfo.ElseInfoBean getUserFromSP() {

        MineInfo.ElseInfoBean info = new MineInfo.ElseInfoBean();

        SharedPreferences spf = MetroApp.getContext()
                .getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        info.setNickname(spf.getString(Config.USER_NICKNAME, ""));
        info.setUser_headpic(spf.getString(Config.USER_AVATAR, ""));
        info.setPhone_no(spf.getString(Config.USER_PHONE, ""));
        info.setUser_money(spf.getString(Config.USER_MONEY, ""));

        return info;
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        swipeRefreshLayout.setRefreshing(false);
        ToastUtil.Short(R.string.mine_failure);
    }

    @Override
    public void onCompleted() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        presenter.getMine();
    }
}
