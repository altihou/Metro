package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.accelerator.metro.Config;
import com.accelerator.metro.R;
import com.accelerator.metro.ui.fragment.MineFragment;
import com.accelerator.metro.ui.fragment.OrderFragment;
import com.accelerator.metro.ui.fragment.StationFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int STATION = 0;
    private static final int ORDER = 1;
    private static final int MINE = 2;

    private long exitTime = 0;

    private BottomBar bottomBar;

    private StationFragment stationFragment;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;

    private FragmentManager manager;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences spf=getSharedPreferences(Config.FIRST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=spf.edit();
        boolean isFirst=spf.getBoolean(Config.FIRST_TIME,true);
        if (isFirst){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            editor.putBoolean(Config.FIRST_TIME,false);
            editor.apply();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
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

    /**
     * 显示Fragment
     * @param index 序号
     */
    private void showFragment(int index) {

        FragmentTransaction ft = manager.beginTransaction();
        hideFragments(ft);

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

    /**
     * 隐藏Fragment
     * @param ft FragmentTransaction
     */
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
