package com.yaya.myvr.app;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.util.SpUtils;

/**
 * Created by admin on 2017/5/2.
 */

public class VRApp extends Application {
    private static VRApp appInstance;
    private SpUtils spUtils;

    public static VRApp getAppInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        LeakCanary.install(this);
        // Normal app init code...

        appInstance = this;
        // 初始化DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
        LogUtils.init(true, 'v', "TAG");
        spUtils = new SpUtils("vr_data", getApplicationContext());
    }

    public SpUtils getSpUtil() {
        return spUtils;
    }
}
