package com.yaya.myvr.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.VideoActivity;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.dao.Task;
import com.yaya.myvr.widget.cache.VideoCacheTask2;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

/**
 * Created by admin on 2017/5/18.
 */

public class CacheVideoAdapter extends RecyclerView.Adapter<CacheVideoAdapter.CacheHolder> {
    private static final String TAG = CacheVideoAdapter.class.getSimpleName();
    public static final String PAYLOAD = "payload";

    private Context context;
    private List<Task> taskList;

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

        setListener(holder, task);
    }

    // 局部刷新
    @Override
    public void onBindViewHolder(CacheHolder holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {
            // 设置下载状态
            final Task task = taskList.get(position);
            String state = AppConst.DOWNLOAD_STATUS.get(task.status) + task.progress + "%";
            holder.tvState.setText(state);
            setListener(holder, task);
        } else {
            onBindViewHolder(holder, position);
        }
    }


    class Listener implements View.OnClickListener {
        private Task task;

        public Listener(Task task) {
            this.task = task;
        }

        @Override
        public void onClick(View v) {
            final View alertView = LayoutInflater.from(context).inflate(R.layout.item_alert_cache, null, false);
            Button btn1 = (Button) alertView.findViewById(R.id.btn_1);
            Button btn2 = (Button) alertView.findViewById(R.id.btn_2);
            Button btn3 = (Button) alertView.findViewById(R.id.btn_3);
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(alertView)
                    .show();

            switch (task.status) {
                case AppConst.IDLE:
                    setIdleState(task, dialog, btn1, btn2);
                    break;
                case AppConst.DOWNLOADING:
                    setDownloadingState(task, dialog, btn1);
                    break;
                case AppConst.DOWNLOAD_PAUSE:
                    setPauseState(task, dialog, btn1, btn2, btn3);
                    break;
                case AppConst.DOWNLOADED:
                    setDownloadedState(task, dialog, btn1, btn2);
                    break;
            }
        }

        private void setDownloadedState(final Task task, final AlertDialog dialog, Button btn1, Button btn2) {
            btn1.setText("开始播放");
            btn2.setVisibility(View.VISIBLE);
            btn2.setText("删除缓存");

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo(task);
                    dialog.dismiss();
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteVideo(task);
                    dialog.dismiss();
                }
            });
        }

        private void deleteVideo(Task task) {
            // 本地处理
            final String cacheDir = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
                    .append(File.separator)
                    .append(ApiConst.VIDEO_CACHE)
                    .append(File.separator)
                    .append(task.videoId)
                    .toString();
            File file = new File(cacheDir);
            if (file.exists()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
                // 删除文件夹
                file.delete();
            }
            task.delete();
            int index = taskList.indexOf(task);
            taskList.remove(task);
            notifyItemRangeRemoved(index, 1);
            EventBus.getDefault().post(new AppEvent("update_cache", null));
        }

        private void playVideo(Task task) {
            String m3u8 = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
                    .append(File.separator)
                    .append(ApiConst.VIDEO_CACHE)
                    .append(File.separator)
                    .append(task.videoId)
                    .append(File.separator)
                    .append("new.m3u8")
                    .toString();
            VideoActivity.start(context, AppConst.LOCAL_VIDEO, m3u8, task.format);
        }

        private void setPauseState(final Task task, final AlertDialog dialog, Button btn1, Button btn2, Button btn3) {
            btn1.setText("立即下载");
            btn2.setVisibility(View.VISIBLE);
            btn2.setText("删除缓存");
            btn3.setVisibility(View.VISIBLE);
            btn3.setText("排队下载");

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoCacheTask2.getInstance().handStart(task.videoId);
                    dialog.dismiss();
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteVideo(task);
                    dialog.dismiss();
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPendingTask(task);
                    dialog.dismiss();
                }
            });
        }



        private void setDownloadingState(final Task task, final AlertDialog dialog, Button btn1) {
            btn1.setText("暂停下载");

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoCacheTask2.getInstance().handPause(task.videoId);
                    dialog.dismiss();
                }
            });
        }

        private void setIdleState(final Task task, final AlertDialog dialog, Button btn1, Button btn2) {
            btn1.setText("立即下载");
            btn2.setVisibility(View.VISIBLE);
            btn2.setText("删除任务");

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoCacheTask2.getInstance().handStart(task.videoId);
                    dialog.dismiss();
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePendingTask(task);
                    dialog.dismiss();
                }
            });
        }

        /**
         * 添加待下载任务
         * @param task
         */
        private void addPendingTask(Task task) {
            VideoCacheTask2.getInstance().addPendingTask(task);
        }

        /**
         * 删除待下载任务
         * @param task
         */
        private void deletePendingTask(Task task) {
            VideoCacheTask2.getInstance().removePendingTask(task.videoId);
            deleteVideo(task);
        }
    }

    private void setListener(CacheHolder holder, Task task) {
        holder.itemView.setOnClickListener(new Listener(task));
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
