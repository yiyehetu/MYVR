package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.CacheProgress;
import com.yaya.myvr.dao.Task;
import com.yaya.myvr.widget.VideoCacheTask;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/18.
 */

public class CacheVideoAdapter extends RecyclerView.Adapter<CacheVideoAdapter.CacheHolder> {
    private static final String TAG = CacheVideoAdapter.class.getSimpleName();

    private Context context;
    private List<Task> taskList;
    private Map<String, CacheHolder> map = new HashMap<>();
    private Map<String, String> stateMap = new HashMap<>();

    public CacheVideoAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public CacheHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_video_cache, parent, false);
        CacheHolder cacheHolder = new CacheHolder(itemView);
        cacheHolder.ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        cacheHolder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        cacheHolder.tvState = (TextView) itemView.findViewById(R.id.tv_state);
        cacheHolder.tvProfile = (TextView) itemView.findViewById(R.id.tv_profile);
        return cacheHolder;
    }

    @Override
    public void onBindViewHolder(final CacheHolder holder, int position) {
        final Task task = taskList.get(position);
        holder.tvTitle.setText(task.title);
//        holder.tvActor.setText("演员:" + favor.actor);
        holder.tvProfile.setText(task.profile);

        // 设置下载状态
        map.put(task.videoId, holder);
        holder.tvState.setTag(task.videoId);

        String state = stateMap.get(task.videoId);
        if (TextUtils.isEmpty(state)) {
            state = AppConst.DOWNLOAD_STATUS.get(task.status) + task.progress + "%";
            stateMap.put(task.videoId, state);
        }
        holder.tvState.setText(state);

        Glide.with(context)
                .load(task.picture)
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                VideoActivity.start(context, AppConst.LOCAL_VIDEO, favor.m3u8, favor.format);

                if(isPause){
                    Toast.makeText(context, "启动任务", Toast.LENGTH_SHORT).show();
                    VideoCacheTask.getInstance().start(task.videoId);
                    isPause = false;
                }else{
                    Toast.makeText(context, "暂停任务", Toast.LENGTH_SHORT).show();
                    VideoCacheTask.getInstance().pause(task.videoId);
                    isPause = true;
                }
            }
        });
    }

    private boolean isPause = false;

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    static class CacheHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvTitle;
        TextView tvState;
        TextView tvProfile;
        View itemView;

        public CacheHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event) {
        CacheProgress data = (CacheProgress) event.getObj();
        String videoId = data.getVideoId();
        String state = "";

        if ("download_pending".equals(event.getMark())) {
            state = "下载中...0%";
        } else if ("download_progress".equals(event.getMark())) {
            int progress = data.getProgress();
            if (progress == 100) {
                state = "下载完成...100%";
            } else {
                state = "下载中..." + progress + "%";
            }
        } else if ("download_pause".equals(event.getMark())) {
            int progress = data.getProgress();
            state = "下载暂停..." + progress + "%";
        }

        stateMap.put(videoId, state);
        CacheHolder cacheHolder = map.get(data.getVideoId());
        if (cacheHolder == null) {
            return;
        }
        if (videoId.equals(cacheHolder.tvState.getTag())) {
            cacheHolder.tvState.setText(state);
        }

    }

}
