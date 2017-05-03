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
 */

public class HomeGridAdapter extends BaseAdapter {
    private Context context;
    private List<HomeInfo.DataBean.BtnsBean> btnList;

    public HomeGridAdapter(Context context, List<HomeInfo.DataBean.BtnsBean> btnList) {
        this.context = context;
        this.btnList = btnList;
    }

    @Override
    public int getCount() {
        return btnList.size();
    }

    @Override
    public Object getItem(int position) {
        return btnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_typeshow, parent, false);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomeInfo.DataBean.BtnsBean bean = btnList.get(position);
        Glide.with(context)
                .load(bean.getImg())
                .fitCenter()
                .crossFade()
                .into(holder.ivIcon);
        holder.tvType.setText(bean.getCategoryName());
        return convertView;
    }

    static class ViewHolder{
        ImageView ivIcon;
        TextView tvType;
    }
}
