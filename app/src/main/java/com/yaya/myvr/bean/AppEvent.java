package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/8.
 */

public class AppEvent {
    private String mark;
    private String data;

    public AppEvent(String mark, String data) {
        this.mark = mark;
        this.data = data;
    }

    public String getMark() {
        return mark;
    }

    public String getData() {
        return data;
    }
}
