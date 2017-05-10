package com.yaya.myvr.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/10.
 */

public class SearchInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : [{"actor":"理查德·劳斯伯格，瑞斯·维克菲尔德，爱丽丝·帕金逊","area":"3","brand":"0","cate":"movie","createTime":"1467775223283","duration":"109分钟","format":"2","id":"107","m3u8":"http://static.mystical.com.cn/tsdir1467553795027/1467563302516_gen.m3u8","modifyTime":"1485074059465","picture":"http://pic.firstvr.net.cn/images/orign/2017-1/1485074046842176998.jpg-small","playTimes":"24万+","profile":"在巴布亚新几内亚某地一个人迹罕见的原始丛林腹地，有一个号称洞穴之母的巨洞\u2014\u2014伊萨·阿拉。腰缠万贯的探险家卡尔（Ioan Gruffudd 饰）一掷千金，雇用拥有丰富洞穴探险经验的弗兰克（Richard Roxburgh 饰）及其团队对这里进行勘查。转眼之间几个月过去，伊萨·阿拉的勘探虽然颇见成效，但进度仍显缓慢。与此同时，强烈的热带风暴即将到来，洞穴探险迫在眉睫。卡尔为此和弗兰克发生争吵，而后者孤注一掷，和早已身心疲惫的搭档茱德（Allison Cratchley 饰）潜入水中，挑战有着\u201c魔鬼之限\u201d之称的凶险通道。另一方面，卡尔的女友维多莉亚和弗兰克的儿子乔希也到洞中探险。殊不知灭顶之灾正在逼近\u2026\u2026","score":"7.4","title":"夺命深渊","year":"2011"}]
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
        return "SearchInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * actor : 理查德·劳斯伯格，瑞斯·维克菲尔德，爱丽丝·帕金逊
         * area : 3
         * brand : 0
         * cate : movie
         * createTime : 1467775223283
         * duration : 109分钟
         * format : 2
         * id : 107
         * m3u8 : http://static.mystical.com.cn/tsdir1467553795027/1467563302516_gen.m3u8
         * modifyTime : 1485074059465
         * picture : http://pic.firstvr.net.cn/images/orign/2017-1/1485074046842176998.jpg-small
         * playTimes : 24万+
         * profile : 在巴布亚新几内亚某地一个人迹罕见的原始丛林腹地，有一个号称洞穴之母的巨洞——伊萨·阿拉。腰缠万贯的探险家卡尔（Ioan Gruffudd 饰）一掷千金，雇用拥有丰富洞穴探险经验的弗兰克（Richard Roxburgh 饰）及其团队对这里进行勘查。转眼之间几个月过去，伊萨·阿拉的勘探虽然颇见成效，但进度仍显缓慢。与此同时，强烈的热带风暴即将到来，洞穴探险迫在眉睫。卡尔为此和弗兰克发生争吵，而后者孤注一掷，和早已身心疲惫的搭档茱德（Allison Cratchley 饰）潜入水中，挑战有着“魔鬼之限”之称的凶险通道。另一方面，卡尔的女友维多莉亚和弗兰克的儿子乔希也到洞中探险。殊不知灭顶之灾正在逼近……
         * score : 7.4
         * title : 夺命深渊
         * year : 2011
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
