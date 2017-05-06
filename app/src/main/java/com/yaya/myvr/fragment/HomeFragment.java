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
import android.widget.Toast;

import com.yaya.myvr.R;
import com.yaya.myvr.adapter.HomeAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewDivider;
import com.yaya.myvr.widget.VpSwipeRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && firstVisiableItem == 0 && homeAdapter != null) {
                    homeAdapter.setAutoIndex(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisiableItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisiableItem != 0 && homeAdapter != null) {
                    homeAdapter.setAutoIndex(false);
                }

                // 顶部渐变
                if (firstVisiableItem == 0) {
                    View firstView = layoutManager.findViewByPosition(0);
                    int[] location = new int[2];
                    firstView.getLocationInWindow(location);

                    int currY = Math.abs(location[1]);
                    LogUtils.e(TAG, "currY = " + currY);
                    if (currY <= BASE_Y) {
                        int alpha = (int) (currY / BASE_Y * 255);
                        rlSearch.setBackgroundColor(Color.argb(alpha, 47, 79, 79));
                    } else {
                        rlSearch.setBackgroundColor(Color.argb(255, 47, 79, 79));
                    }
                }
            }
        });

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
        } else {
            // 数据有误
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
        Toast.makeText(getContext(), "click search...", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_search)
    void clickScan() {
        Toast.makeText(getContext(), "click scan...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e(TAG, "onStart...");
        if (homeAdapter != null) {
            homeAdapter.performTask();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop...");
        homeAdapter.cancelTask();
    }

}
