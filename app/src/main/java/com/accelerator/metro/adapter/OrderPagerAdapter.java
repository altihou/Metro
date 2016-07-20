package com.accelerator.metro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;

import java.util.List;

/**
 * Created by Nicholas on 2016/7/12.
 */
public class OrderPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;

    String[] titles = new String[]{
            MetroApp.getContext().getResources().getString(R.string.order_tab_finish),
            MetroApp.getContext().getResources().getString(R.string.order_tab_unfinish)
    };

    public OrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragments(List<Fragment> fragments){
        if (fragments==null)
            return;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments==null?0:fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
