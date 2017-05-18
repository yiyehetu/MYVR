package com.yaya.myvr.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yaya.myvr.R;
import com.yaya.myvr.adapter.LocalVideoAdapter;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.Video;
import com.yaya.myvr.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by admin on 2017/5/15.
 */

public class LocalVideoFragment extends BaseFragment {
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.ll_error)
    LinearLayout llError;

    private MediaTask mediaTask;
    private List<Video> videoList = new ArrayList<>();
    private LocalVideoAdapter localAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvVideo.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        localAdapter = new LocalVideoAdapter(getContext(), videoList);
        rvVideo.setAdapter(localAdapter);

        startTask();
    }

    // 检索本地视频
    class MediaTask extends AsyncTask<Void, Integer, List<Video>> {

        @Override
        protected List<Video> doInBackground(Void... params) {
            return getLoadMedia();
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            pbProgress.setVisibility(View.GONE);
            if (videos.size() == 0) {
                llError.setVisibility(View.VISIBLE);
            } else {
                videoList.addAll(videos);
                localAdapter.notifyDataSetChanged();
            }
        }
    }

    // 获得视频列表
    private List<Video> getLoadMedia() {
        List<Video> videos = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                long time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                String duration = getFormatTime(time);
                Bitmap bitmap = getVideoCover(path);

                Video video = new Video(title, displayName, path, duration, bitmap);
                videos.add(video);
                LogUtils.e(TAG, "video = " + video);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return videos;
    }

    private String getFormatTime(float miliSeconds) {
        miliSeconds = miliSeconds / 1000;
        int hours = (int) (miliSeconds / 3600);
        miliSeconds = miliSeconds - hours * 3600;
        int minutes = (int) (miliSeconds / 60);
        int seconds = (int) (miliSeconds - minutes * 60);
        return String.format("时长: %02d:%02d:%02d", hours, minutes, seconds);
    }

    public Bitmap getVideoCover(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public void onStop() {
        super.onStop();
        clearTask();
    }

    private void startTask() {
        if (mediaTask != null) {
            return;
        }
        mediaTask = new MediaTask();
        mediaTask.execute();
    }

    private void clearTask() {
        if (mediaTask != null && !mediaTask.isCancelled()) {
            mediaTask.cancel(true);
            mediaTask = null;
        }
    }
}
