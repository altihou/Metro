package com.accelerator.metro.model;

import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.contract.LoginContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zoom on 2016/5/17.
 */
public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<UserInfo> login(String phone,String pwd) {

        ApiStore apiStore= ApiEngine.getInstance().apiStore;

        return apiStore.login(phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
