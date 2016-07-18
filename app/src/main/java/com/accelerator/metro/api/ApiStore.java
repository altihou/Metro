package com.accelerator.metro.api;

import com.accelerator.metro.bean.Message;
import com.accelerator.metro.bean.UserInfo;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    String BASE_URL="http://192.168.1.144:9096/TicketSys/mobile.php/Index/m/";

    /**
     * 登录
     * @param phone 手机号
     * @param pwd 密码
     * @return
     */
    @GET("User/action/UserRegister/phone_no/{phone}/key/{pwd}")
    Observable<UserInfo> login(@Path("phone") String phone
            ,@Path("pwd") String pwd);

    /**
     * 上传头像和脸部图片
     * @param files
     * @return
     */
    @Multipart
    @POST("upload")
    Observable<Message> uploadAvatarAndFace(@PartMap Map<String,RequestBody> files);

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
