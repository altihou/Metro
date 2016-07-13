package com.accelerator.metro.model;

import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.Message;
import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.contract.RegisterContract;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/9.
 */
public class RegisterModel implements RegisterContract.Model {

    @Override
    public Observable<Message> register(UserInfo userInfo,String path) {

        ApiStore apiStore= ApiEngine.getInstance().apiStore;

        File file=new File(path);
        RequestBody request=RequestBody.create(MediaType.parse("image/jpg"),file);
        MultipartBody.Part body=MultipartBody.Part.createFormData("picture",file.getName(),request);

        return Observable.zip(apiStore.register(userInfo), apiStore.uploadAvatar(body),
                new Func2<Message, Message, Message>() {
            @Override
            public Message call(Message message, Message message2) {

                return null;
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

    }

}
