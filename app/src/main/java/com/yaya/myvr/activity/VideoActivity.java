package com.yaya.myvr.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asha.vrlib.MDVRLibrary;
import com.yaya.myvr.R;
import com.yaya.myvr.app.AppManager;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.video.IControllerView;
import com.yaya.myvr.widget.video.VideoController;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 视频播放
 */
public class VideoActivity extends BaseActivity implements IControllerView {
    @BindView(R.id.svVideo)
    GLSurfaceView svVideo;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.cb_mode)
    CheckBox cbMode;
    @BindView(R.id.cb_play)
    CheckBox cbPlay;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.tvTotalTime)
    TextView tvTotalTime;
    @BindView(R.id.awake_menu)
    View awakeMenu;

    private static final String TAG = VideoActivity.class.getSimpleName();
    private MDVRLibrary mdvrLibrary;
    private VideoController videoController;
    private float duration;
    // 菜单显示
    private boolean isShowing = true;

    private Subscription timerSubscription;
    private Subscription menuSubscription;
    private float prePositon = 0.0f;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        // 模式切换
        cbMode.setChecked(true);
        cbMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mdvrLibrary == null) {
                    return;
                }
                mdvrLibrary.switchDisplayMode(VideoActivity.this);
            }
        });

        // 播放切换
        cbPlay.setChecked(true);
        cbPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (videoController == null) {
                    return;
                }

                if (isChecked) {
                    videoController.resume();
                } else {
                    videoController.pause();
                }
            }
        });

        // 播放位置
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private float miliSeconds;
            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }

                this.progress = progress;
                miliSeconds = progress / 200.0f * duration;
                tvTime.setText(getFormatTime(miliSeconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 停止计时
                stopTimer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (videoController == null) {
                    return;
                }

                showLoadView();
                if (progress >= 198) {
                    videoController.seekTo(0);
                } else {
                    videoController.seekTo((long) (miliSeconds));
                }

            }
        });
    }

    @Override
    protected void doBeforeSetContentView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppManager.getInstance().addActivity(this);
        // 设置横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initController();
    }

    /**
     * 初始化控制器
     */
    private void initController() {
        videoController = new VideoController(this);
        initVRlibrary();
        videoController.initPlayer(mdvrLibrary);

        String path = "/storage/0266-2DB4/DCIM/Camera/20161005_132423.mp4";

//        videoController.openLocalFile(this, Uri.parse(path));
        videoController.openRemoteFile("http://cache.utovr.com/201508270528174780.m3u8");
//        videoController.openRemoteFile("http://cache.utovr.com/s1oc3vlhxbt9mugwjz/L2_1920_3_25.m3u8");
        videoController.prepare();
    }

    private void initVRlibrary() {
        mdvrLibrary = MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_GLASS)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH)
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        videoController.getPlayer().setSurface(surface);
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                        Toast.makeText(VideoActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .build(svVideo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mdvrLibrary.onResume(this);
        videoController.resume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mdvrLibrary.onPause(this);
        videoController.pause();
        stopTimer();
        clearMenuTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdvrLibrary.onDestroy();
        videoController.destroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mdvrLibrary.onOrientationChanged(this);
    }

    @Override
    public void hideLoadView() {
        if (pbProgress.isShown()) {
            pbProgress.setVisibility(View.INVISIBLE);
        }
        // 3秒后隐藏菜单
//        hideMenu();
    }

    @Override
    public void showLoadView() {
        if (!pbProgress.isShown()) {
            pbProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCompletedView() {
        cbPlay.setChecked(false);
        clearMenuTimer();
        btnBack.setVisibility(View.VISIBLE);
        rlMenu.setVisibility(View.VISIBLE);
        cbMode.setVisibility(View.VISIBLE);
        isShowing = true;
    }

    @Override
    public void showPlayError(String errorMsg) {
        Toast.makeText(VideoActivity.this, "error:" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBufferProgress(int percent) {
        sbProgress.setSecondaryProgress(percent * 2);
    }

    @Override
    public void startTimer() {
        if (timerSubscription != null) {
            return;
        }

        timerSubscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return videoController.isPlaying();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        float position = videoController.getPlayer().getCurrentPosition();
                        // 卡顿处理
                        if (position - prePositon < 500) {
                            showLoadView();
                        } else {
                            hideLoadView();
                        }

                        LogUtils.e(TAG, "position = " + position);
                        // 莫名超出处理
                        tvTime.setText(getFormatTime(position));

                        float progress = position / duration * 200;
                        int value = (int) progress;
                        LogUtils.e(TAG, "progress = " + progress + ", value = " + value);
                        sbProgress.setProgress(value);
                        prePositon = position;
                    }
                });
    }

    @Override
    public void stopTimer() {
        if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
            timerSubscription = null;
        }
    }

    @Override
    public void showTotalTime() {
        duration = videoController.getPlayer().getDuration();
        tvTotalTime.setText(" / " + getFormatTime(duration));
    }

    @Override
    public void switchMenuView() {
        if (isShowing) {
            btnBack.setVisibility(View.GONE);
            rlMenu.setVisibility(View.GONE);
            cbMode.setVisibility(View.GONE);
            isShowing = false;
        } else {
            btnBack.setVisibility(View.VISIBLE);
            rlMenu.setVisibility(View.VISIBLE);
            cbMode.setVisibility(View.VISIBLE);
            isShowing = true;
            startMenuTimer();
        }
    }

    @Override
    public void startMenuTimer() {
        clearMenuTimer();
        menuSubscription = Observable.timer(5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        btnBack.setVisibility(View.GONE);
                        rlMenu.setVisibility(View.GONE);
                        cbMode.setVisibility(View.GONE);
                        isShowing = false;
                    }
                });
    }

    private void clearMenuTimer() {
        if (menuSubscription != null && !menuSubscription.isUnsubscribed()) {
            menuSubscription.unsubscribe();
            menuSubscription = null;
        }
    }

    private String getFormatTime(float miliSeconds) {
        miliSeconds = miliSeconds / 1000;
        int hours = (int) (miliSeconds / 3600);
        miliSeconds = miliSeconds - hours * 3600;
        int minutes = (int) (miliSeconds / 60);
        int seconds = (int) (miliSeconds - minutes * 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @OnClick(R.id.rl_menu)
    void clickRelativeMenu() {
        LogUtils.e(TAG, "relativelayout onclick...");
        switchMenuView();
    }

    @OnClick(R.id.awake_menu)
    void clickAwakeMenu() {
        LogUtils.e(TAG, "view onclick...");
        switchMenuView();
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }
}
