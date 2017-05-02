package com.yaya.myvr.fragment;

import com.yaya.myvr.R;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/5/2.
 */

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private Map<String, String> map = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        LogUtils.e(TAG, "HomeFragment init View...");
    }

    @Override
    protected void initData() {
        LogUtils.e(TAG, "HomeFragment init Data...");
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "getIndex");

        ApiManager.getInstance().getApiService().getHomeInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(HomeInfo homeInfo) {
                        LogUtils.e(TAG, "onNext... homeInfo = " + homeInfo);
                    }
                });

    }

}
