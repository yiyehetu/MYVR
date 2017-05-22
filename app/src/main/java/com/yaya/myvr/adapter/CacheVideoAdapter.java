package com.yaya.myvr.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.dao.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/18.
 */

public class CacheVideoAdapter extends RecyclerView.Adapter<CacheVideoAdapter.CacheHolder> {
    private static final String TAG = CacheVideoAdapter.class.getSimpleName();
    public static final String PAYLOAD = "payload";

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
        holder.tvProfile.setText(task.profile);

        // 设置下载状态
        String state = AppConst.DOWNLOAD_STATUS.get(task.status) + task.progress + "%";
        holder.tvState.setText(state);

        Glide.with(context)
                .load(task.picture)
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);

        setListener(holder, task.status);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //            private boolean isPause = false;
//            @Override
//            public void onClick(View v) {
//                VideoActivity.start(context, AppConst.LOCAL_VIDEO, favor.m3u8, favor.format);

                // 缓存文件路径
//                String m3u8 = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
//                        .append(File.separator)
//                        .append(ApiConst.VIDEO_CACHE)
//                        .append(File.separator)
//                        .append(task.videoId)
//                        .append(File.separator)
//                        .append("new.m3u8")
//                        .toString();
//                VideoActivity.start(context, AppConst.LOCAL_VIDEO, m3u8, task.format);
//                if(isPause){
//                    Toast.makeText(context, "启动任务", Toast.LENGTH_SHORT).show();
//                    VideoCacheTask.getInstance().start(task.videoId);
//                    isPause = false;
//                }else{
//                    Toast.makeText(context, "暂停任务", Toast.LENGTH_SHORT).show();
//                    VideoCacheTask.getInstance().pause(task.videoId);
//                    isPause = true;
//                }
//            }
//        });
    }

    // 局部刷新
    @Override
    public void onBindViewHolder(CacheHolder holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {
            // 设置下载状态
            final Task task = taskList.get(position);
            String state = AppConst.DOWNLOAD_STATUS.get(task.status) + task.progress + "%";
            holder.tvState.setText(state);
            setListener(holder, task.status);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    private void setListener(CacheHolder holder, final int status) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View alertView = LayoutInflater.from(context).inflate(R.layout.item_alert_cache, null, false);
                new AlertDialog.Builder(context)
                            .setView(alertView)
                            .show();

                switch (status) {
                    case AppConst.IDLE:

                        break;
                    case AppConst.DOWNLOADING:
                        break;
                    case AppConst.DOWNLOAD_PAUSE:
                        break;
                    case AppConst.DOWNLOADED:
                        break;
                }
            }
        });
    }

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

}
