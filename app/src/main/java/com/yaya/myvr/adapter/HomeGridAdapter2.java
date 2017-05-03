package com.yaya.myvr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.bean.HomeInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/3.
 * <p>
 * 首页列表GridAdapter
 */

public class HomeGridAdapter2 extends BaseAdapter {
    private Context context;
    private List<HomeInfo.DataBean.HomeListBean.ListBean> list;

    public HomeGridAdapter2(Context context, List<HomeInfo.DataBean.HomeListBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_viewshow, parent, false);
            holder = new ViewHolder();
            holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomeInfo.DataBean.HomeListBean.ListBean bean = list.get(position);

        Glide.with(context)
                .load(bean.getImg())
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);
        holder.tvTitle.setText(bean.getTitle());
        return convertView;
    }

    static class ViewHolder {
        ImageView ivPic;
        TextView tvTitle;
    }

}
