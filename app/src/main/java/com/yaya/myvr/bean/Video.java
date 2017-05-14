package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/14.
 */

public class Video {
    private int id;
    private String title;
    private String album;
    private String displayName;
    private String path;
    private long duration;
    private long size;

    public Video(int id, String title, String album, String displayName, String path, long duration, long size) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.displayName = displayName;
        this.path = path;
        this.duration = duration;
        this.size = size;
    }

    public Video() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }
}
