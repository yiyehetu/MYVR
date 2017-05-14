package com.yaya.myvr.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.Video;
import com.yaya.myvr.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by admin on 2017/5/2.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_local)
    TextView tvLocal;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.rl_history)
    RelativeLayout rlHistory;
    @BindView(R.id.rl_download)
    RelativeLayout rlDownload;
    @BindView(R.id.rl_suggest)
    RelativeLayout rlSuggest;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    Unbinder unbinder;

    private static final String TAG = MineFragment.class.getSimpleName();
    private MediaLoadTask loadTask;
    private List<Video> videoList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        LogUtils.e("MineFragment init View...");
    }

    @Override
    protected void initData() {
        LogUtils.e("MineFragment init Data...");

        loadTask = new MediaLoadTask();
        loadTask.execute();
    }


    // 检索本地视频
    class MediaLoadTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvLocal.setText("...");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return getLoadMedia();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            tvLocal.setText(integer + "");
        }
    }

    private int getLoadMedia() {
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)); // id
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)); // 专辑
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 显示名称
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小

                Video video = new Video(id, title, album, displayName, path, duration, size);
                videoList.add(video);
                LogUtils.e(TAG, "video = " + video);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return videoList.size();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadTask.cancel(true);
    }

    @OnClick(R.id.ll_local)
    void clickLocalVideo() {

    }
}
