package com.yaya.myvr.dao;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by admin on 2017/5/21.
 *
 * 下载任务
 */

@Table(database = AppDataBase.class)
public class Task extends BaseModel{
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    public String videoId;

    @Column
    public String picture;

    @Column
    public String title;

    @Column
    public String profile;

    @Column
    public String m3u8;

    @Column
    public String format;

    @Column
    public int progress;

    @Column
    public int status;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", videoId='" + videoId + '\'' +
                ", picture='" + picture + '\'' +
                ", title='" + title + '\'' +
                ", profile='" + profile + '\'' +
                ", m3u8='" + m3u8 + '\'' +
                ", format='" + format + '\'' +
                ", progress=" + progress +
                ", status=" + status +
                '}';
    }
}
