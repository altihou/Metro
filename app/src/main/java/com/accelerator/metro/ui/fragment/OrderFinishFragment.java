package com.accelerator.metro.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accelerator.metro.R;

/**
 * Created by Nicholas on 2016/7/19.
 */
public class OrderFinishFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static OrderFinishFragment newInstance(){
        OrderFinishFragment fragment=new OrderFinishFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order_finish,container,false);
        return view;
    }




}
