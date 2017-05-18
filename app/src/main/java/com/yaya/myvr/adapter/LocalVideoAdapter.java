package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.activity.VideoActivity;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.Video;

import java.util.List;

/**
 * Created by admin on 2017/5/15.
 * <p>
 * 视频列表适配器
 */

public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.LocalHolder> {
    private Context context;
    private List<Video> videoList;

    public LocalVideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override
    public LocalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_video_local, parent, false);
        LocalHolder localHolder = new LocalHolder(itemView);
        localHolder.ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
        localHolder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        localHolder.tvDisplayName = (TextView) itemView.findViewById(R.id.tv_displayname);
        localHolder.tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        return localHolder;
    }

    @Override
    public void onBindViewHolder(LocalHolder holder, int position) {
        final Video video = videoList.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.tvDuration.setText(video.getDuration());
        holder.tvDisplayName.setText(video.getDisplayName());
        holder.ivCover.setImageBitmap(video.getBitmap());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.start(context, AppConst.LOCAL_VIDEO, video.getPath(), "3");
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class LocalHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvDisplayName;
        TextView tvDuration;
        View itemView;

        public LocalHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

}
