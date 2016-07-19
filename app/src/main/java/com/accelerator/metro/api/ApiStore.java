package com.accelerator.metro.api;

import com.accelerator.metro.bean.Message;
import com.accelerator.metro.bean.User;
import com.accelerator.metro.bean.UserAvatar;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Nicholas on 2016/5/8.
 */
public interface ApiStore {

    String BASE_URL = "http://192.168.1.144:9096/TicketSys/mobile.php/";

    /**
     * 登录
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return
     */
    @FormUrlEncoded
    @POST("Index")
    Observable<User> login(@Field("m") String m
            , @Field("action") String action
            , @Field("phone") String phone
            , @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("Index")
    Observable<User> register(@Field("m") String m
            , @Field("action") String action
            , @Field("phone_no") String phone
            , @Field("key") String key);

    /**
     * 上传头像
     *
     * @param userId UserId
     * @param file   文件地址
     * @return
     */
    @Multipart
    @POST("UserModify/action/ModifyHeadPic/user_id/{user_id}")
    Observable<UserAvatar> uploadAvatar(@Path("user_id") String userId
            , @Part("file\"; filename=\"avatar.jpg") RequestBody file);

    /**
     * 上传头像和脸部图片
     *
     * @param files
     * @return
     */
    @Multipart
    @POST("upload")
    Observable<Message> uploadAvatarAndFace(@PartMap Map<String, RequestBody> files);


}
