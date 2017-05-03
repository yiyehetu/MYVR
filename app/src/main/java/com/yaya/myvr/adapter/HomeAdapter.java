package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.widget.FixedGridView;

import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private static final int HEADER = 100;
    private static final int RECOMMEND = 101;
    private static final int DEFAULT = 102;
    private Context context;
    private List<HomeInfo.DataBean.BtnsBean> btnList;
    private List<HomeInfo.DataBean.HomeListBean> homeList;
    private List<HomeInfo.DataBean.LoopViewBean> loopList;
    private FragmentManager fragmentManager;


    public HomeAdapter(HomeInfo.DataBean data, Context context, FragmentManager fragmentManager) {
        btnList = data.getBtns();
        homeList = data.getHomeList();
        loopList = data.getLoopView();
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                View headerView = LayoutInflater.from(context).inflate(R.layout.item_home_header, parent, false);
                HomeHolder headerHolder = new HomeHolder(headerView);
                headerHolder.vpHeader = (ViewPager) headerView.findViewById(R.id.vp_header);
                headerHolder.gvHeader = (FixedGridView) headerView.findViewById(R.id.gv_header);
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
        holder.gvHeader.setAdapter(new HomeGridAdapter(context, btnList));
        holder.vpHeader.setAdapter(new HomePagerAdapter(fragmentManager, loopList));
        holder.vpHeader.setCurrentItem(0);
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
        FixedGridView gvHeader;

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
