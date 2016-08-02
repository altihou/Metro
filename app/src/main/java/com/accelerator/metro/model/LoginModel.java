package com.accelerator.metro.model;

import com.accelerator.metro.Config;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.User;
import com.accelerator.metro.contract.LoginContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zoom on 2016/5/17.
 */
public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<User> login(String phone, String pwd) {

        ApiStore apiStore= ApiEngine.getInstance().apiStore;

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.LOGIN_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.LOGIN_ACTION);
        RequestBody phoneNum = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), pwd);

        return apiStore.login(m,action,phoneNum,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
