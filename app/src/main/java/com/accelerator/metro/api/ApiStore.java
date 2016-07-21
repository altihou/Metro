package com.accelerator.metro.api;

import com.accelerator.metro.bean.MineInfo;
import com.accelerator.metro.bean.User;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Nicholas on 2016/5/8.
 */
public interface ApiStore {

    String BASE_URL = "http://192.168.1.144:9096/TicketSys/mobile.php/";
    String BASE_URL_IMG = "http://192.168.1.144:9096/TicketSys/TicketSys/data/upload/image/user/";

    //登录
    @Multipart
    @POST("Index")
    Observable<User> login(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("phone_no") RequestBody phone
            , @Part("key") RequestBody pwd);

    //注册
    @Multipart
    @POST("Index")
    Observable<User> register(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("phone_no") RequestBody phone
            , @Part("key1") RequestBody key1
            , @Part("key2") RequestBody key2
            , @Part("file\"; filename=\"avatar.jpg") RequestBody file);

    //Mine
    @Multipart
    @POST("Index")
    Observable<MineInfo> mine(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId);

}
