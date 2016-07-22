package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.ModifyPayPwdContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/22.
 *
 */
public class ModifyPayPwdModel implements ModifyPayPwdContract.Model {

    @Override
    public Observable<ResultCode> modifyPayPwd(String oldPwd, String newPwd1, String newPwd2) {

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.MODIFY_PAY_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.MODIFY_PAY_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody old = RequestBody.create(MediaType.parse("text/plain"), oldPwd);
        RequestBody new1 = RequestBody.create(MediaType.parse("text/plain"), newPwd1);
        RequestBody new2 = RequestBody.create(MediaType.parse("text/plain"), newPwd2);

        return getApi().modifyPayPwd(m,action,userId,userSession,old,new1,new2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResultCode> checkPayPwd() {

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.MODIFY_PAY_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.MODIFY_PAY_CHECK_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody userSession = RequestBody.create(MediaType.parse("text/plain"), session);

        return getApi().checkPayPwd(m,action,userId,userSession)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private ApiStore getApi(){
        return ApiEngine.getInstance().apiStore;
    }
}
