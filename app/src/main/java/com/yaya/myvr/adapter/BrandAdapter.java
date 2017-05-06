package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.bean.BrandInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/4.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandHolder> {
    private Context context;
    private List<BrandInfo.DataBean.ListBean> brandList;


    public BrandAdapter(BrandInfo.DataBean data, Context context) {
        brandList = data.getList();
        this.context = context;
    }

    @Override
    public BrandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_brand, parent, false);
        BrandHolder brandHolder = new BrandHolder(itemView);
        brandHolder.ivLogo = (ImageView) itemView;
        return brandHolder;
    }

    @Override
    public void onBindViewHolder(BrandHolder holder, final int position) {
        final BrandInfo.DataBean.ListBean bean = brandList.get(position);
        Glide.with(context)
                .load(bean.getLogo())
                .placeholder(R.drawable.icon_placeholder_squre)
                .centerCrop()
                .crossFade()
                .into(holder.ivLogo);

        holder.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "id:" + bean.getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    static class BrandHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;

        public BrandHolder(View itemView) {
            super(itemView);
        }
    }
}
