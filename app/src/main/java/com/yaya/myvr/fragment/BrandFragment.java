package com.yaya.myvr.fragment;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.adapter.BrandAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.BrandInfo;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewGridDivider;

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
 * <p>
 * 品牌页面
 */

public class BrandFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.srl_brand)
    SwipeRefreshLayout srlBrand;
    @BindView(R.id.rv_brand)
    RecyclerView rvBrand;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private static final String TAG = BrandFragment.class.getSimpleName();
    private Map<String, String> map = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_brand;
    }

    @Override
    protected void initView() {
        LogUtils.e("BrandFragment init View...");
        tvTitle.setText("品牌");

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rvBrand.setLayoutManager(layoutManager);
        rvBrand.addItemDecoration(new RecyclerViewGridDivider(getContext()));

        // 添加加载刷新
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                srlBrand.setRefreshing(true);
                requestData();
            }
        });

        srlBrand.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
    }

    @Override
    protected void initData() {
        LogUtils.e("BrandFragment init Data...");

    }

    /**
     * 请求数据
     */
    private void requestData() {
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "getBrandList");

        Subscription subscription = ApiManager.getInstance().getApiService().getBrandInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BrandInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                        srlBrand.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        srlBrand.setRefreshing(false);
                        rlError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(BrandInfo brandInfo) {
                        LogUtils.e(TAG, "onNext... brandInfo = " + brandInfo);
                        if (rlError.isShown()) {
                            rlError.setVisibility(View.GONE);
                        }
                        bindData(brandInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定数据
     *
     * @param brandInfo
     */
    private void bindData(BrandInfo brandInfo) {
        if (brandInfo != null && brandInfo.getErrCode() == 0) {
            BrandAdapter brandAdapter = new BrandAdapter(brandInfo.getData(), getContext());
            rvBrand.setAdapter(brandAdapter);

        } else {

        }
    }

    @OnClick(R.id.rl_error)
    void reLoad() {
        rlError.setVisibility(View.GONE);
        srlBrand.setRefreshing(true);
        requestData();
    }
}
