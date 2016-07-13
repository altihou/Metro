package com.accelerator.metro.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accelerator.metro.R;
import com.accelerator.metro.ui.activity.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zoom on 2016/5/4.
 */
public class StationFragment extends Fragment {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    /**
     * 获取StationFragment对象或与Activity传值
     *
     * @return StationFragment
     */
    public static StationFragment newInstance() {
        StationFragment stationFragment = new StationFragment();
        return stationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_station, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.bottombar_tab1);
        return view;
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view){
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
