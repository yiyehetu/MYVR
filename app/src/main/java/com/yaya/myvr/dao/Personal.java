package com.yaya.myvr.dao;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by admin on 2017/5/16.
 */
@Table(database=AppDataBase.class)
public class Personal extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    public String deviceId;

    @Column
    public String password;

    @Column
    public String phone;
}
