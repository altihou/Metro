package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.CommitOrder;
import com.accelerator.metro.contract.CommitOrderContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/22.
 */
public class CommitOrderModel implements CommitOrderContract.Model {

    @Override
    public Observable<CommitOrder> commitOrder(String start, String end,String money) {

        ApiStore api= ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.COMMIT_ORDER_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.COMMIT_ORDER_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody sessionId = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody startStation = RequestBody.create(MediaType.parse("text/plain"), start);
        RequestBody endStation = RequestBody.create(MediaType.parse("text/plain"), end);
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), money);


        return api.commitOrder(m,action,userId,sessionId,startStation,endStation,price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
