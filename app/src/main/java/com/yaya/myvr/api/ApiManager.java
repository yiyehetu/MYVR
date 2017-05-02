package com.yaya.myvr.api;

import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.util.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yph
 * Time is 2016/11/30 17:51
 * Good Good Study, Day Day Up
 */

public class ApiManager {
    private Retrofit mRetrofit;

    private ApiManager(){
        // 缓存
        Cache cache = new Cache(VRApp.getAppInstance().getExternalCacheDir(), 1024 * 1024 * 10);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(new ApiInterceptor())
                .addNetworkInterceptor(new ApiInterceptor())
                .cache(cache)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConst.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        LogUtils.e("Retrofit create success...");
    }

    private static class InnerApiManager{
        private static ApiManager instance = new ApiManager();
    }

    public static ApiManager getInstance(){
        return InnerApiManager.instance;
    }

    public ApiService getApiService(){
        return mRetrofit.create(ApiService.class);
    }
}
