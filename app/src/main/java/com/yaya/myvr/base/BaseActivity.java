package com.yaya.myvr.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yaya.myvr.app.AppManager;
import com.yaya.myvr.app.RxManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;

public abstract class BaseActivity extends AppCompatActivity {

    // 子Activity加载布局
    protected abstract int getLayoutId();
    // 初始化View
    protected abstract void initView();
    // 订阅集合
    protected List<Subscription> subscriptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 预处理
        doBeforeSetContentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
    }

    protected void doBeforeSetContentView() {
        // 添加Activity至栈中
        AppManager.getInstance().addActivity(this);
        // 透明状态栏(延伸)
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            // 设置系统UI可见性
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // 沉浸状态栏
        /**
         * 布局设置属性
         *  android:fitsSystemWindows="true"
         *  android:clipToPadding="true"
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

//        ScreenUtils.setTranslucentStatus(this, true);


        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // 通过Class跳转界面
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    // 通过Class跳转界面
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    // 含有Bundle通过Class跳转界面
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    // 含有Bundle通过Class跳转界面
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RxManager.unsubscribe(subscriptionList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除Activity
        AppManager.getInstance().finishActivity(this);
    }

}
