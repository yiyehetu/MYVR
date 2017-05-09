package com.yaya.myvr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.BrandDetailActivity;
import com.yaya.myvr.activity.VideoInfoActivity;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.FindInfo;
import com.yaya.myvr.dao.Brand;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/4.
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.FindHolder> {
    private static final String TAG = FindAdapter.class.getSimpleName();
    private static final int DEFAULT = 101;
    private static final int FOOTER = 102;
    private List<FindInfo.DataBean> findList;
    private Context context;
    private Map<Integer, Brand> brandMap;

    // 底部标记
    private boolean isBottom = false;

    public FindAdapter(List<FindInfo.DataBean> findList, Context context, Map<Integer, Brand> brandMap) {
        this.findList = findList;
        this.context = context;
        this.brandMap = brandMap;
    }

    @Override
    public FindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER:
                View footerView = LayoutInflater.from(context).inflate(R.layout.item_find_bottom, parent, false);
                FindHolder footerHolder = new FindHolder(footerView);
                footerHolder.tvBottom = (TextView) footerView;
                return footerHolder;
            default:
                View defaultView = LayoutInflater.from(context).inflate(R.layout.item_find_default, parent, false);
                FindHolder defaultHolder = new FindHolder(defaultView);
                defaultHolder.ivPic = (ImageView) defaultView.findViewById(R.id.iv_pic);
                defaultHolder.tvTitle = (TextView) defaultView.findViewById(R.id.tv_title);
                defaultHolder.llBrand = (LinearLayout) defaultView.findViewById(R.id.ll_brand);
                defaultHolder.ivLogo = (ImageView) defaultView.findViewById(R.id.iv_logo);
                defaultHolder.tvName = (TextView) defaultView.findViewById(R.id.tv_name);
                defaultHolder.tvTimes = (TextView) defaultView.findViewById(R.id.tv_times);
                return defaultHolder;
        }

    }

    @Override
    public void onBindViewHolder(FindHolder holder, int position) {
        switch (getItemViewType(position)) {
            case DEFAULT:
                bindDefault(holder, position);
                break;
            case FOOTER:
                bindBottom(holder);
                break;
        }
    }

    private void bindDefault(FindHolder holder, int position) {
        final FindInfo.DataBean bean = findList.get(position);
        Glide.with(context)
                .load(bean.getPicture())
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);
        holder.tvTitle.setText(bean.getTitle());

        final Brand brand = brandMap.get(Integer.valueOf(bean.getBrand()));
        Glide.with(context)
                .load(brand.logo)
                .placeholder(R.drawable.icon_placeholder_squre)
                .centerCrop()
                .crossFade()
                .into(holder.ivLogo);
        holder.tvName.setText(brand.name);
        holder.tvTimes.setText(bean.getPlayTimes());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoInfoActivity.class);
                intent.putExtra(AppConst.VIDEO_ID, bean.getId());
                context.startActivity(intent);
            }
        });

        holder.llBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrandDetailActivity.class);
                intent.putExtra(AppConst.BRAND_ID, brand.brandId);
                intent.putExtra(AppConst.BRAND_NAME, brand.name);
                context.startActivity(intent);
            }
        });
    }

    private void bindBottom(FindHolder holder) {
        if (isBottom) {
            holder.tvBottom.setText("不能再请求了哦...");
        } else {
            holder.tvBottom.setText("正在拼命加载中...");
        }
    }

    @Override
    public int getItemCount() {
        return findList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < findList.size()) {
            return DEFAULT;
        } else {
            return FOOTER;
        }
    }

    public void setBottomFlag(boolean bottomFlag) {
        isBottom = bottomFlag;
    }

    static class FindHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView ivPic;
        TextView tvTitle;
        LinearLayout llBrand;
        ImageView ivLogo;
        TextView tvName;
        TextView tvTimes;

        TextView tvBottom;

        public FindHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
