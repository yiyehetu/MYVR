package com.yaya.myvr.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.fragment.TypeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/8.
 */

public class TypePagerAdapter extends FragmentPagerAdapter {
    private List<TypeFragment> fragments = new ArrayList<>();
    private String[] titles;

    public TypePagerAdapter(FragmentManager fm, String[] titles, String cate) {
        super(fm);
        this.titles = titles;

        for (int i = 0; i < titles.length; i++) {
            TypeFragment fragment = new TypeFragment();
            Bundle args = new Bundle();
            args.putInt(AppConst.TYPE_POSITION, i);
            args.putString(AppConst.TYPE_CATE, cate);
            args.putString(AppConst.TYPE_TAG, titles[i]);
            fragment.setArguments(args);
            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
