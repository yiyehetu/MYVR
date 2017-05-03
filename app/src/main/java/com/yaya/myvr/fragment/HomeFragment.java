package com.yaya.myvr.fragment;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yaya.myvr.R;
import com.yaya.myvr.adapter.HomeAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.RxManager;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/5/2.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.srl_home)
    SwipeRefreshLayout srlHome;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<Subscription> subscriptions = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        LogUtils.e(TAG, "HomeFragment init View...");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHome.setLayoutManager(layoutManager);
        RecyclerViewDivider divider = new RecyclerViewDivider(new ColorDrawable(0xffececec), LinearLayoutManager.VERTICAL);
        divider.setHeight(ConvertUtils.dp2px(getContext(), 8.0f));
        rvHome.addItemDecoration(divider);

        srlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        LogUtils.e(TAG, "HomeFragment init Data...");
        requestData();
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "getIndex");

        Subscription subscription = ApiManager.getInstance().getApiService().getHomeInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                        srlHome.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        srlHome.setRefreshing(false);
                    }

                    @Override
                    public void onNext(HomeInfo homeInfo) {
                        LogUtils.e(TAG, "onNext... homeInfo = " + homeInfo);
                        bindData(homeInfo);
                    }
                });

        subscriptions.add(subscription);
    }

    private void bindData(HomeInfo homeInfo) {
        if (homeInfo != null && homeInfo.getErrCode() == 0) {
            HomeAdapter homeAdapter = new HomeAdapter(homeInfo.getData(), getContext(), getFragmentManager());
            rvHome.setAdapter(homeAdapter);

        } else {
            // 数据有误
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RxManager.unsubscribe(subscriptions);
    }
}
