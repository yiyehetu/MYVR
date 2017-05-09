package com.yaya.myvr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.VideoInfoActivity;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.TypeInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/8.
 * 分类信息适配器
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeHolder> {
    public static final int FOOTER = 100;
    public static final int DEFAULT = 101;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<TypeInfo.DataBean> dataList;
    private boolean isBottom = false;

    public TypeAdapter(Context context, List<TypeInfo.DataBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER:
                View footerView = LayoutInflater.from(context).inflate(R.layout.item_find_bottom, parent, false);
                TypeHolder footerHolder = new TypeHolder(footerView);
                footerHolder.tvBottom = (TextView) footerView;
                return footerHolder;
            default:
                View itemView = layoutInflater.inflate(R.layout.item_type_viewshow, parent, false);
                TypeHolder typeHolder = new TypeHolder(itemView);
                typeHolder.ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
                typeHolder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                typeHolder.tvSum = (TextView) itemView.findViewById(R.id.tv_sum);
                typeHolder.tvMark = (TextView) itemView.findViewById(R.id.tv_mark);

                return typeHolder;
        }
    }

    @Override
    public void onBindViewHolder(TypeHolder holder, int position) {
        switch (getItemViewType(position)) {
            case FOOTER:
                bindFooter(holder);
                break;
            case DEFAULT:
                bindDefault(holder, position);
                break;
        }
    }

    private void bindFooter(TypeHolder holder) {
        if (isBottom) {
            holder.tvBottom.setText("");
        } else if (dataList.size() >= 20) {
            holder.tvBottom.setText("正在拼命加载中...");
        } else {
            holder.tvBottom.setText("");
        }
    }

    private void bindDefault(TypeHolder holder, int position) {
        final TypeInfo.DataBean bean = dataList.get(position);
        Glide.with(context)
                .load(bean.getPicture())
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvSum.setText(bean.getPlayTimes());

        String format = bean.getFormat();
        if ("3".equals(format)) {
            holder.tvMark.setText("360");
        } else {
            holder.tvMark.setText("3D");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoInfoActivity.class);
                intent.putExtra(AppConst.VIDEO_ID, bean.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dataList.size()) {
            return FOOTER;
        } else {
            return DEFAULT;
        }
    }

    public void setBottomFlag(boolean bottomFlag) {
        this.isBottom = bottomFlag;
    }

    static class TypeHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView ivPic;
        TextView tvTitle;
        TextView tvSum;
        TextView tvMark;

        TextView tvBottom;

        public TypeHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
