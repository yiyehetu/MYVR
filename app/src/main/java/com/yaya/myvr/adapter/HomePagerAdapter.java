package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.bean.HomeInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class HomePagerAdapter extends PagerAdapter {
    private Context context;
    private List<HomeInfo.DataBean.LoopViewBean> loopList;
    private int size;

    public HomePagerAdapter(Context context, List<HomeInfo.DataBean.LoopViewBean> loopList) {
        this.context = context;
        this.loopList = loopList;

        size = loopList.size();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_loop, container, false);
        container.addView(itemView);

        TextView tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        ImageView ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
        position = position - position / size * size;
        HomeInfo.DataBean.LoopViewBean bean = loopList.get(position);
        tvTitle.setText(bean.getTitle());


        Glide.with(context)
                .load(bean.getImg())
                .crossFade()
                .centerCrop()
                .into(ivImg);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
