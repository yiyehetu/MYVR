package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/20.
 */

public class VideoPath {
    private String originPath;
    private String newPath;

    public VideoPath() {
    }

    public VideoPath(String originPath, String newPath) {
        this.originPath = originPath;
        this.newPath = newPath;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    @Override
    public String toString() {
        return "VideoPath{" +
                "originPath='" + originPath + '\'' +
                ", newPath='" + newPath + '\'' +
                '}';
    }
}
