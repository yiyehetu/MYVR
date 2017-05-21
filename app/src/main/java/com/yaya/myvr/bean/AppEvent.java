package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/8.
 */

public class AppEvent {
    private String mark;
    private String data;
    private Object obj;

    public AppEvent(String mark, String data) {
        this.mark = mark;
        this.data = data;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
    public AppEvent(String mark, Object obj){
        this.mark = mark;
        this.obj = obj;
    }

    public String getMark() {
        return mark;
    }

    public String getData() {
        return data;
    }
}
