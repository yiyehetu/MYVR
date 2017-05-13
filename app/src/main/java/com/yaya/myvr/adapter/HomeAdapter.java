package com.yaya.myvr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.TypeActivity;
import com.yaya.myvr.activity.VideoInfoActivity;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.widget.FixedGridView;

import java.util.List;


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

    private int size;
    private int currPostion = 0;

    private HomeHolder headerHolder;

    // 自动滑动标记
    private boolean isAutoIndex = true;

    public boolean getAutoIndex() {
        return isAutoIndex;
    }

    public void setAutoIndex(boolean autoIndex) {
        isAutoIndex = autoIndex;
    }

    public HomeAdapter(HomeInfo.DataBean data, Context context) {
        btnList = data.getBtns();
        homeList = data.getHomeList();
        loopList = data.getLoopView();
        this.context = context;
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LogUtils.e(TAG, "onCreateViewHolder...");
        switch (viewType) {
            case HEADER:
                View headerView = LayoutInflater.from(context).inflate(R.layout.item_home_header, parent, false);
                headerHolder = new HomeHolder(headerView);
                headerHolder.vpHeader = (ViewPager) headerView.findViewById(R.id.vp_header);
//                headerHolder.gvHeader = (FixedGridView) headerView.findViewById(R.id.gv_header);
                headerHolder.gvHeader = (FixedGridView) headerView.findViewById(R.id.gv_header);
                headerHolder.llIndex = (LinearLayout) headerView.findViewById(R.id.ll_index);
                // 设置轮播标记
                size = loopList.size();
                initIndex(headerHolder.llIndex);
                setIndex(headerHolder.llIndex, 0);
                setViewPagerListener(headerHolder.vpHeader, headerHolder.llIndex);
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
//        LogUtils.e(TAG, "onBindViewHolder...");
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

    // 绑定头部
    private void bindHeader(HomeHolder holder) {
        holder.vpHeader.setAdapter(new HomePagerAdapter(context, loopList));
        headerHolder.gvHeader.setAdapter(new HomeGridAdapter(context, btnList));
        headerHolder.gvHeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, TypeActivity.class);
                intent.putExtra(AppConst.TYPE_POSITION, position);
                context.startActivity(intent);
            }
        });
    }

    // 初始化轮播索引
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

    // 设置轮播索引
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
//                        LogUtils.e(TAG, "---->SCROLL_STATE_IDLE");
                        isAutoIndex = true;
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        LogUtils.e(TAG, "---->SCROLL_STATE_DRAGGING");
                        isAutoIndex = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }

    // 执行定时任务
    public void performTask() {
        int postion = (int) (currPostion + 1);
        headerHolder.vpHeader.setCurrentItem(postion);
        int index = postion - postion / size * size;
        setIndex(headerHolder.llIndex, index);
    }


    // 绑定推荐Item
    private void bindRecommend(HomeHolder holder) {
        String adUrl = homeList.get(1).getAdData();
        Glide.with(context)
                .load(adUrl)
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivRecommend);

        final HomeInfo.DataBean.HomeListBean bean = homeList.get(0);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvType.setText(bean.getTitle2());
        final List<HomeInfo.DataBean.HomeListBean.ListBean> list = bean.getList();
        holder.gvRecommend.setAdapter(new HomeGridAdapter2(context, list));

        holder.gvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, VideoInfoActivity.class);
                intent.putExtra(AppConst.VIDEO_ID, list.get(position).getVideoId());
                context.startActivity(intent);
            }
        });
    }

    // 绑定默认Item
    private void bindDefault(HomeHolder holder, int position) {
        final HomeInfo.DataBean.HomeListBean bean = homeList.get(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvType.setText(bean.getTitle2());
        final List<HomeInfo.DataBean.HomeListBean.ListBean> list = bean.getList();
        holder.gvDefault.setAdapter(new HomeGridAdapter2(context, list));

        holder.gvDefault.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, VideoInfoActivity.class);
                intent.putExtra(AppConst.VIDEO_ID, list.get(position).getVideoId());
                context.startActivity(intent);
            }
        });
    }

    static class HomeHolder extends RecyclerView.ViewHolder {
        ViewPager vpHeader;
        //        FixedGridView gvHeader;
        FixedGridView gvHeader;
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
