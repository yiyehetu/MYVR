package com.yaya.myvr.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.VideoInfoAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.RelativeInfo;
import com.yaya.myvr.bean.VideoInfo;
import com.yaya.myvr.bean.VideoPath;
import com.yaya.myvr.dao.Favor;
import com.yaya.myvr.dao.Favor_Table;
import com.yaya.myvr.dao.Task;
import com.yaya.myvr.dao.Task_Table;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewDivider;
import com.yaya.myvr.widget.cache.VideoCacheTask2;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * 视频信息界面
 */
public class VideoInfoActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_mark)
    TextView tvMark;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.ll_play)
    LinearLayout llPlay;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_actor)
    TextView tvActor;
    @BindView(R.id.tv_profile)
    TextView tvProfile;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_format)
    TextView tvFormat;
    @BindView(R.id.rb_score)
    AppCompatRatingBar rbScore;
    @BindView(R.id.tv_playtimes)
    TextView tvPlaytimes;
    @BindView(R.id.rl_load)
    RelativeLayout rlLoad;
    @BindView(R.id.rv_relative)
    RecyclerView rvRelative;

    private static final String TAG = VideoInfoActivity.class.getSimpleName();
    private String videoId;
    private VideoInfo.DataBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_info;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRelative.setLayoutManager(layoutManager);
        RecyclerViewDivider divider = new RecyclerViewDivider(new ColorDrawable(Color.WHITE), LinearLayoutManager.HORIZONTAL);
        divider.setWidth(ConvertUtils.dp2px(getContext(), 2.0f));
        rvRelative.addItemDecoration(divider);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        videoId = getIntent().getStringExtra(AppConst.VIDEO_ID);
        LogUtils.e(TAG, "videoId = " + videoId);

        requestVideoInfoData();
        requestRelativeInfoData();
    }

    private void requestVideoInfoData() {
        Map<String, String> map = new HashMap<>();
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "getVideoInfoNormal");
        map.put("id", videoId);

        ApiManager.getInstance().getApiService().getVideoInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideoInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(VideoInfo videoInfo) {
                        LogUtils.e(TAG, "onNext... videoInfo = " + videoInfo);
                        bindVideoInfoData(videoInfo);
                    }
                });
    }

    /**
     * 绑定视频信息
     *
     * @param videoInfo
     */
    private void bindVideoInfoData(VideoInfo videoInfo) {
        if (videoInfo != null && videoInfo.getErrCode() == 0) {
            List<VideoInfo.DataBean> data = videoInfo.getData();
            if (data != null && !data.isEmpty()) {
                bean = data.get(0);
                Glide.with(this)
                        .load(bean.getPicture())
                        .placeholder(R.drawable.icon_placeholder)
                        .centerCrop()
                        .crossFade()
                        .into(ivPic);
                tvTitle.setText(bean.getTitle());
                tvMark.setText(AppConst.DICT_QUALITY.get(bean.getQuality()));
                float score = Float.valueOf(bean.getScore());
                rbScore.setRating(5 * score / 10.0f);
                tvScore.setText(score + "");
                tvPlaytimes.setText(bean.getPlayTimes() + "次播放");
                tvYear.setText("年代 : " + bean.getYear());
                tvArea.setText("地区 : " + AppConst.DICT_AREA.get(bean.getArea()));
                tvActor.setText("演员 : " + bean.getActor());
                tvDuration.setText("片长 : " + bean.getDuration());
                tvFormat.setText("格式 : " + AppConst.DICT_FORMAT.get(bean.getFormat()));
                tvProfile.setText(bean.getProfile());

                // 本地读取是否喜欢
                List<Favor> favorList = SQLite.select()
                        .from(Favor.class)
                        .where(Favor_Table.videoId.eq(bean.getId()))
                        .queryList();
                if (favorList != null && favorList.size() > 0) {
                    ivLike.setSelected(true);
                }
            }
        }
    }

    private void requestRelativeInfoData() {
        Map<String, String> map = new HashMap<>();
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "getVideoRelative");
        map.put("id", videoId);

        ApiManager.getInstance().getApiService().getRelativeInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RelativeInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                        rlLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(RelativeInfo relativeInfo) {
                        LogUtils.e(TAG, "onNext... relativeInfo = " + relativeInfo);
                        bindRelativeInfoData(relativeInfo);
                    }
                });
    }

    private void bindRelativeInfoData(RelativeInfo relativeInfo) {
        if (relativeInfo != null && relativeInfo.getErrCode() == 0) {
            List<RelativeInfo.DataBean> data = relativeInfo.getData();
            if (!data.isEmpty()) {
                rvRelative.setAdapter(new VideoInfoAdapter(data, this));
            }
        }
    }

    @OnClick(R.id.iv_back)
    void clickBack() {
        finish();
    }

    @OnClick(R.id.iv_download)
    void clickDownload() {
        if (bean == null) {
            return;
        }
        download();
    }

    @OnClick(R.id.iv_like)
    void clickLike() {
        if (ApiConst.IS_LOGIN) {
            switchLikeState();
        } else {
            MineActivity.start(this, AppConst.LOGIN);
        }
    }

    @OnClick(R.id.ll_play)
    void clickPlay() {
        if (bean == null) {
            return;
        }
        VideoActivity.start(this, AppConst.ONLINE_VIDEO, bean.getM3u8(), bean.getFormat());
    }

    private void download() {
        List<Task> tasks = SQLite.select()
                .from(Task.class)
                .where(Task_Table.videoId.eq(bean.getId()))
                .queryList();

        if (tasks != null && tasks.size() > 0) {
            Toast.makeText(VideoInfoActivity.this, "已在缓存列表中", Toast.LENGTH_SHORT).show();
        } else {
            Task task = new Task();
            task.videoId = bean.getId();
            task.picture = bean.getPicture();
            task.m3u8 = bean.getM3u8();
            task.format = bean.getFormat();
            task.profile = bean.getProfile();
            task.title = bean.getTitle();
            task.progress = 0;
            task.status = AppConst.IDLE;
            task.save();
            VideoCacheTask2.getInstance().addTask(task);
            EventBus.getDefault().post(new AppEvent("update_cache", null));
            Toast.makeText(VideoInfoActivity.this, "添加缓存成功", Toast.LENGTH_SHORT).show();
        }


//        final String cacheDir = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
//                .append(File.separator)
//                .append(ApiConst.VIDEO_CACHE)
//                .append(File.separator)
//                .append(bean.getId())
//                .append(File.separator)
//                .toString();
//        final String path = cacheDir + "origin.m3u8";
//
//        LogUtils.e(TAG, "path = " + path);
//
//        FileDownloader.getImpl().create(m3u8)
//                .setPath(path)
//                .setListener(new FileDownloadListener() {
//                    @Override
//                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        LogUtils.e(TAG, "pending...soFarBytes = " + soFarBytes + ", totalBytes = " + totalBytes);
//                    }
//
//                    @Override
//                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        LogUtils.e(TAG, "progress...soFarBytes = " + soFarBytes + ", totalBytes = " + totalBytes);
//                    }
//
//                    @Override
//                    protected void completed(BaseDownloadTask task) {
//                        ivDownload.setSelected(true);
//                        LogUtils.e(TAG, "completed..." + task.getSmallFileTotalBytes());
//                        List<VideoPath> pathList = readData(path, cacheDir);
//                        if (pathList != null && pathList.size() > 0) {
//                            downloadAllFile(pathList);
//                        }
//                    }
//
//                    @Override
//                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//                    }
//
//                    @Override
//                    protected void error(BaseDownloadTask task, Throwable e) {
//                        ivDownload.setSelected(false);
//                        LogUtils.e(TAG, "error..." + e.getMessage());
//                    }
//
//                    @Override
//                    protected void warn(BaseDownloadTask task) {
//
//                    }
//                }).start();
    }

    private void downloadAllFile(List<VideoPath> pathList) {
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);

        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            BaseDownloadTask task = FileDownloader.getImpl()
                    .create(pathList.get(i).getOriginPath())
                    .setPath(pathList.get(i).getNewPath())
                    .setTag(i);
            tasks.add(task);
        }

