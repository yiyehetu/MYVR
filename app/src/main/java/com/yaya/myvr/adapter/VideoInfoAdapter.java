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
import com.yaya.myvr.bean.RelativeInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/9.
 */

public class VideoInfoAdapter extends RecyclerView.Adapter<VideoInfoAdapter.VideoHolder> {
    private List<RelativeInfo.DataBean> dataList;
    private Context context;

    public VideoInfoAdapter(List<RelativeInfo.DataBean> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_video_relative, parent, false);
        VideoHolder holder = new VideoHolder(itemView);
        holder.ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        holder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        final RelativeInfo.DataBean bean = dataList.get(position);
        Glide.with(context)
                .load(bean.getPicture())
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);
        holder.tvTitle.setText(bean.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoInfoActivity.class);
                intent.putExtra(AppConst.VIDEO_ID, bean.getId());
                context.startActivity(intent);

                VideoInfoActivity activity = (VideoInfoActivity) context;
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class VideoHolder extends RecyclerView.ViewHolder{
        ImageView ivPic;
        TextView tvTitle;
        View itemView;

        public VideoHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
