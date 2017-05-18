package com.yaya.myvr.dao;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by admin on 2017/5/18.
 */
@Table(database = AppDataBase.class)
public class Favor extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    public String videoId;

    @Column
    public String picture;

    @Column
    public String title;

    @Column
    public String actor;

    @Column
    public String profile;

    @Column
    public String m3u8;

    @Column
    public String format;

    @Override
    public String toString() {
        return "Favor{" +
                "id=" + id +
                ", videoId='" + videoId + '\'' +
                ", picture='" + picture + '\'' +
                ", title='" + title + '\'' +
                ", actor='" + actor + '\'' +
                ", profile='" + profile + '\'' +
                ", m3u8='" + m3u8 + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
