package com.accelerator.metro.model;

import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.bean.UserLogin;
import com.accelerator.metro.contract.LoginContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zoom on 2016/5/17.
 */
public class LoginModel implements LoginContract.Model {

    /**
     * 把登录信息上传服务器，得到返回信息
     * @param userLogin 登录实体
     * @return Observable<UserLogin>
     */
    @Override
    public Observable<UserInfo> login(UserLogin userLogin) {

        ApiStore apiStore= ApiEngine.getInstance().apiStore;

        return apiStore.login(userLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