//        queueSet.disableCallbackProgressTimes();
        // do not want each task's download progress's callback,
        // we just consider which task will completed.

        // auto retry 1 time if download fail
        queueSet.setAutoRetryTimes(1);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }

    final FileDownloadListener downloadListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            LogUtils.e(TAG, "tag:" + task.getTag() + "_pending...soFarBytes = " + soFarBytes + ", totalBytes = " + totalBytes);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            LogUtils.e(TAG, "tag:" + task.getTag() + "_progress...soFarBytes = " + soFarBytes + ", totalBytes = " + totalBytes);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            LogUtils.e(TAG, "tag:" + task.getTag() + "_completed...");
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {

        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            LogUtils.e(TAG, "tag:" + etag + "connected...soFarBytes = " + soFarBytes + ", totalBytes = " + totalBytes);
        }
    };


    private List<VideoPath> readData(String path, String cacheDir) {
        List<VideoPath> list = new ArrayList<>();
        int count = 0;

        File newFile = new File(cacheDir + "new.m3u8");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        FileWriter fileWriter = null;
        try {
            fileReader = new FileReader(new File(path));
            bufferedReader = new BufferedReader(fileReader);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            fileWriter = new FileWriter(newFile);

            String buffer = null;
            while ((buffer = bufferedReader.readLine()) != null) {
                if (buffer.length() > 0 && buffer.startsWith("http://")) {
                    // 添加到集合
                    VideoPath videoPath = new VideoPath();
                    videoPath.setOriginPath(buffer);
                    String newPath;
                    if (buffer.endsWith("ts")) {
                        newPath = cacheDir + count + ".ts";
                    } else {
                        newPath = cacheDir + count + ".ds";
                    }
                    videoPath.setNewPath(newPath);
                    list.add(videoPath);
                    count++;

                    // 写入本地
                    LogUtils.e(TAG, "newPath = " + newPath);
                    fileWriter.write(newPath + "\n");
                } else {
                    fileWriter.write(buffer + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 切换喜欢状态
     */
    private void switchLikeState() {
        if (bean == null) {
            return;
        }

        if (ivLike.isSelected()) {
            ivLike.setSelected(false);
            // 删除
            SQLite.delete()
                    .from(Favor.class)
                    .where(Favor_Table.videoId.eq(bean.getId()))
                    .query();
        } else {
            ivLike.setSelected(true);
            // 添加
            Favor favor = new Favor();
            favor.actor = bean.getActor();
            favor.profile = bean.getProfile();
            favor.m3u8 = bean.getM3u8();
            favor.picture = bean.getPicture();
            favor.title = bean.getTitle();
            favor.videoId = bean.getId();
            favor.format = bean.getFormat();
            favor.save();
        }
        EventBus.getDefault().post(new AppEvent("update_favor", null));
    }
}
