package com.yaya.myvr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yaya.myvr.R;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.util.LogUtils;

/**
 * Created by admin on 2017/5/2.
 */

public class HomeFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        LogUtils.e("HomeFragment init View...");
    }

    @Override
    protected void initData() {
        LogUtils.e("HomeFragment init Data...");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.e("HomeFragment onActivityCreated...");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("HomeFragment onStart...");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("HomeFragment onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e("HomeFragment onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e("HomeFragment onStop...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("HomeFragment onDestroy...");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e("HomeFragment setUserVisibleHint...isVisibleToUser = " + isVisibleToUser);
    }
}
