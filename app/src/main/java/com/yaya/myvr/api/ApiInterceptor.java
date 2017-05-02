package com.yaya.myvr.api;


import android.widget.Toast;

import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.util.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by admin on 2017/4/14.
 */

public class ApiInterceptor implements Interceptor {

    private void showToast(boolean isConnected) {
        // 当前为网络线程
        // 为false调用一次
        Observable.just(isConnected)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            Toast.makeText(VRApp.getAppInstance().getApplicationContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean isConnected = NetworkUtils.isConnected();
        LogUtils.e("isConnected = " + isConnected);
        showToast(isConnected);

        if (!isConnected) {
            //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (isConnected) {
            String cacheControl = request.cacheControl().toString();
//            String cacheControl = "Cache-Control:public,max-age=0";
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            //无网络
            return response.newBuilder()
                    .header("Cache-Control", "public,only-if-cached,max-stale=30*24*60*60")
                    .removeHeader("Pragma")
                    .build();
        }

    }
}
