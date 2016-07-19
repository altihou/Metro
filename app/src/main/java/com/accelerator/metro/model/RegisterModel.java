package com.accelerator.metro.model;

import android.util.Log;

import com.accelerator.metro.Config;
import com.accelerator.metro.api.ApiEngine;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.bean.User;
import com.accelerator.metro.bean.UserAvatar;
import com.accelerator.metro.bean.UserRegister;
import com.accelerator.metro.contract.RegisterContract;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nicholas on 2016/7/9.
 */
public class RegisterModel implements RegisterContract.Model {


    @Override
    public Observable<UserRegister> register(String phone, String pwd, final String path) {

        ApiStore apiStore = ApiEngine.getInstance().apiStore;

        final UserRegister register = new UserRegister();

        return apiStore.register(Config.M,Config.ACTION_REGISTER,phone,pwd)
                .filter(new Func1<User, Boolean>() {
                    @Override
                    public Boolean call(User user) {
                        if (user.getIs_ok() != 1) {
                            Log.e("RegisterModel", "个人信息注册错误，错误码：" + user.getIs_ok());
                        }
                        return user.getIs_ok() == 1;
                    }
                })
                .flatMap(new Func1<User, Observable<UserAvatar>>() {
                    @Override
                    public Observable<UserAvatar> call(User user) {

                        Log.e("RegisterModel", "User:"+user.toString());

                        register.setUserId(user.getElse_info().getUser_id());
                        register.setUserSession(user.getElse_info().getSession_id());
                        return uploadAvatar(user, path);
                    }
                }).flatMap(new Func1<UserAvatar, Observable<UserRegister>>() {

                    @Override
                    public Observable<UserRegister> call(UserAvatar userAvatar) {

                        register.setUserAvatar(userAvatar.getElse_info().get(0).getHeadpic());

                        return Observable.just(register);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private Observable<UserAvatar> uploadAvatar(User user, String path) {

        ApiStore apiStore = ApiEngine.getInstance().apiStore;

        File file = new File(path);
        RequestBody request = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        String userId = user.getElse_info().getUser_id();

        return apiStore.uploadAvatar(userId, request)
                .filter(new Func1<UserAvatar, Boolean>() {
                    @Override
                    public Boolean call(UserAvatar userAvatar) {
                        if (userAvatar.getIs_ok() != 1) {
                            Log.e("RegisterModel", "上传头像错误，错误码：" + userAvatar.getIs_ok());
                        }
                        return userAvatar.getIs_ok() == 1;
                    }
                });

    }

}
