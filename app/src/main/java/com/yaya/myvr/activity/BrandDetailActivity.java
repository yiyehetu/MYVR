package com.yaya.myvr.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.adapter.BrandDetailAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.AttentionInfo;
import com.yaya.myvr.bean.BrandBottomInfo;
import com.yaya.myvr.bean.BrandTopInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * 品牌详情页面
 */
public class BrandDetailActivity extends BaseActivity {
    @BindView(R.id.rv_brand)
    RecyclerView rvBrand;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_brand)
    LinearLayout llBrand;
    @BindView(R.id.srl_brand)
    SwipeRefreshLayout srlBrand;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private static final String TAG = BrandDetailActivity.class.getSimpleName();
    private Map<String, String> topMap = new HashMap<>();
    private Map<String, String> bottomMap = new HashMap<>();
    private Map<String, String> attentionMap = new HashMap<>();
    private LinearLayoutManager layoutManager;
    // 底部数据集合
    private List<BrandBottomInfo.DataBean> bottomData = new ArrayList<>();
    private BrandDetailAdapter brandDetailAdapter;
    private static final float BASE_Y = ConvertUtils.dp2px(VRApp.getAppInstance().getApplicationContext(), 240);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_brand_detail;
    }

    @Override
    protected void initView() {
        layoutManager = new LinearLayoutManager(this);
        rvBrand.setLayoutManager(layoutManager);
        RecyclerViewDivider divider = new RecyclerViewDivider(new ColorDrawable(0xffececec), LinearLayoutManager.VERTICAL);
        divider.setHeight(ConvertUtils.dp2px(getContext(), 1.0f));
        rvBrand.addItemDecoration(divider);

        rvBrand.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisiableItem = 0;
            // 透明状态
            private boolean isTransparent = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisiableItem = layoutManager.findFirstVisibleItemPosition();

                // 顶部渐变
                if (firstVisiableItem == 0) {
                    View firstView = layoutManager.findViewByPosition(0);
                    int[] location = new int[2];
                    firstView.getLocationInWindow(location);

                    int currY = Math.abs(location[1]);
//                    LogUtils.e(TAG, "currY = " + currY);
                    if (currY <= BASE_Y) {
                        isTransparent = true;
                        int alpha = (int) (currY / BASE_Y * 255);
                        llBrand.setBackgroundColor(Color.argb(alpha, 47, 79, 79));
                    } else {
                        if (isTransparent) {
                            llBrand.setBackgroundColor(Color.argb(255, 47, 79, 79));
                            isTransparent = false;
                        }
                    }
                }
            }
        });

        srlBrand.setColorSchemeColors(getResources().getColor(R.color.top_nav_background));
        srlBrand.setProgressViewOffset(false, AppConst.SWIPE_START, AppConst.SWIPE_END);
        srlBrand.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                srlBrand.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                srlBrand.setRefreshing(true);
                requestData();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void requestData() {
        String brandId = getIntent().getStringExtra(AppConst.BRAND_ID);
        String brandName = getIntent().getStringExtra(AppConst.BRAND_NAME);
        tvTitle.setText(brandName);
        LogUtils.e(TAG, "brandId = " + brandId);

        topMap.putAll(ApiConst.BASE_MAP);
        topMap.put("cmd", "getBrandInfo");
        topMap.put("id", brandId);
        topMap.put("phone", ApiConst.PHONE);

        bottomMap.putAll(ApiConst.BASE_MAP);
        bottomMap.put("cmd", "getVideoByCate");
        bottomMap.put("brandId", brandId);
        bottomMap.put("start", 0 + "");
        bottomMap.put("limit", 10 + "");

        attentionMap.putAll(ApiConst.BASE_MAP);
        attentionMap.put("brandId", brandId);

        requestTopData();
    }

    /**
     * 请求头部数据
     */
    private void requestTopData() {
        Subscription subscription = ApiManager.getInstance().getApiService().getBrandTopInfo(topMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BrandTopInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                        srlBrand.setRefreshing(false);
                        srlBrand.setEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        srlBrand.setRefreshing(false);
                        rlError.setVisibility(View.VISIBLE);
                        llBrand.setBackgroundColor(Color.argb(255, 47, 79, 79));
                    }

                    @Override
                    public void onNext(BrandTopInfo brandTopInfo) {
                        LogUtils.e(TAG, "onNext... brandTopInfo = " + brandTopInfo);
                        if (rlError.isShown()) {
                            rlError.setVisibility(View.GONE);
                            llBrand.setBackgroundColor(Color.argb(0, 47, 79, 79));
                        }
                        bindTopData(brandTopInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定头部数据
     */
    private void bindTopData(BrandTopInfo brandTopInfo) {
        if (brandTopInfo != null && brandTopInfo.getErrCode() == 0) {
            BrandTopInfo.DataBean topData = brandTopInfo.getData();
            tvTitle.setText(topData.getName());
            brandDetailAdapter = new BrandDetailAdapter(topData, bottomData, this);
            rvBrand.setAdapter(brandDetailAdapter);

            requestBottomData();
        } else {

        }
    }

    /**
     * 请求底部数据
     */
    private void requestBottomData() {
        Subscription subscription = ApiManager.getInstance().getApiService().getBrandBottomInfo(bottomMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BrandBottomInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(BrandBottomInfo brandBottomInfo) {
                        LogUtils.e(TAG, "onNext... brandBottomInfo = " + brandBottomInfo);
                        bindBottomData(brandBottomInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定底部数据
     *
     * @param brandBottomInfo
     */
    private void bindBottomData(BrandBottomInfo brandBottomInfo) {
        if (brandBottomInfo != null && brandBottomInfo.getErrCode() == 0) {
            bottomData.addAll(brandBottomInfo.getData());
            brandDetailAdapter.notifyDataSetChanged();
        } else {

        }
    }

    @OnClick(R.id.iv_back)
    void back(){
        finish();
    }

    @OnClick(R.id.rl_error)
    void reLoad() {
        srlBrand.setRefreshing(true);
        rlError.setVisibility(View.GONE);
        requestData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event){
        if("attention".equals(event.getMark())){
            attentionMap.put("cmd", event.getData());
            attentionMap.put("loginKey", ApiConst.LOGIN_KEY);
            attentionMap.put("phone", ApiConst.PHONE);

            requestAttentionData();
        }
    }

    /**
     * 请求关注
     */
    private void requestAttentionData() {
        Subscription subscription = ApiManager.getInstance().getApiService().getAttentionInfo(attentionMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AttentionInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(AttentionInfo attentionInfo) {
                        LogUtils.e(TAG, "onNext... attentionInfo = " + attentionInfo);
                        if(attentionInfo != null && attentionInfo.getErrCode() == 0){
                            brandDetailAdapter.setAttentionUpdate();
                        }
                    }
                });

        subscriptionList.add(subscription);
    }
}
