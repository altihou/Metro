package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.PayOrderContract;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/23.
 *
 */
public class PayOrderModel implements PayOrderContract.Model {

    @Override
    public Observable<ResultCode> payOrder(String orderNum, String userPayPwd) {

        Map<String, RequestBody> maps = new HashMap<>();

        ApiStore api = ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        maps.put("m", RequestBody.create(MediaType.parse("text/plain"), Config.PAY_ORDER_M));
        maps.put("action", RequestBody.create(MediaType.parse("text/plain"), Config.PAY_ORDER_ACTION));
        maps.put("user_id", RequestBody.create(MediaType.parse("text/plain"), id));
        maps.put("session_id", RequestBody.create(MediaType.parse("text/plain"), session));
        maps.put("order_sn", RequestBody.create(MediaType.parse("text/plain"), orderNum));
        maps.put("user_pay_key", RequestBody.create(MediaType.parse("text/plain"), userPayPwd));

        return api.payOrder(maps)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
