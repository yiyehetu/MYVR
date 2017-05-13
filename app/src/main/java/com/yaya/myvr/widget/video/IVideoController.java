package com.yaya.myvr.widget.video;

import com.asha.vrlib.MDVRLibrary;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by admin on 2017/5/13.
 */

public interface IVideoController {
    // 获得播放器
    IjkMediaPlayer getPlayer();
    // 初始化播放器
    void initPlayer(MDVRLibrary mdvrLibrary);
    // 跳转
    void seekTo(long positon);
}
