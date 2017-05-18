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
import com.yaya.myvr.activity.VideoActivity;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.dao.Favor;

import java.util.List;

/**
 * Created by admin on 2017/5/18.
 */

public class FavorVideoAdapter extends RecyclerView.Adapter<FavorVideoAdapter.FavorHolder> {
    private Context context;
    private List<Favor> favorList;

    public FavorVideoAdapter(Context context, List<Favor> favorList) {
        this.context = context;
        this.favorList = favorList;
    }

    @Override
    public FavorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_video_favor, parent, false);
        FavorHolder favorHolder = new FavorHolder(itemView);
        favorHolder.ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        favorHolder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        favorHolder.tvActor = (TextView) itemView.findViewById(R.id.tv_actor);
        favorHolder.tvProfile = (TextView) itemView.findViewById(R.id.tv_profile);
        return favorHolder;
    }

    @Override
    public void onBindViewHolder(FavorHolder holder, int position) {
        final Favor favor = favorList.get(position);
        holder.tvTitle.setText(favor.title);
        holder.tvActor.setText("演员:" + favor.actor);
        holder.tvProfile.setText(favor.profile);

        Glide.with(context)
                .load(favor.picture)
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.start(context, AppConst.LOCAL_VIDEO, favor.m3u8, favor.format);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorList.size();
    }

    static class FavorHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvTitle;
        TextView tvActor;
        TextView tvProfile;
        View itemView;

        public FavorHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

}
