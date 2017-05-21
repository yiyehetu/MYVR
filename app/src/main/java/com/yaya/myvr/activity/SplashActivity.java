package com.yaya.myvr.activity;

import android.os.Bundle;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.bean.LoginInfo;
import com.yaya.myvr.dao.Personal;
import com.yaya.myvr.dao.Task;
import com.yaya.myvr.dao.Task_Table;
import com.yaya.myvr.util.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 欢迎页面
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
    }

    private void setNetBaseMap() {
        ApiConst.BASE_MAP.put("rnddd", "0.20147895014532935");
        ApiConst.BASE_MAP.put("devtype", "android");
        ApiConst.BASE_MAP.put("ver", "1004000");
        ApiConst.BASE_MAP.put("channel", "106");
        ApiConst.BASE_MAP.put("build", "003");
        ApiConst.BASE_MAP.put("apiVer", "2");
    }

    private void setDownLoadState() {
        AppConst.DOWNLOAD_STATUS.put(AppConst.IDLE, "待下载...");
        AppConst.DOWNLOAD_STATUS.put(AppConst.DOWNLOADING, "下载中...");
        AppConst.DOWNLOAD_STATUS.put(AppConst.DOWNLOAD_PAUSE, "下载暂停...");
        AppConst.DOWNLOAD_STATUS.put(AppConst.DOWNLOADED, "下载完成...");

        List<Task> tasks = SQLite.select()
                .from(Task.class)
                .where(Task_Table.status.eq(AppConst.DOWNLOADING))
                .queryList();
        if (tasks != null && tasks.size() > 0) {
            for(int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                task.status = AppConst.DOWNLOAD_PAUSE;
                task.update();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetBaseMap();
        setDownLoadState();

        boolean isLogin = VRApp.getAppInstance().getSpUtil().getBoolean("isLogin");
        if (isLogin) {
            readData();
        } else {
            startTask();
        }

    }

    private void readData() {
        List<Personal> personals = SQLite.select()
                .from(Personal.class)
                .queryList();

        if (personals != null && personals.size() > 0) {
            Personal personal = personals.get(0);
            login(personal);
        } else {
            startTask();
        }
    }

    private void login(final Personal personal) {
        Map<String, String> map = new HashMap<>();
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "userLogin");

        Subscription subscription = ApiManager.getInstance().getApiService().getLoginInfo(map, personal.deviceId, personal.password, personal.phone)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                        startTask();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        startTask();
                    }

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        LogUtils.e(TAG, "onNext... loginInfo = " + loginInfo);
                        bindLoginInfo(loginInfo, personal);
                    }
                });

        subscriptionList.add(subscription);
    }

    private void bindLoginInfo(LoginInfo loginInfo, Personal personal) {
        if (loginInfo != null && loginInfo.getErrCode() == 0) {
            ApiConst.IS_LOGIN = true;
            ApiConst.LOGIN_KEY = loginInfo.getData().getLoginKey();
            ApiConst.PHONE = personal.phone;
        }
    }

    private void startTask() {
        // 延时任务
        Subscription subscription = Observable.timer(500, TimeUnit.MILLISECONDS)
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
}
