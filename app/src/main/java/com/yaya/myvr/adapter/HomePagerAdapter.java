package com.yaya.myvr.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.fragment.LoopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<HomeInfo.DataBean.LoopViewBean> loopList;
    private List<LoopFragment> loopFragments = new ArrayList<>();

    public HomePagerAdapter(FragmentManager fm, List<HomeInfo.DataBean.LoopViewBean> loopList) {
        super(fm);
        this.loopList = loopList;

        for (int i = 0; i < loopList.size(); i++) {
            HomeInfo.DataBean.LoopViewBean bean = loopList.get(i);
            LoopFragment loopFragment = new LoopFragment();
            Bundle args = new Bundle();
            args.putString(AppConst.LOOP_IMG, bean.getImg());
            args.putString(AppConst.LOOP_TITLE, bean.getTitle());
            args.putString(AppConst.LOOP_ID, bean.getVideoId());
            loopFragment.setArguments(args);
            loopFragments.add(loopFragment);
        }

    }

    @Override
    public Fragment getItem(int position) {
        return loopFragments.get(position);
    }

    @Override
    public int getCount() {
        return loopList.size();
    }
}
