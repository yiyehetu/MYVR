package com.yaya.myvr.fragment;

import com.yaya.myvr.R;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.util.LogUtils;

/**
 * Created by admin on 2017/5/2.
 */

public class HomeFragment extends BaseFragment {
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

}
