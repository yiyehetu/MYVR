package com.yaya.myvr.dao;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by adon 2017/5/10.
 *
 * 历史记录表
 */
@Table(database = AppDataBase.class)
public class History extends BaseModel{
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    public String data;

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}
