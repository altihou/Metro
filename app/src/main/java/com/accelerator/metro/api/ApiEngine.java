package com.accelerator.metro.api;

import com.accelerator.metro.MetroApp;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zoom on 2016/5/8.
 */
public class ApiEngine {

    private volatile static ApiEngine apiEngine;
    public ApiStore apiStore;

    /**
     * 防止构造
     */
    private ApiEngine(){

        //拦截器
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //缓存
        File cacheFile = new File(MetroApp.getContext().getCacheDir(), "okhttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(12,TimeUnit.SECONDS)
                .writeTimeout(12,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .cache(cache)
                .build();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ApiStore.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiStore=retrofit.create(ApiStore.class);
    }

    /**
     * 单例
     * @return ApiEngine
     */
    public static ApiEngine getInstance(){
        if (apiEngine==null){
            synchronized (ApiEngine.class){
                if (apiEngine==null){
                    apiEngine=new ApiEngine();
                }
            }
        }
        return apiEngine;
    }

}
