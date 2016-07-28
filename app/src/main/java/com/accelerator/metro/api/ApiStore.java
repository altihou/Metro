package com.accelerator.metro.api;

import com.accelerator.metro.bean.CommitOrder;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.bean.MineInfo;
import com.accelerator.metro.bean.ModifyUser;
import com.accelerator.metro.bean.Recharge;
import com.accelerator.metro.bean.User;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by Nicholas on 2016/5/8.
 */
public interface ApiStore {

    String BASE_URL = "http://192.168.1.144:9096/TicketSys/mobile.php/";
    String BASE_URL_IMG = "http://192.168.1.144:9096/TicketSys/TicketSys/data/upload/image/user/";

    //Login
    @Multipart
    @POST("Index")
    Observable<User> login(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("phone_no") RequestBody phone
            , @Part("key") RequestBody pwd);

    //Register
    @Multipart
    @POST("Index")
    Observable<User> register(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("phone_no") RequestBody phone
            , @Part("key1") RequestBody key1
            , @Part("key2") RequestBody key2
            , @Part("file\"; filename=\"avatar.jpg") RequestBody file);

    //Register No Avatar
    @Multipart
    @POST("Index")
    Observable<User> registerNoAvatar(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("phone_no") RequestBody phone
            , @Part("key1") RequestBody key1
            , @Part("key2") RequestBody key2);

    //Mine
    @Multipart
    @POST("Index")
    Observable<MineInfo> mine(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId);

    //Modify
    @Multipart
    @POST("Index")
    Observable<ModifyUser> modify(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("nickname") RequestBody nickname
            , @Part("user_sex") RequestBody sex
            , @Part("file\"; filename=\"avatar.jpg") RequestBody avatarFile);

    //Modify No Avatar
    @Multipart
    @POST("Index")
    Observable<ModifyUser> modifyNoAvatar(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("nickname") RequestBody nickname
            , @Part("user_sex") RequestBody sex);

    //Recharge
    @Multipart
    @POST("Index")
    Observable<Recharge> recharge(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("money") RequestBody money);

    //Feedback
    @Multipart
    @POST("Index")
    Observable<ResultCode> feedback(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("content") RequestBody content);

    //Modify Login Pwd
    @Multipart
    @POST("Index")
    Observable<ResultCode> modifyLoginPwd(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("old_pwd") RequestBody oldPwd
            , @Part("new_pwd1") RequestBody newPwd1
            , @Part("new_pwd2") RequestBody newPwd2);

    //Commit Order
    @Multipart
    @POST("Index")
    Observable<CommitOrder> commitOrder(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("start_point") RequestBody start
            , @Part("end_point") RequestBody end
            , @Part("money") RequestBody money);

    //Modify Pay Pwd
    @Multipart
    @POST("Index")
    Observable<ResultCode> modifyPayPwd(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("old_pwd") RequestBody oldPwd
            , @Part("new_pwd1") RequestBody newPwd1
            , @Part("new_pwd2") RequestBody newPwd2);

    //Check Pay Exist
    @Multipart
    @POST("Index")
    Observable<ResultCode> checkPayPwd(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId);

    //Pay For Order
    @Multipart
    @POST("Index")
    Observable<ResultCode> payOrder(@PartMap Map<String, RequestBody> map);

    //Get Order
    //2表示未出行订单
    //3已出行订单与退票
    //1未完成订单
    //4已完成订单
    //-1所有订单
    //5退票
    //6充值
    @Multipart
    @POST("Index")
    Observable<Order> getOrder(@Part("m") RequestBody m
            , @Part("action") RequestBody action
            , @Part("user_id") RequestBody userId
            , @Part("session_id") RequestBody sessionId
            , @Part("p") RequestBody p
            , @Part("type") RequestBody type);

}
