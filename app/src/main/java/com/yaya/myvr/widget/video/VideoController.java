package com.yaya.myvr.widget.video;

import android.content.Context;
import android.net.Uri;

import com.asha.vrlib.MDVRLibrary;
import com.yaya.myvr.util.LogUtils;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by admin on 2017/5/13.
 */

public class VideoController implements IVideoController, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = VideoController.class.getSimpleName();
    // 播放状态
    private static final int STATUS_IDLE = 0;
    private static final int STATUS_INITIALIZED = 1;
    private static final int STATUS_PREPARING = 2;
    private static final int STATUS_PREPARED = 3;
    private static final int STATUS_STARTED = 4;
    private static final int STATUS_PAUSED = 5;
    private static final int STATUS_STOPPED = 6;
    private static final int STATUS_COMPLETED = 7;

    private int mStatus = STATUS_IDLE;

    private IjkMediaPlayer mPlayer;
    private IControllerView controllerView;
    private MDVRLibrary mdvrLibrary;


    public VideoController(IControllerView controllerView) {
        this.controllerView = controllerView;
        // idle状态
        mPlayer = new IjkMediaPlayer();
    }


    public IjkMediaPlayer getPlayer() {
        return mPlayer;
    }

    @Override
    public void initPlayer(MDVRLibrary mdvrLibrary) {
        this.mdvrLibrary = mdvrLibrary;
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnVideoSizeChangedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        enableHardwareDecoding();
    }

    @Override
    public void seekTo(long positon) {
        if (mPlayer == null) {
            return;
        }

        if (mStatus == STATUS_PREPARED || mStatus == STATUS_STARTED || mStatus == STATUS_PAUSED || mStatus == STATUS_COMPLETED) {
            LogUtils.e(TAG, "seekto begin...");
            mPlayer.seekTo(positon);
        }
    }

    private void enableHardwareDecoding() {
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
    }

    /**
     * 进入prepared状态
     *
     * @param iMediaPlayer
     */
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        LogUtils.e(TAG, "onPrepared...");
        mStatus = STATUS_PREPARED;
        controllerView.showTotalTime();
        controllerView.hideLoadView();
        start();
        if (mdvrLibrary != null) {
            // 通知变化
            mdvrLibrary.notifyPlayerChanged();
        }
    }

    /**
     * 打开远程文件
     *
     * @param path
     */
    public void openRemoteFile(String path) {
        try {
            mPlayer.setDataSource(path);
            mStatus = STATUS_INITIALIZED;
            // 进入Initialized状态
            LogUtils.e(TAG, "Initialized...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开远程文件
     *
     * @param uri
     */
    public void openLocalFile(Context context, Uri uri) {
        try {
            mPlayer.setDataSource(context, uri);
            // 进入Initialized状态
            mStatus = STATUS_INITIALIZED;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepare() {
        LogUtils.e(TAG, "prepare...");
        if (mPlayer == null) {
            return;
        }

        if (mStatus == STATUS_INITIALIZED || mStatus == STATUS_STOPPED) {
            mPlayer.prepareAsync();
            mStatus = STATUS_PREPARING;
            LogUtils.e(TAG, "preparing...");
        }
    }

    public void stop() {
        if (mPlayer == null) {
            return;
        }

        if (mStatus == STATUS_STARTED || mStatus == STATUS_PAUSED) {
            mPlayer.stop();
            mStatus = STATUS_STOPPED;
        }
    }

    public void pause() {
        if (mPlayer == null) {
            return;
        }

        if (mPlayer.isPlaying() && mStatus == STATUS_STARTED) {
            mPlayer.pause();
            mStatus = STATUS_PAUSED;
        }
    }

    private void start() {
        if (mPlayer == null) {
            return;
        }

        if (mStatus == STATUS_PREPARED || mStatus == STATUS_PAUSED || mStatus == STATUS_COMPLETED) {
            mPlayer.start();
            mStatus = STATUS_STARTED;
            // 开始计时
            controllerView.startTimer();
            controllerView.startMenuTimer();
        }

    }

    public boolean isPlaying() {
        return mStatus == STATUS_STARTED;
    }

    public void resume() {
        start();
    }

    public void destroy() {
        stop();
        if (mPlayer != null) {
            mPlayer.setSurface(null);
            mPlayer.release();
        }
        mPlayer = null;
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        String error = String.format("Play Error what=%d extra=%d", what, extra);
        controllerView.showPlayError(error);
        return true;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sar_num, int sar_den) {
        if (mdvrLibrary != null) {
            mdvrLibrary.onTextureResize(width, height);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (mPlayer == null) {
            return;
        }

        if (mStatus == STATUS_STARTED) {
            mStatus = STATUS_COMPLETED;
            controllerView.showCompletedView();
        }
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        LogUtils.e(TAG, "seekto end...");
        controllerView.hideLoadView();

        if (mStatus == STATUS_STARTED) {
            controllerView.startTimer();
        }

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        LogUtils.e(TAG, "onBufferingUpdate..." + percent);
        controllerView.showBufferProgress(percent);
    }
}
