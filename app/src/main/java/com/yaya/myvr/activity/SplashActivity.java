package com.yaya.myvr.activity;

import android.os.Bundle;

import com.yaya.myvr.R;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 欢迎页面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        setNetBaseMap();
        // 延时任务
        Subscription subscription = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(MainActivity.class);
                        finish();
                    }
                });
        subscriptionList.add(subscription);
    }

    private void setNetBaseMap() {
        ApiConst.BASE_MAP.put("rnddd", "0.20147895014532935");
        ApiConst.BASE_MAP.put("devtype", "android");
        ApiConst.BASE_MAP.put("ver", "1004000");
        ApiConst.BASE_MAP.put("channel", "106");
        ApiConst.BASE_MAP.put("build", "003");
        ApiConst.BASE_MAP.put("apiVer", "2");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
