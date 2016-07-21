package com.accelerator.metro.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accelerator.metro.R;
import com.accelerator.metro.bean.MineInfo;
import com.accelerator.metro.contract.MineContract;
import com.accelerator.metro.presenter.MinePresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zoom on 2016/5/4.
 */
public class MineFragment extends Fragment implements MineContract.View {

    private static final String TAG=MineFragment.class.getName();

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
    @Bind(R.id.mine_user_modify_pwd)
    RelativeLayout mineUserModifyPwd;
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

    private MinePresenter presenter;

    /**
     * 获取MineFragment对象或与Activity传值
     *
     * @return MineFragment
     */
    public static MineFragment newInstance() {
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter=new MinePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        initViews();

        return view;
    }

    private void initViews() {
        toolbar.setTitle(R.string.bottombar_tab4);
    }

    @OnClick(R.id.mine_fab)
    public void onFabClick(View view){
        presenter.getMine();
    }

    @OnClick(R.id.mine_user_money_info)
    public void onMoneyInfoClick(View view){
        Log.e(TAG, "onMoneyInfoClick");
    }

    @OnClick(R.id.mine_user_modify_pwd)
    public void onModifyPwdClick(View view){
        Log.e(TAG, "onModifyPwdClick");
    }

    @OnClick(R.id.mine_user_expense_calendar)
    public void onExpenseCalendarClick(View view){
        Log.e(TAG, "onExpenseCalendarClick");
    }

    @OnClick(R.id.mine_settings)
    public void onSettingsClick(View view){
        Log.e(TAG, "onSettingsClick");
    }

    @OnClick(R.id.mine_feedback)
    public void onFeedbackClick(View view){
        Log.e(TAG, "onFeedbackClick");
    }

    @OnClick(R.id.mine_about)
    public void onAboutClick(View view){
        Log.e(TAG, "onAboutClick");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSucceed(MineInfo values) {
        Log.d(TAG, "onSucceed "+values.getElse_info().toString());
    }

    @Override
    public void onFailure(String err) {
        Log.d(TAG, "Error:"+err);
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted");
    }

    @Override
    public void reLogin() {

    }
}
