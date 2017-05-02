package com.yaya.myvr.app;

import android.app.Application;

import com.yaya.myvr.util.LogUtils;

/**
 * Created by admin on 2017/5/2.
 */

public class VRApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.init(true, 'v', "TAG");
    }
}
