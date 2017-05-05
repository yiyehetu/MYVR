package com.yaya.myvr.dao;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by min on 2017/5/5.
 *
 * 本地数据库
 */

@Database(name = AppDataBase.NAME, version = AppDataBase.VERSION)
public class AppDataBase {
    public static final String NAME = "MYVR"; // we will add the .db extension

    public static final int VERSION = 1;
}
