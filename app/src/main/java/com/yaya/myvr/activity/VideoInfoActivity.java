package com.yaya.myvr.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.bean.VideoInfo;
import com.yaya.myvr.util.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 视频信息界面
 */
public class VideoInfoActivity extends BaseActivity {
    private static final String TAG = VideoInfoActivity.class.getSimpleName();

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

    private String videoId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_info;
    }

    @Override
    protected void initView() {

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
                        rlLoad.setVisibility(View.GONE);
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
                VideoInfo.DataBean bean = data.get(0);
                Glide.with(this)
                        .load(bean.getPicture())
                        .placeholder(R.drawable.icon_placeholder)
                        .centerCrop()
                        .crossFade()
                        .into(ivPic);
                tvTitle.setText(bean.getTitle());
                float score = Float.valueOf(bean.getScore());
                rbScore.setRating(5 * score / 10.0f);
                tvScore.setText(score + "");
                tvPlaytimes.setText(bean.getPlayTimes() + "次播放");
                tvYear.setText("年代 : " + bean.getYear());
                tvArea.setText("地区 : " + bean.getArea());
                tvActor.setText("演员 : " + bean.getActor());
                tvDuration.setText("片长 : " + bean.getDuration());
                tvProfile.setText(bean.getProfile());
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_download, R.id.iv_like, R.id.ll_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_download:
                break;
            case R.id.iv_like:
                break;
            case R.id.ll_play:
                break;
        }
    }
}
