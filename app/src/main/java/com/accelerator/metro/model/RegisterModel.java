package com.accelerator.metro.model;

import com.accelerator.metro.Config;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.User;
import com.accelerator.metro.contract.RegisterContract;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/9.
 */
public class RegisterModel implements RegisterContract.Model {

    @Override
    public Observable<User> register(String phone, String pwd1, String pwd2, String path) {

        ApiStore apiStore = ApiEngine.getInstance().apiStore;

        File file = new File(path);

        RequestBody imgFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody m = RequestBody.create(MediaType.parse("text/plain"), Config.REGISTER_M);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), Config.REGISTER_ACTION);
        RequestBody phoneNum = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), pwd1);
        RequestBody password2 = RequestBody.create(MediaType.parse("text/plain"), pwd2);

        return apiStore.register(m, action, phoneNum, password1,password2, imgFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
