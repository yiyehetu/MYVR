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

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.VideoInfoAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.bean.RelativeInfo;
import com.yaya.myvr.bean.VideoInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.RecyclerViewDivider;

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
    @BindView(R.id.rv_relative)
    RecyclerView rvRelative;

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


    @OnClick({R.id.iv_back, R.id.iv_download, R.id.iv_like, R.id.ll_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_download:
                break;
            case R.id.iv_like:
                break;
            // 视频播放
            case R.id.ll_play:
                if(bean == null){
                    return;
                }
                VideoActivity.start(this, AppConst.ONLINE_VIDEO, bean.getM3u8());
                break;
        }
    }
}
