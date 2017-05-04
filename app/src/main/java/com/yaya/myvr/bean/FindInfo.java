package com.yaya.myvr.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/4.
 */

public class FindInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : [{"actor":"未知","area":"1","brand":"2","cate":"tour","createTime":"1471857839794","duration":"3分钟","format":"3","id":"711","m3u8":"http://static.mystical.com.cn/hls/mts_1471876176828.m3u8","modifyTime":"1475224797309","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1474515478960353286.jpg-small","playTimes":"61万+","profile":"第一VR、旅人说影视，联合出品。带你走进美丽的十度，享受自然。","score":"8","title":"北京十度","year":"2016"},{"actor":"未知","area":"3","brand":"5","cate":"entertainment","createTime":"1473237940243","duration":"3分钟","format":"3","id":"1505","m3u8":"http://static.mystical.com.cn/hls/mts_1473224229209.m3u8","modifyTime":"1473755906510","picture":"http://pic.firstvr.net.cn/images/orign/2016-9/1473671415002032226.jpg-small","playTimes":"28万+","profile":"激情DJ","score":"8","title":"激情DJ","year":"2016"},{"actor":"未知","area":"4","brand":"20","cate":"variety","createTime":"1483953677503","duration":"10分钟","format":"3","id":"2236","m3u8":"http://static.mystical.com.cn/hls/mts_1483947785277.m3u8","modifyTime":"1483953729053","picture":"http://pic.firstvr.net.cn/images/orign/2017-1/1483953672351157135.jpg-small","playTimes":"17万+","profile":"无聊的日本女孩","score":"8","title":"无聊的日本女孩","year":"2016"},{"actor":"未知","area":"4","brand":"20","cate":"variety","createTime":"1483953703813","duration":"10分钟","format":"3","id":"2237","m3u8":"http://static.mystical.com.cn/hls/mts_1483947540591.m3u8","modifyTime":"1483953719567","picture":"http://pic.firstvr.net.cn/images/orign/2017-1/1483953699000712213.jpg-small","playTimes":"16万+","profile":"逗逼日本女孩","score":"8","title":"逗逼日本女孩","year":"2016"},{"actor":"未知","area":"3","brand":"5","cate":"youtube","createTime":"1481174701571","duration":"1分钟","format":"3","id":"2086","m3u8":"http://static.mystical.com.cn/hls/mts_1481173240255.m3u8","modifyTime":"1481174701571","picture":"http://pic.firstvr.net.cn/images/orign/2016-12/1481174693724620817.jpg-small","playTimes":"15万+","profile":"欧美","score":"8","title":"蒙面聚会","year":"2016"},{"actor":"未知","area":"1","brand":"5","cate":"youtube","createTime":"1481174576216","duration":"1分钟","format":"4","id":"2085","m3u8":"http://static.mystical.com.cn/hls/mts_1481173451576.m3u8","modifyTime":"1481174576216","picture":"http://pic.firstvr.net.cn/images/orign/2016-12/1481174570727405605.jpg-small","playTimes":"18万+","profile":"粉红女郎","score":"8","title":"粉红女郎","year":"2016"},{"actor":"未知","area":"3","brand":"5","cate":"youtube","createTime":"1480577151436","duration":"1分钟","format":"3","id":"2027","m3u8":"http://static.mystical.com.cn/hls/mts_1480571665779.m3u8","modifyTime":"1480577151436","picture":"http://pic.firstvr.net.cn/images/orign/2016-12/1480577140001240005.jpg-small","playTimes":"22万+","profile":"迷彩服美女","score":"8","title":"迷彩服美女","year":"2016"},{"actor":"未知","area":"3","brand":"5","cate":"youtube","createTime":"1480406877932","duration":"1分钟","format":"3","id":"1988","m3u8":"http://static.mystical.com.cn/hls/mts_1480405752126.m3u8","modifyTime":"1480407175811","picture":"http://pic.firstvr.net.cn/images/orign/2016-11/1480406863474553031.jpg-small","playTimes":"22万+","profile":"啦啦队长","score":"8","title":"啦啦队长","year":"2016"},{"actor":"未知","area":"1","brand":"18","cate":"entertainment","createTime":"1478666394357","duration":"3分钟","format":"3","id":"1882","m3u8":"http://static.mystical.com.cn/hls/mts_1478659043704.m3u8","modifyTime":"1478685218490","picture":"http://pic.firstvr.net.cn/images/orign/2016-11/1478685215058238934.jpg-small","playTimes":"16万+","profile":"希拉里克林顿集会","score":"8","title":"希拉里克林顿集会","year":"2016"},{"actor":"未知","area":"1","brand":"4","cate":"variety","createTime":"1473132653597","duration":"2分钟","format":"3","id":"1283","m3u8":"http://static.mystical.com.cn/tsdir1470278858133/1470278957759_gen.m3u8","modifyTime":"1478682858298","picture":"http://pic.firstvr.net.cn/images/orign/2016-11/1478682855914418590.jpg-small","playTimes":"18万+","profile":"唐纳德·特朗普曾经是美国最具知名度的房地产商之一，人称\u201c地产之王\u201d。依靠房地产和股市，特朗普拥有纽约、新泽西州、佛罗里达州等地黄金地段的房地产，并且创建\u201c特朗普梭运航空\u201d，也是新泽西州\u201c将军\u201d职业足球队老板。他在风景怡人的城镇兴建数幢豪华大厦与别墅，还购买价值一亿美元的豪华游艇、此外还拥有私人飞机。","score":"8","title":"唐纳德-特朗普竞选","year":"2016"}]
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
        return "FindInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * actor : 未知
         * area : 1
         * brand : 2
         * cate : tour
         * createTime : 1471857839794
         * duration : 3分钟
         * format : 3
         * id : 711
         * m3u8 : http://static.mystical.com.cn/hls/mts_1471876176828.m3u8
         * modifyTime : 1475224797309
         * picture : http://pic.firstvr.net.cn/images/orign/2016-9/1474515478960353286.jpg-small
         * playTimes : 61万+
         * profile : 第一VR、旅人说影视，联合出品。带你走进美丽的十度，享受自然。
         * score : 8
         * title : 北京十度
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
