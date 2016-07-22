package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.Recharge;
import com.accelerator.metro.contract.RechargeContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class RechargeModel implements RechargeContract.Model {

    @Override
    public Observable<Recharge> recharge(String money) {

        ApiStore api= ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.RECHARGE_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.RECHARGE_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody sessionId = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody addmoney = RequestBody.create(MediaType.parse("text/plain"), money);

        return api.recharge(m,action,userId,sessionId,addmoney)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
