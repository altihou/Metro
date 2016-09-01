package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.FinishOrderContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/8/2.
 *
 */
public class FinishOrderModel implements FinishOrderContract.Model {

    private static final String TAG = FinishOrderModel.class.getName();

    private static final String TYPE_BACK = "5";
    private static final String TYPE_FINISH = "2";
    private static final String PAGE = "1";

    @Override
    public Observable<Order> getNoTripOrder() {

        ApiStore api = ApiEngine.getInstance().apiStore;
        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);

        RequestBody page = RequestBody.create(MediaType.parse("text/plain"), PAGE);
        RequestBody t = RequestBody.create(MediaType.parse("text/plain"), TYPE_FINISH);

        return api.getOrder(m, action, userId, userSession, page, t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Order> getHistoryOrder() {

        ApiStore api = ApiEngine.getInstance().apiStore;
        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);

        RequestBody page = RequestBody.create(MediaType.parse("text/plain"), PAGE);
        RequestBody t = RequestBody.create(MediaType.parse("text/plain"), TYPE_BACK);

        return api.getOrder(m, action, userId, userSession, page, t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResultCode> refundOrder(String orderSn) {

        ApiStore api = ApiEngine.getInstance().apiStore;
        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_REFUND_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_REFUND_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody orderNum = RequestBody.create(MediaType.parse("text/plain"), orderSn);

        return api.refundOrder(m, action, userId, userSession, orderNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResultCode> deletedOrder(String orderSn) {

        ApiStore api = ApiEngine.getInstance().apiStore;
        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_DELETE_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.ORDER_DELETE_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody orderNum = RequestBody.create(MediaType.parse("text/plain"), orderSn);

        return api.deleteOrder(m, action, userId, userSession, orderNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
