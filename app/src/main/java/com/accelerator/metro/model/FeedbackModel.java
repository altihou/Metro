package com.accelerator.metro.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.FeedbackContract;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class FeedbackModel implements FeedbackContract.Model {


    @Override
    public Observable<ResultCode> feedback(String content) {

        ApiStore api= ApiEngine.getInstance().apiStore;

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String id = spf.getString(Config.USER_ID, "");
        String session = spf.getString(Config.USER_SESSION, "");

        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.FEEDBACK_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.FEEDBACK_ACTION);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody sessionId = RequestBody.create(MediaType.parse("text/plain"), session);
        RequestBody addcontent = RequestBody.create(MediaType.parse("text/plain"), content);

        return api.feedback(m,action,userId,sessionId,addcontent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
