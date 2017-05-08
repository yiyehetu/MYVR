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
import com.yaya.myvr.bean.TypeInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/8.
 * 分类信息适配器
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<TypeInfo.DataBean> dataList;

    public TypeAdapter(Context context, List<TypeInfo.DataBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_type_viewshow, parent, false);
        TypeHolder typeHolder = new TypeHolder(itemView);
        typeHolder.ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        typeHolder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        typeHolder.tvSum = (TextView) itemView.findViewById(R.id.tv_sum);
        typeHolder.tvMark = (TextView) itemView.findViewById(R.id.tv_mark);
        return typeHolder;
    }

    @Override
    public void onBindViewHolder(TypeHolder holder, int position) {
        TypeInfo.DataBean bean = dataList.get(position);
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
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class TypeHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvTitle;
        TextView tvSum;
        TextView tvMark;

        public TypeHolder(View itemView) {
            super(itemView);
        }
    }
}
