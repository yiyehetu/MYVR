package com.yaya.myvr.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/9.
 */

public class VideoInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : [{"actor":"未知","area":"1","brand":"0","cate":"hd","createTime":"1485574057467","duration":"5分钟","format":"3","id":"2298","m3u8":"http://cdn.mystical.cn/thls/tsdir1485573465595/1485573476596_gen.m3u8","modifyTime":"1486974430954","oriUrl":"","picture":"http://pic.firstvr.net.cn/images/orign/2017-2/1486974427458038635.jpg-small","playTimes":"20万+","profile":"央视春晚VR 金鸡报晓","quality":"2","score":"8","title":"金鸡报晓","year":"2016"}]
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
        return "VideoInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * actor : 未知
         * area : 1
         * brand : 0
         * cate : hd
         * createTime : 1485574057467
         * duration : 5分钟
         * format : 3
         * id : 2298
         * m3u8 : http://cdn.mystical.cn/thls/tsdir1485573465595/1485573476596_gen.m3u8
         * modifyTime : 1486974430954
         * oriUrl :
         * picture : http://pic.firstvr.net.cn/images/orign/2017-2/1486974427458038635.jpg-small
         * playTimes : 20万+
         * profile : 央视春晚VR 金鸡报晓
         * quality : 2
         * score : 8
         * title : 金鸡报晓
         * year : 2016
         */

        private String actor;
        private String area;
        private String brand;
        private String cate;
        private String createTime;
        private String duration;
        private String format;
        private String id;
        private String m3u8;
        private String modifyTime;
        private String oriUrl;
        private String picture;
        private String playTimes;
        private String profile;
        private String quality;
        private String score;
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

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public String getOriUrl() {
            return oriUrl;
        }

        public void setOriUrl(String oriUrl) {
            this.oriUrl = oriUrl;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPlayTimes() {
            return playTimes;
        }

        public void setPlayTimes(String playTimes) {
            this.playTimes = playTimes;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
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
                    ", brand='" + brand + '\'' +
                    ", cate='" + cate + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", duration='" + duration + '\'' +
                    ", format='" + format + '\'' +
                    ", id='" + id + '\'' +
                    ", m3u8='" + m3u8 + '\'' +
                    ", modifyTime='" + modifyTime + '\'' +
                    ", oriUrl='" + oriUrl + '\'' +
                    ", picture='" + picture + '\'' +
                    ", playTimes='" + playTimes + '\'' +
                    ", profile='" + profile + '\'' +
                    ", quality='" + quality + '\'' +
                    ", score='" + score + '\'' +
                    ", title='" + title + '\'' +
                    ", year='" + year + '\'' +
                    '}';
        }
    }
}
