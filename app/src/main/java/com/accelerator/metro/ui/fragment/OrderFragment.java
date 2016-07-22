package com.accelerator.metro.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accelerator.metro.R;
import com.accelerator.metro.adapter.OrderPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nicholas on 2016/5/4.
 */
public class OrderFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    public static OrderFragment newInstance(){
        return new OrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {

        toolbar.setTitle(R.string.bottombar_tab2);
        toolbar.inflateMenu(R.menu.menu_order);

        OrderPagerAdapter adapter=new OrderPagerAdapter(getChildFragmentManager());
        List<Fragment> fragments=new ArrayList<>();

        fragments.add(OrderFinishFragment.newInstance());
        fragments.add(OrderUnFinishFragment.newInstance());

        adapter.addFragments(fragments);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
