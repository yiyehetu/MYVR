package com.yaya.myvr.fragment;

import com.yaya.myvr.R;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.util.LogUtils;

/**
 * Created by admin on 2017/5/2.
 */

public class FindFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {
        LogUtils.e("FindFragment init View...");
    }

    @Override
    protected void initData() {
        LogUtils.e("FindFragment init Data...");
    }
}
