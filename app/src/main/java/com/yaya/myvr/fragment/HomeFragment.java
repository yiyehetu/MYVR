package com.yaya.myvr.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.yaya.myvr.R;
import com.yaya.myvr.activity.SearchActivity;
import com.yaya.myvr.adapter.HomeAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.DictInfo;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewDivider;
import com.yaya.myvr.widget.VpSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/5/2.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.srl_home)
    VpSwipeRefreshLayout srlHome;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private Map<String, String> map = new HashMap<>();
    private LinearLayoutManager layoutManager;
    private HomeAdapter homeAdapter;
    private static final float BASE_Y = ConvertUtils.dp2px(VRApp.getAppInstance().getApplicationContext(), 240);
    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        LogUtils.e(TAG, "HomeFragment init View... BASE_Y = " + BASE_Y);

        layoutManager = new LinearLayoutManager(getContext());
        rvHome.setLayoutManager(layoutManager);
        RecyclerViewDivider divider = new RecyclerViewDivider(new ColorDrawable(0xffececec), LinearLayoutManager.VERTICAL);
        divider.setHeight(ConvertUtils.dp2px(getContext(), 8.0f));
        rvHome.addItemDecoration(divider);

        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisiableItem = 0;
            // 透明状态
            private boolean isTransparent = true;
            // 顶部View
//            private View firstView = null;
            // 位置存储
            private int[] location = new int[2];

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && firstVisiableItem == 0 && homeAdapter != null) {
                    // 开启自动轮播
                    homeAdapter.setAutoIndex(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisiableItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisiableItem != 0) {
                    // 停止自动轮播
                    if (homeAdapter != null) {
                        homeAdapter.setAutoIndex(false);
                    }
                    return;
                }

                // 位于顶部
                // 顶部渐变
                View firstView = layoutManager.findViewByPosition(0);
                firstView.getLocationInWindow(location);
                int currY = Math.abs(location[1]);
//                    LogUtils.e(TAG, "currY = " + currY);
                if (currY <= BASE_Y) {
                    isTransparent = true;
                    int alpha = (int) (currY / BASE_Y * 255);
                    rlSearch.setBackgroundColor(Color.argb(alpha, 47, 79, 79));
                } else {
                    if (isTransparent) {
                        rlSearch.setBackgroundColor(Color.argb(255, 47, 79, 79));
                        isTransparent = false;
                    }
                }

            }
        });

        srlHome.setColorSchemeColors(getResources().getColor(R.color.top_nav_background));
        srlHome.setProgressViewOffset(false, AppConst.SWIPE_START, AppConst.SWIPE_END);
        srlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        // 添加加载刷新
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                srlHome.setRefreshing(true);
                cancelAutoSkip();
                requestData();
            }
        });
    }

    @Override
    protected void initData() {
        LogUtils.e(TAG, "HomeFragment init Data...");

//        requestData();
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        // 刷新时避免重复请求
        if (!AppConst.IS_DICT_REQUESTED) {
            requestDict();
        } else {
            requestList();
        }
    }

    /**
     * 请求分类数据
     */
    private void requestDict() {
        Map<String, String> dictMap = new HashMap<>();
        dictMap.putAll(ApiConst.BASE_MAP);
        dictMap.put("cmd", "getDict");

        Subscription subscription = ApiManager.getInstance().getApiService().getDictInfo(dictMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DictInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        srlHome.setRefreshing(false);
                        rlError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(DictInfo dictInfo) {
                        LogUtils.e(TAG, "onNext... dictInfo = " + dictInfo);
                        if (dictInfo != null && dictInfo.getErrCode() == 0) {
                            AppConst.IS_DICT_REQUESTED = true;
                            bindDictInfo(dictInfo);
                            requestList();
                        }
                    }
                });

        subscriptionList.add(subscription);
    }

    private void bindDictInfo(DictInfo dictInfo) {
        DictInfo.DataBean data = dictInfo.getData();
        // 种类
        AppConst.DICT_CATEGORY.addAll(data.getCategory());
        // 地区
        List<DictInfo.DataBean.AreaBean> areaList = data.getArea();
        for (int i = 0; i < areaList.size(); i++) {
            DictInfo.DataBean.AreaBean bean = areaList.get(i);
            AppConst.DICT_AREA.put(bean.getId(), bean.getName());
        }
        // 格式
        List<DictInfo.DataBean.FormatBean> formatList = data.getFormat();
        for (int i = 0; i < formatList.size(); i++) {
            DictInfo.DataBean.FormatBean bean = formatList.get(i);
            AppConst.DICT_FORMAT.put(bean.getId(), bean.getName());
        }
        AppConst.DICT_FORMAT.put("4", "VR");
        // 质量
        List<DictInfo.DataBean.QualityBean> qualityList = data.getQuality();
        for (int i = 0; i < qualityList.size(); i++) {
            DictInfo.DataBean.QualityBean bean = qualityList.get(i);
            AppConst.DICT_QUALITY.put(bean.getId(), bean.getName());
        }
    }

    /**
     * 请求列表数据
     */
    private void requestList() {
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
                        rlError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(HomeInfo homeInfo) {
                        LogUtils.e(TAG, "onNext... homeInfo = " + homeInfo);
                        if (rlError.isShown()) {
                            rlError.setVisibility(View.GONE);
                        }
                        bindData(homeInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定数据
     *
     * @param homeInfo
     */
    private void bindData(HomeInfo homeInfo) {
        if (homeInfo != null && homeInfo.getErrCode() == 0) {
            homeAdapter = new HomeAdapter(homeInfo.getData(), getContext());
            rvHome.setAdapter(homeAdapter);
            setAutoSkip();
        } else {
            // 数据有误
        }
    }

    private void setAutoSkip() {
        if (subscription != null) {
            return;
        }

        subscription = Observable.interval(5, 5, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return homeAdapter.getAutoIndex();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        homeAdapter.performTask();
                    }
                });
    }


    // 取消定时任务
    public void cancelAutoSkip() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @OnClick(R.id.rl_error)
    void reLoad() {
        srlHome.setRefreshing(true);
        rlError.setVisibility(View.GONE);
        requestData();
    }

    @OnClick(R.id.ll_search)
    void clickSearch() {
        startActivity(SearchActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e(TAG, "onStart...");
        if (homeAdapter == null) {
            return;
        }
        setAutoSkip();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop...");
        cancelAutoSkip();
    }
}
