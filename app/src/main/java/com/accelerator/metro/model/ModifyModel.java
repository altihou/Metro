package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.ModifyUser;
import com.accelerator.metro.contract.ModifyContract;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class ModifyModel implements ModifyContract.Model {

    @Override
    public Observable<ModifyUser> modify(String nickname, String sex, String avatarPath) {

        ApiStore api= ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        File file = new File(avatarPath);
        RequestBody imgFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.MODIFY_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.MODIFY_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody sessionId = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody nick = RequestBody.create(MediaType.parse("text/plain"), nickname);
        RequestBody sexUser = RequestBody.create(MediaType.parse("text/plain"), sex);

        return api.modify(m,action,userId,sessionId,nick,sexUser,imgFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
