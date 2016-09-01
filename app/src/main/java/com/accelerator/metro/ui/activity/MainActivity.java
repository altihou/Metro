package com.accelerator.metro.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.ui.fragment.MineFragment;
import com.accelerator.metro.ui.fragment.OrderFragment;
import com.accelerator.metro.ui.fragment.StationFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import butterknife.ButterKnife;

public class MainActivity extends BaseDialogActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final int STATION = 0;
    private static final int ORDER = 1;
    private static final int MINE = 2;
    public static final String ACTION_NAME_SHOW = "action_show";
    public static final String ACTION_NAME_HIDE = "action_hide";
    public static final String ACTION_NAME_FINIFSH = "action_finish";
    public static final String POSITION = "position";


    private long exitTime = 0;

    private BottomBar bottomBar;

    private StationFragment stationFragment;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;

    private FragmentManager manager;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        manager = getSupportFragmentManager();

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        bottomBar.noNavBarGoodness();
        bottomBar.useFixedMode();

        bottomBar.setItemsFromMenu(R.menu.menu_bottombar, new OnMenuTabClickListener() {

            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                switch (menuItemId) {
                    case R.id.menu_station:
                        showFragment(STATION);
                        break;
                    case R.id.menu_order:
                        showFragment(ORDER);
                        break;
                    case R.id.menu_mine:
                        showFragment(MINE);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                //ReClick
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_SHOW);
        intentFilter.addAction(ACTION_NAME_HIDE);
        intentFilter.addAction(ACTION_NAME_FINIFSH);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_NAME_SHOW:
                    setDialogMsg(R.string.WAIT);
                    setDialogCancelable(false);
                    setDialogShow();
                    break;
                case ACTION_NAME_HIDE:
                    setDialogDismiss();
                    break;
                case ACTION_NAME_FINIFSH:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        bottomBar.onSaveInstanceState(outState);
        outState.putInt(POSITION, position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        showFragment(savedInstanceState.getInt(POSITION));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //再按一次退出程序
        long currentTime = System.currentTimeMillis();

        if (currentTime - exitTime > 2000) {
            Toast.makeText(this, R.string.EXIT, Toast.LENGTH_SHORT).show();
            exitTime = currentTime;
            return;
        }
        finish();
    }

    private void showFragment(int index) {

        FragmentTransaction ft = manager.beginTransaction();
        hideFragments(ft);

        position = index;

        switch (index) {

            case STATION:
                if (stationFragment != null) {
                    ft.show(stationFragment);
                } else {
                    stationFragment = StationFragment.newInstance();
                    ft.add(R.id.container, stationFragment);
                }
                break;
            case ORDER:
                if (orderFragment != null) {
                    ft.show(orderFragment);
                } else {
                    orderFragment = OrderFragment.newInstance();
                    ft.add(R.id.container, orderFragment);
                }
                break;
            case MINE:
                if (mineFragment != null) {
                    ft.show(mineFragment);
                } else {
                    mineFragment = MineFragment.newInstance();
                    ft.add(R.id.container, mineFragment);
                }
                break;
        }

        ft.commit();
    }

    private void hideFragments(FragmentTransaction ft) {

        if (stationFragment != null) {
            ft.hide(stationFragment);
        }
        if (orderFragment != null) {
            ft.hide(orderFragment);
        }

        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
    }


}
