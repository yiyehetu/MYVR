package com.yaya.myvr.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import butterknife.ButterKnife;

/**
 * Created by yph
 * Time is 2016/11/24 15:26
 * Good Good Study, Day Day Up
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;

    // 子Fragment加载布局
    protected abstract int getLayoutId();

    // 初始化view
    protected abstract void initView();

    // 初始化data
    protected abstract void initData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }

        ButterKnife.bind(this, rootView);
        initView();

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initData();
            }
        });

        return rootView;
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    // 含有Bundle通过Class跳转界面
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

}
