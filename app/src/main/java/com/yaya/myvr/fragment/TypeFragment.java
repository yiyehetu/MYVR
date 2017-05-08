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
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rlType.setLayoutManager(layoutManager);
        rlType.addItemDecoration(new RecyclerViewGridDivider(getContext()));

        srlType.setColorSchemeColors(getResources().getColor(R.color.top_nav_background));
        srlType.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currStart = 0;
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
        if (getUserVisibleHint() && isViewInited && !isDataLoaded) {
            lazyLoadData();
        }

        if (getUserVisibleHint() && datalist != null && !datalist.isEmpty()) {
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
                        if (currStart == 0) {
                            srlType.setRefreshing(false);
                        }
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
                        if (currStart == 0 && pbType.isShown()) {
                            pbType.setVisibility(View.GONE);
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
            if (currStart == 0) {
                datalist = typeInfo.getData();
                typeAdapter = new TypeAdapter(getContext(), datalist);
                rlType.setAdapter(typeAdapter);
            } else {

            }
        }
    }

}
