package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/21.
 */

public class CacheProgress {
    private String videoId;
    private int progress;

    public CacheProgress() {
    }

    public CacheProgress(String videoId) {
        this.videoId = videoId;
    }

    public CacheProgress(String videoId, int progress) {
        this.videoId = videoId;
        this.progress = progress;
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
                '}';
    }
}
