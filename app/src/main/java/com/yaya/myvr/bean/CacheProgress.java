package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/21.
 *
 * 缓存进度消息
 */

public class CacheProgress {
    private String videoId;
    private int progress;
    private int status;

    public CacheProgress() {
    }

    public CacheProgress(String videoId) {
        this.videoId = videoId;
    }

    public CacheProgress(String videoId, int progress) {
        this.videoId = videoId;
        this.progress = progress;
    }

    public CacheProgress(String videoId, int progress, int status) {
        this.videoId = videoId;
        this.progress = progress;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "CacheProgress{" +
                "videoId='" + videoId + '\'' +
                ", progress=" + progress +
                ", status=" + status +
                '}';
    }
}
