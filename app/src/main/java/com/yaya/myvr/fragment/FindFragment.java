package com.yaya.myvr.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.FindAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.BrandInfo;
import com.yaya.myvr.bean.FindInfo;
import com.yaya.myvr.dao.Brand;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewDivider2;

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
 * <p>
 * 发现页面
 */

public class FindFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.srl_find)
    SwipeRefreshLayout srlFind;
    @BindView(R.id.rv_find)
    RecyclerView rvFind;

    private static final String TAG = FindFragment.class.getSimpleName();
    private Map<String, String> map = new HashMap<>();
    private Map<Integer, Brand> brandMap = new HashMap<>();
    private int currStart = 0;
    private LinearLayoutManager layoutManager;
    private List<FindInfo.DataBean> findList;
    private FindAdapter findAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {
        LogUtils.e("FindFragment init View...");
        tvTitle.setText("发现");

        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "getBrandVideo");
        map.put("limit", "10");

        layoutManager = new LinearLayoutManager(getContext());
        rvFind.setLayoutManager(layoutManager);
        RecyclerViewDivider2 divider = new RecyclerViewDivider2(new ColorDrawable(0xffececec), LinearLayoutManager.VERTICAL);
        divider.setHeight(ConvertUtils.dp2px(getContext(), 8.0f));
        rvFind.addItemDecoration(divider);

        rvFind.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisiableItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (findAdapter == null) {
                    return;
                }

                // 不能再加载
                if (currStart > 90) {
                    findAdapter.setBottomFlag(true);
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisiableItem + 1 == findAdapter.getItemCount()) {
                    // 滑动到底部
                    requestData();
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        // 添加加载刷新
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                srlFind.setRefreshing(true);
                readDataBase();
            }
        });

        srlFind.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reset
                currStart = 0;
                requestData();
            }
        });

    }

    private void readDataBase() {
        List<Brand> brandList = SQLite.select()
                .from(Brand.class)
                .queryList();

        if (brandList.size() == 0) {
            requestBrandData();
        }else{

        }

        requestData();
    }


    @Override
    protected void initData() {
        LogUtils.e("FindFragment init Data...");
    }

    /**
     * 请求发现数据
     */
    private void requestData() {
        map.put("start", currStart + "");

        Subscription subscription = ApiManager.getInstance().getApiService().getFindInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FindInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                        srlFind.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        srlFind.setRefreshing(false);
                    }

                    @Override
                    public void onNext(FindInfo findInfo) {
                        LogUtils.e(TAG, "onNext... findInfo = " + findInfo);
                        bindData(findInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定数据
     *
     * @param findInfo
     */
    private void bindData(FindInfo findInfo) {
        if (findInfo != null && findInfo.getErrCode() == 0) {
            if (currStart == 0) {
                findList = findInfo.getData();
                findAdapter = new FindAdapter(findList, getContext());
                rvFind.setAdapter(findAdapter);
            } else {
                findList.addAll(findInfo.getData());
                findAdapter.notifyDataSetChanged();
            }
            currStart += 10;
        } else {

        }
    }

    /**
     * 请求品牌数据
     */
    private void requestBrandData() {
        Map<String, String> map2 = new HashMap<>();
        map2.putAll(ApiConst.BASE_MAP);
        map2.put("cmd", "getIndex");

        Subscription subscription = ApiManager.getInstance().getApiService().getBrandInfo(map2)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BrandInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(BrandInfo brandInfo) {
                        LogUtils.e(TAG, "onNext... brandInfo = " + brandInfo);
                        WriteData(brandInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    // 写入数据到集合或数据库
    private void WriteData(BrandInfo brandInfo) {
        if(brandInfo != null && brandInfo.getErrCode() == 0){
            List<BrandInfo.DataBean.ListBean> beanList = brandInfo.getData().getList();
            for(int i = 0; i < beanList.size(); i++) {
                BrandInfo.DataBean.ListBean bean = beanList.get(i);
                Brand brand = new Brand();
                brand.brandId = bean.getId();
                brand.logo = bean.getLogo();
                brand.name = bean.getName();
                brand.save();

                brandMap.put(Integer.getInteger(brand.brandId), brand);
            }
        }
    }

}
