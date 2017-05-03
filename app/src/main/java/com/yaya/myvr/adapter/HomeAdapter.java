package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.FixedGridView;
import com.yaya.myvr.widget.XGridView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by admin on 2017/5/3.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private static final String TAG = HomeAdapter.class.getSimpleName();
    private static final int HEADER = 100;
    private static final int RECOMMEND = 101;
    private static final int DEFAULT = 102;
    private Context context;
    private List<HomeInfo.DataBean.BtnsBean> btnList;
    private List<HomeInfo.DataBean.HomeListBean> homeList;
    private List<HomeInfo.DataBean.LoopViewBean> loopList;
    private Subscription subscription;
    private int size;
    private int currPostion = 0;

    private HomeHolder headerHolder;

    public HomeAdapter(HomeInfo.DataBean data, Context context) {
        btnList = data.getBtns();
        homeList = data.getHomeList();
        loopList = data.getLoopView();
        this.context = context;
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.e(TAG, "onCreateViewHolder...");
        switch (viewType) {
            case HEADER:
                View headerView = LayoutInflater.from(context).inflate(R.layout.item_home_header, parent, false);
                headerHolder = new HomeHolder(headerView);
                headerHolder.vpHeader = (ViewPager) headerView.findViewById(R.id.vp_header);
//                headerHolder.gvHeader = (FixedGridView) headerView.findViewById(R.id.gv_header);
                headerHolder.gvHeader = (XGridView) headerView.findViewById(R.id.gv_header);
                headerHolder.llIndex = (LinearLayout) headerView.findViewById(R.id.ll_index);
                // 设置轮播标记
                size = loopList.size();
                initIndex(headerHolder.llIndex);
                setIndex(headerHolder.llIndex, 0);
                setViewPagerListener(headerHolder.vpHeader, headerHolder.llIndex);
                performTask();
                //
                headerHolder.gvHeader.setAdapter(new HomeGridAdapter(context, btnList));
                headerHolder.gvHeader.setOnItemClickListener(new XGridView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int positon) {
                        Toast.makeText(context.getApplicationContext(), "positon:" + positon, Toast.LENGTH_SHORT).show();
                    }
                });

                return headerHolder;
            case RECOMMEND:
                View recommendView = LayoutInflater.from(context).inflate(R.layout.item_home_recommend, parent, false);
                HomeHolder recommendHolder = new HomeHolder(recommendView);
                recommendHolder.gvRecommend = (FixedGridView) recommendView.findViewById(R.id.gv_recommend);
                recommendHolder.ivRecommend = (ImageView) recommendView.findViewById(R.id.iv_recommend);
                recommendHolder.tvTitle = (TextView) recommendView.findViewById(R.id.tv_title);
                recommendHolder.tvType = (TextView) recommendView.findViewById(R.id.tv_type);
                return recommendHolder;
            default:
                View defaultView = LayoutInflater.from(context).inflate(R.layout.item_home_default, parent, false);
                HomeHolder defaultHolder = new HomeHolder(defaultView);
                defaultHolder.gvDefault = (FixedGridView) defaultView.findViewById(R.id.gv_default);
                defaultHolder.tvTitle = (TextView) defaultView.findViewById(R.id.tv_title);
                defaultHolder.tvType = (TextView) defaultView.findViewById(R.id.tv_type);
                return defaultHolder;
        }
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else if (position == 1) {
            return RECOMMEND;
        } else {
            return DEFAULT;
        }
    }

    @Override
    public void onBindViewHolder(HomeHolder holder, int position) {
        LogUtils.e(TAG, "onBindViewHolder...");
        switch (getItemViewType(position)) {
            case HEADER:
                bindHeader(holder);
                break;
            case RECOMMEND:
                bindRecommend(holder);
                break;
            case DEFAULT:
                bindDefault(holder, position);
                break;
        }
    }

    private void bindHeader(HomeHolder holder) {
        holder.vpHeader.setAdapter(new HomePagerAdapter(context, loopList));
    }

    private void initIndex(LinearLayout linearLayout) {
        for (int i = 0; i < size; i++) {
            ImageView ivIndex = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_home_index, linearLayout, false);
            linearLayout.addView(ivIndex);
            if (i != size - 1) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ivIndex.getLayoutParams();
                layoutParams.rightMargin = ConvertUtils.dp2px(context, 5);
                ivIndex.setLayoutParams(layoutParams);
            }
        }
    }

    private void setIndex(LinearLayout linearLayout, int index) {
        for (int i = 0; i < size; i++) {
            ImageView ivIndex = (ImageView) linearLayout.getChildAt(i);
            if (i == index) {
                ivIndex.setSelected(true);
            } else {
                ivIndex.setSelected(false);
            }
        }
    }

    private void setViewPagerListener(final ViewPager viewPager, final LinearLayout linearLayout) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currPostion = position;
                position = position - position / size * size;
                setIndex(linearLayout, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        LogUtils.e(TAG, "---->SCROLL_STATE_IDLE");
                        performTask();
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        LogUtils.e(TAG, "---->SCROLL_STATE_DRAGGING");
                        cancelTask();
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        LogUtils.e(TAG, "---->SCROLL_STATE_SETTLING");
                        cancelTask();
                        break;
                }
            }
        });
    }

    public void performTask() {
        if (headerHolder == null) {
            return;
        }

        subscription = Observable.interval(5, 5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        int postion = (int) (currPostion + aLong + 1);
                        LogUtils.e(TAG, "postion = " + postion);
                        headerHolder.vpHeader.setCurrentItem(postion);
                        int index = postion - postion / size * size;
                        LogUtils.e(TAG, "index = " + index);
                        setIndex(headerHolder.llIndex, index);
                    }
                });
    }

    public void cancelTask() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void bindRecommend(HomeHolder holder) {
        String adUrl = homeList.get(1).getAdData();
        Glide.with(context)
                .load(adUrl)
                .centerCrop()
                .crossFade()
                .into(holder.ivRecommend);

        HomeInfo.DataBean.HomeListBean bean = homeList.get(0);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvType.setText(bean.getTitle2());
        holder.gvRecommend.setAdapter(new HomeGridAdapter2(context, bean.getList()));
    }

    private void bindDefault(HomeHolder holder, int position) {
        HomeInfo.DataBean.HomeListBean bean = homeList.get(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvType.setText(bean.getTitle2());
        holder.gvDefault.setAdapter(new HomeGridAdapter2(context, bean.getList()));
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        ViewPager vpHeader;
//        FixedGridView gvHeader;
        XGridView gvHeader;
        LinearLayout llIndex;

        FixedGridView gvRecommend;
        ImageView ivRecommend;

        FixedGridView gvDefault;

        TextView tvTitle;
        TextView tvType;

        public HomeHolder(View itemView) {
            super(itemView);
        }
    }
}
