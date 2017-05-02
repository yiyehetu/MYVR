package com.yaya.myvr.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by admin on 2017/5/2.
 */

public class FixedViewPager extends ViewPager {
    public FixedViewPager(Context context) {
        super(context);
    }

    public FixedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 不响应
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    // 不拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
