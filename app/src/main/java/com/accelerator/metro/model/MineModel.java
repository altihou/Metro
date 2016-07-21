package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.MineInfo;
import com.accelerator.metro.contract.MineContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/20.
 */
public class MineModel implements MineContract.Model {

    @Override
    public Observable<MineInfo> getMine() {

        ApiStore api = ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        Log.e("MineModel ID & Session", id + " " + session);

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.MINE_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.MINE_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);

        return api.mine(m, action, userId, userSession)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
