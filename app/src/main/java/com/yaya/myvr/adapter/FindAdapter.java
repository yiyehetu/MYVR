package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.bean.FindInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/4.
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.FindHolder> {
    private static final int DEFAULT = 101;
    private static final int BOTTOM = 102;
    private List<FindInfo.DataBean> findList;
    private Context context;

    // 底部标记
    private boolean isBottom = false;

    public FindAdapter(List<FindInfo.DataBean> findList, Context context) {
        this.findList = findList;
        this.context = context;
    }

    @Override
    public FindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DEFAULT:
                View defaultView = LayoutInflater.from(context).inflate(R.layout.item_find_default, parent, false);
                FindHolder defaultHolder = new FindHolder(defaultView);
                defaultHolder.ivPic = (ImageView) defaultView.findViewById(R.id.iv_pic);
                defaultHolder.tvTitle = (TextView) defaultView.findViewById(R.id.tv_title);
                return defaultHolder;
            default:
                View bottomView = LayoutInflater.from(context).inflate(R.layout.item_find_bottom, parent, false);
                FindHolder bottomHolder = new FindHolder(bottomView);
                bottomHolder.tvBottom = (TextView) bottomView;
                return bottomHolder;
        }

    }

    @Override
    public void onBindViewHolder(FindHolder holder, int position) {
        switch (getItemViewType(position)) {
            case DEFAULT:
                bindDefault(holder, position);
                break;
            case BOTTOM:
                bindBottom(holder);
                break;
        }
    }

    private void bindDefault(FindHolder holder, int position) {
        FindInfo.DataBean bean = findList.get(position);
        Glide.with(context)
                .load(bean.getPicture())
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);
        holder.tvTitle.setText(bean.getTitle());
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
            return BOTTOM;
        }
    }

    public void setBottomFlag(boolean bottomFlag) {
        isBottom = bottomFlag;
    }

    static class FindHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvTitle;

        TextView tvBottom;

        public FindHolder(View itemView) {
            super(itemView);
        }
    }
}
