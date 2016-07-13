package com.accelerator.metro.api;

import com.accelerator.metro.bean.Message;
import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.bean.UserLogin;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Nicholas on 2016/5/8.
 */
public interface ApiStore {

    String BASE_URL="/";

    /**
     * 登录
     * @param userLogin UserLogin
     * @return UserInfo
     */
    @POST("add")
    Observable<UserInfo> login(@Body UserLogin userLogin);

    /**
     * 注册
     * @param userInfo UserInfo
     * @return UserRegister
     */
    @POST("register")
    Observable<Message> register(@Body UserInfo userInfo);


    /**
     * 上传头像
     * @param file 头像路径
     * @return UserAvatar
     */
    @Multipart
    @POST("upload")
    Observable<Message> uploadAvatar(@Part MultipartBody.Part file);




}
