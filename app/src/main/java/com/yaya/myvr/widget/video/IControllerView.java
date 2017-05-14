package com.yaya.myvr.widget.video;

/**
 * Created by admin on 2017/5/13.
 */

public interface IControllerView {
    // 隐藏加载
    void hideLoadView();
    // 显示加载
    void showLoadView();
    // 显示结束
    void showCompletedView();
    // 显示播放错误
    void showPlayError(String errorMsg);
    // 显示缓冲进度
    void showBufferProgress(int percent);

    // 开始计时
    void startTimer();
    // 停止计时
    void stopTimer();
    // 显示总时间
    void showTotalTime();
    // 切换显示菜单
    void switchMenuView();

    // 菜单计时
    void startMenuTimer();
}
