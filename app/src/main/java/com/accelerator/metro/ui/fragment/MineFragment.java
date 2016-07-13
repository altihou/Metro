package com.accelerator.metro.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accelerator.metro.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zoom on 2016/5/4.
 */
public class MineFragment extends Fragment {


    @Bind(R.id.toolbar)
    Toolbar toolbar;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        toolbar.setTitle(R.string.bottombar_tab4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
