package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.PlanTicketContract;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/8/8.
 */
public class PlanTicketModel implements PlanTicketContract.Model {

    @Override
    public Observable<ResultCode> autoBuy(int type, String startPoint, String endPoint, String workTime, String days) {

        Map<String, RequestBody> maps = new HashMap<>();
        ApiStore api = ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        maps.put("m", RequestBody.create(MediaType.parse("text/plain"), Config.AUTO_BUY_M));
        maps.put("action", RequestBody.create(MediaType.parse("text/plain"), Config.AUTO_BUY_ACTION));
        maps.put("user_id", RequestBody.create(MediaType.parse("text/plain"), id));
        maps.put("session_id", RequestBody.create(MediaType.parse("text/plain"), session));

        maps.put("type", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(type)));
        maps.put("start_point", RequestBody.create(MediaType.parse("text/plain"), startPoint));
        maps.put("end_point", RequestBody.create(MediaType.parse("text/plain"), endPoint));
        maps.put("work_time", RequestBody.create(MediaType.parse("text/plain"), workTime));

        if (!TextUtils.isEmpty(days)) {
            maps.put("days", RequestBody.create(MediaType.parse("text/plain"), days));
        }

        return api.autoBuy(maps)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
