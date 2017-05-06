package com.yaya.myvr.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/6.
 */

public class BrandBottomInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : [{"actor":"未知","area":"1","brand":"3","cate":"sport","createTime":"1473821782582","duration":"4分钟","format":"3","id":"1567","m3u8":"http://static.mystical.com.cn/hls/mts_1473819346471.m3u8","modifyTime":"1473821782582","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473821763445595026.png-small","playTimes":"17万+","profile":"FANC球迷","score":"8","title":"FANC球迷","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"sport","createTime":"1473821341279","duration":"1分30秒","format":"3","id":"1566","m3u8":"http://static.mystical.com.cn/hls/mts_1473819284809.m3u8","modifyTime":"1473821341279","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473821330938431764.png-small","playTimes":"21万+","profile":"FANC赛车","score":"8","title":"FANC赛车","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"entertainment","createTime":"1473821259626","duration":"2分钟","format":"3","id":"1565","m3u8":"http://static.mystical.com.cn/hls/mts_1473819247588.m3u8","modifyTime":"1473821259626","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473821254056149229.png-small","playTimes":"17万+","profile":"FANC卖场二","score":"8","title":"FANC卖场二","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"entertainment","createTime":"1473821136669","duration":"2","format":"3","id":"1564","m3u8":"http://static.mystical.com.cn/hls/mts_1473819205980.m3u8","modifyTime":"1473821136669","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473821066134054988.png-small","playTimes":"16万+","profile":"FANC卖场","score":"8","title":"FANC卖场","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"sport","createTime":"1473820756885","duration":"3分钟","format":"3","id":"1563","m3u8":"http://static.mystical.com.cn/hls/mts_1473819112081.m3u8","modifyTime":"1473820756885","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473820745158316465.png-small","playTimes":"18万+","profile":"匹克-感受中国","score":"8","title":"匹克-感受中国","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"sport","createTime":"1473820614936","duration":"1分钟","format":"3","id":"1562","m3u8":"http://static.mystical.com.cn/hls/mts_1473819074343.m3u8","modifyTime":"1473820614936","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473820593694986558.jpg-small","playTimes":"18万+","profile":"维尔姆斯中国行","score":"8","title":"维尔姆斯中国行","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"sport","createTime":"1473820511973","duration":"2分钟","format":"3","id":"1561","m3u8":"http://static.mystical.com.cn/hls/mts_1473818985227.m3u8","modifyTime":"1473820511973","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473820496800289042.png-small","playTimes":"16万+","profile":"维尔姆斯中国行-第3天","score":"8","title":"维尔姆斯中国行-第3天","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"hd","createTime":"1473820042771","duration":"2分钟","format":"3","id":"1560","m3u8":"http://static.mystical.com.cn/hls/mts_1473818896841.m3u8","modifyTime":"1473820042771","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473820040551636325.jpg-small","playTimes":"22万+","profile":"匹克联盟-中国行第4天","score":"8","title":"匹克联盟-中国行第4天","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"sport","createTime":"1473734765571","duration":"3分钟","format":"3","id":"1543","m3u8":"http://static.mystical.com.cn/hls/mts_1473734613239.m3u8","modifyTime":"1473734765571","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473734753405138789.png-small","playTimes":"17万+","profile":"足球练习","score":"8","title":"足球练习","year":"2016"},{"actor":"未知","area":"1","brand":"3","cate":"hd","createTime":"1473734494019","duration":"4分钟","format":"3","id":"1542","m3u8":"http://static.mystical.com.cn/hls/mts_1473734322627.m3u8","modifyTime":"1473734494019","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473734473562702886.jpg-small","playTimes":"17万+","profile":"Adidas Away DAYS\u2014The premiere of 'Away Days'","score":"8","title":"运动比赛","year":"2016"}]
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
        return "BrandBottomInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * actor : 未知
         * area : 1
         * brand : 3
         * cate : sport
         * createTime : 1473821782582
         * duration : 4分钟
         * format : 3
         * id : 1567
         * m3u8 : http://static.mystical.com.cn/hls/mts_1473819346471.m3u8
         * modifyTime : 1473821782582
         * picture : http://pic.firstvr.net.cn/images/orign/2016-9/1473821763445595026.png-small
         * playTimes : 17万+
         * profile : FANC球迷
         * score : 8
         * title : FANC球迷
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
        private String picture;
        private String playTimes;
        private String profile;
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
                    ", picture='" + picture + '\'' +
                    ", playTimes='" + playTimes + '\'' +
                    ", profile='" + profile + '\'' +
                    ", score='" + score + '\'' +
                    ", title='" + title + '\'' +
                    ", year='" + year + '\'' +
                    '}';
        }
    }
}
