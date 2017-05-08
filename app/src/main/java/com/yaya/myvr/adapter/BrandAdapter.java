package com.yaya.myvr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.BrandDetailActivity;
import com.yaya.myvr.app.AppConst;
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
    public void onBindViewHolder(BrandHolder holder, int position) {
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
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(100);
                v.startAnimation(scaleAnimation);

                // 动画监听
                setAnimationListener(scaleAnimation, bean);
            }
        });
    }

    private void setAnimationListener(ScaleAnimation scaleAnimation, final BrandInfo.DataBean.ListBean bean) {
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(context, BrandDetailActivity.class);
                intent.putExtra(AppConst.BRAND_ID, bean.getId());
                intent.putExtra(AppConst.BRAND_NAME, bean.getName());

                context.startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

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
