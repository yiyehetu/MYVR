package com.yaya.myvr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.yaya.myvr.R;
import com.yaya.myvr.adapter.TypeAdapter;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.TypeInfo;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewGridDivider;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/5/8.
 */

public class TypeFragment extends BaseFragment {
    @BindView(R.id.rl_type)
    RecyclerView rlType;
    @BindView(R.id.pb_type)
    ProgressBar pbType;
    @BindView(R.id.srl_type)
    SwipeRefreshLayout srlType;

    private static final String TAG = TypeFragment.class.getSimpleName();
    private Map<String, String> map = new HashMap<>();
    private boolean isViewInited = false;
    private boolean isDataLoaded = false;
    private int currStart = 0;
    private int currPostion;
    private String cate;
    private String tag;
    private TypeAdapter typeAdapter;
    private List<TypeInfo.DataBean> datalist;
    private GridLayoutManager layoutManager;
    private boolean isBottom = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取当前位置及cate信息
        Bundle args = getArguments();
        currPostion = args.getInt(AppConst.TYPE_POSITION);
        cate = args.getString(AppConst.TYPE_CATE);
        tag = args.getString(AppConst.TYPE_TAG);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_type;
    }

    @Override
    protected void initView() {
        if (isViewInited) {
            return;
        }

//        Log.e(TAG, this.hashCode() + "initView..." + currPostion);
        layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // size一共为2
                switch (typeAdapter.getItemViewType(position)) {
                    case TypeAdapter.FOOTER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });

        rlType.setLayoutManager(layoutManager);
        rlType.addItemDecoration(new RecyclerViewGridDivider(getContext()));
        rlType.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisiableItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(isBottom){
                    return;
                }

                if (typeAdapter == null) {
                    return;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisiableItem + 1 == typeAdapter.getItemCount()) {
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

        srlType.setColorSchemeColors(getResources().getColor(R.color.top_nav_background));
        srlType.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reset
                currStart = 0;
                isBottom = false;
                requestData();
            }
        });

        isViewInited = true;
    }

    @Override
    protected void initData() {
        if (isDataLoaded) {
            return;
        }

//        LogUtils.e(TAG, this.hashCode() + "initData..." + currPostion);
        if (getUserVisibleHint()) {
            lazyLoadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        LogUtils.e(TAG, this.hashCode() + "isVisibleToUser = " + isVisibleToUser);
        if (!getUserVisibleHint()) {
            return;
        }

        // 位于当前
        if (isViewInited && !isDataLoaded) {
            lazyLoadData();
        }

        if (datalist != null && !datalist.isEmpty()) {
            EventBus.getDefault().post(new AppEvent("type_first_pic", datalist.get(0).getPicture()));
        }
    }

    /**
     * 懒加载数据
     * <p>
     * 只执行一次
     */
    private void lazyLoadData() {
        isDataLoaded = true;
        map.put("cmd", "getVideoByCate");
        map.put("cate", cate);
        map.put("limit", "20");
        map.put("tag", tag);

        requestData();

        LogUtils.e(TAG, Log.e(TAG, this.hashCode() + "lazyLoadData..." + currPostion));
    }

    /**
     * 请求数据
     */
    private void requestData() {
        map.put("start", currStart + "");

        Subscription subscription = ApiManager.getInstance().getApiService().getTypeInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TypeInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        if (currStart == 0) {
                            srlType.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onNext(TypeInfo typeInfo) {
                        LogUtils.e(TAG, "onNext... typeInfo = " + typeInfo);
                        if (currStart == 0) {
                            if (pbType.isShown()) {
                                pbType.setVisibility(View.GONE);
                            }
                            srlType.setRefreshing(false);
                        }

                        bindData(typeInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定数据
     *
     * @param typeInfo
     */
    private void bindData(TypeInfo typeInfo) {
        if (typeInfo != null && typeInfo.getErrCode() == 0) {
            List<TypeInfo.DataBean> datas = typeInfo.getData();
            if (datas.isEmpty()) {
                LogUtils.e(TAG, "datas = 沒有數據了");
                // 刷新最后一项
                isBottom = true;
                typeAdapter.setBottomFlag(true);
                typeAdapter.notifyItemChanged(typeAdapter.getItemCount() - 1);
                return;
            }

            if (currStart == 0) {
                datalist = datas;
                // 第一次加载数据发送图片url
                if (datalist != null && !datalist.isEmpty()) {
                    EventBus.getDefault().post(new AppEvent("type_first_pic", datalist.get(0).getPicture()));
                }

                typeAdapter = new TypeAdapter(getContext(), datalist);
                rlType.setAdapter(typeAdapter);
            } else {
                datalist.addAll(datas);
                typeAdapter.notifyDataSetChanged();
            }
            currStart += 20;
        }
    }

}
