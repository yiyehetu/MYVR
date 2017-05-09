package com.yaya.myvr.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/9.
 */

public class RelativeInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : [{"actor":"未知","area":"1","cate":"hd","duration":"5分钟","format":"3","id":"2297","m3u8":"http://cdn.mystical.cn/thls/tsdir1485571979510/1485571996006_gen.m3u8","modifyTime":"1485572315021","picture":"http://pic.firstvr.net.cn/images/orign/2017-1/1485572297834084498.png-small","profile":"春晚 清风VR","title":"[春晚]清风VR","year":"2016"},{"actor":"未知","area":"1","cate":"hd","duration":"5分钟","format":"3","id":"2296","m3u8":"http://cdn.mystical.cn/thls/tsdir1485571135021/1485571151152_gen.m3u8","modifyTime":"1485571337144","picture":"http://pic.firstvr.net.cn/images/orign/2017-1/1485571302571116621.png-small","profile":"央视VR春晚2017","title":"中国骄傲","year":"2016"},{"actor":"未知","area":"1","cate":"hd","duration":"1分40秒","format":"3","id":"2294","m3u8":"http://cdn.mystical.cn/thls/tsdir1485236995327/1485237003170_gen.m3u8","modifyTime":"1485237181167","picture":"http://pic.firstvr.net.cn/images/orign/2017-1/1485237176624229073.jpg-small","profile":"全CG制作，从一颗螺丝钉变成超炫酷跑车","title":"炫酷的兰博基尼CG","year":"2016"}]
     */

    private int errCode;
    private String errMsg;
    private List<DataBean> data;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RelativeInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * actor : 未知
         * area : 1
         * cate : hd
         * duration : 5分钟
         * format : 3
         * id : 2297
         * m3u8 : http://cdn.mystical.cn/thls/tsdir1485571979510/1485571996006_gen.m3u8
         * modifyTime : 1485572315021
         * picture : http://pic.firstvr.net.cn/images/orign/2017-1/1485572297834084498.png-small
         * profile : 春晚 清风VR
         * title : [春晚]清风VR
         * year : 2016
         */

        private String actor;
        private String area;
        private String cate;
        private String duration;
        private String format;
        private String id;
        private String m3u8;
        private String modifyTime;
        private String picture;
        private String profile;
        private String title;
        private String year;

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getM3u8() {
            return m3u8;
        }

        public void setM3u8(String m3u8) {
            this.m3u8 = m3u8;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "actor='" + actor + '\'' +
                    ", area='" + area + '\'' +
                    ", cate='" + cate + '\'' +
                    ", duration='" + duration + '\'' +
                    ", format='" + format + '\'' +
                    ", id='" + id + '\'' +
                    ", m3u8='" + m3u8 + '\'' +
                    ", modifyTime='" + modifyTime + '\'' +
                    ", picture='" + picture + '\'' +
                    ", profile='" + profile + '\'' +
                    ", title='" + title + '\'' +
                    ", year='" + year + '\'' +
                    '}';
        }
    }
}
