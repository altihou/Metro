package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.contract.OrderContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/23.
 */
public class OrderModel implements OrderContract.Model {


    @Override
    public Observable<Order> getOrder(String p, String type) {


        ApiStore api = ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.MINE_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.MINE_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody page = RequestBody.create(MediaType.parse("text/plain"), p);
        RequestBody t = RequestBody.create(MediaType.parse("text/plain"), type);

        return api.getOrder(m,action,userId,userSession,page,t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
