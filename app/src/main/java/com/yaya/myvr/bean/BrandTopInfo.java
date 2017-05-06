package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/6.
 */

public class BrandTopInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : {"attention":"19","brandImg":"http://pic.firstvr.net.cn/images/orign/2016-9/1473054888330437689.png-middle","cateProfile":"体育","id":"3","isAttention":false,"logo":"http://pic.firstvr.net.cn/images/orign/2016-9/1472968101531452305.png-small","name":"Fanc","profile":"Fanc 精选体育运动VR资源","tags":"[体育][竞技]","weight":"7"}
     */

    private int errCode;
    private String errMsg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BrandTopInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * attention : 19
         * brandImg : http://pic.firstvr.net.cn/images/orign/2016-9/1473054888330437689.png-middle
         * cateProfile : 体育
         * id : 3
         * isAttention : false
         * logo : http://pic.firstvr.net.cn/images/orign/2016-9/1472968101531452305.png-small
         * name : Fanc
         * profile : Fanc 精选体育运动VR资源
         * tags : [体育][竞技]
         * weight : 7
         */

        private String attention;
        private String brandImg;
        private String cateProfile;
        private String id;
        private boolean isAttention;
        private String logo;
        private String name;
        private String profile;
        private String tags;
        private String weight;

        public String getAttention() {
            return attention;
        }

        public void setAttention(String attention) {
            this.attention = attention;
        }

        public String getBrandImg() {
            return brandImg;
        }

        public void setBrandImg(String brandImg) {
            this.brandImg = brandImg;
        }

        public String getCateProfile() {
            return cateProfile;
        }

        public void setCateProfile(String cateProfile) {
            this.cateProfile = cateProfile;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsAttention() {
            return isAttention;
        }

        public void setIsAttention(boolean isAttention) {
            this.isAttention = isAttention;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "attention='" + attention + '\'' +
                    ", brandImg='" + brandImg + '\'' +
                    ", cateProfile='" + cateProfile + '\'' +
                    ", id='" + id + '\'' +
                    ", isAttention=" + isAttention +
                    ", logo='" + logo + '\'' +
                    ", name='" + name + '\'' +
                    ", profile='" + profile + '\'' +
                    ", tags='" + tags + '\'' +
                    ", weight='" + weight + '\'' +
                    '}';
        }
    }
}
