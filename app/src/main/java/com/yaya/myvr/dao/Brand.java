package com.yaya.myvr.dao;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by admin on 2017/5/5.
 *
 * 品牌表
 */
@Table(database = AppDataBase.class)
public class Brand extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    public String brandId;

    @Column
    public String logo;

    @Column
    public String name;

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", brandId='" + brandId + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
