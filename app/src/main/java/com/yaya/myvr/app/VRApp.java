package com.yaya.myvr.app;

import android.app.Application;

import com.yaya.myvr.util.LogUtils;

/**
 * Created by admin on 2017/5/2.
 */

public class VRApp extends Application {
    private static VRApp appInstance;

    public static VRApp getAppInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

        LogUtils.init(true, 'v', "TAG");
    }
}
