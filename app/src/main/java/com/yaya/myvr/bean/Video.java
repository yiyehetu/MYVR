package com.yaya.myvr.bean;

import android.graphics.Bitmap;

/**
 * Created by admin on 2017/5/14.
 */

public class Video {
    private String title;
    private String displayName;
    private String path;
    private String duration;
    private Bitmap bitmap;

    public Video(String title, String displayName, String path, String duration, Bitmap bitmap) {
        this.title = title;
        this.displayName = displayName;
        this.path = path;
        this.duration = duration;
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", duration='" + duration + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
