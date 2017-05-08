package com.yaya.myvr.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/7.
 */

public class DictInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : {"area":[{"id":"1","name":"中国大陆"},{"id":"2","name":"港台"},{"id":"3","name":"欧美"},{"id":"4","name":"日韩"},{"id":"5","name":"东南亚"},{"id":"100","name":"其他"}],"quality":[{"id":"1","name":"标清"},{"id":"2","name":"HD"},{"id":"3","name":"720P"},{"id":"4","name":"1080P"},{"id":"5","name":"2K"},{"id":"6","name":"4K"}],"format":[{"id":"1","name":"普通"},{"id":"2","name":"3D"},{"id":"3","name":"VR"}],"category":[{"cate":"hd","name":"超清","tags":"精选"},{"cate":"tour","name":"旅游","tags":"精选|国内|欧美|东南亚|日韩|奥非|其他|海岛|沙漠"},{"cate":"girl","name":"美女","tags":"精选|热舞|日韩|泳装"},{"cate":"entertainment","name":"娱乐","tags":"精选|演唱会|舞蹈"},{"cate":"youtube","name":"Youtube","tags":"精选"},{"cate":"movie","name":"3D电影","tags":"精选|动作|恐怖|科幻|爱情|预告片"},{"cate":"variety","name":"综艺","tags":"精选"},{"cate":"sport","name":"体育","tags":"精选"},{"cate":"live","name":"直播","tags":"直播|回放|预告"}]}
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
        return "DictInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        private List<AreaBean> area;
        private List<QualityBean> quality;
        private List<FormatBean> format;
        private List<CategoryBean> category;

        public List<AreaBean> getArea() {
            return area;
        }

        public void setArea(List<AreaBean> area) {
            this.area = area;
        }

        public List<QualityBean> getQuality() {
            return quality;
        }

        public void setQuality(List<QualityBean> quality) {
            this.quality = quality;
        }

        public List<FormatBean> getFormat() {
            return format;
        }

        public void setFormat(List<FormatBean> format) {
            this.format = format;
        }

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "area=" + area +
                    ", quality=" + quality +
                    ", format=" + format +
                    ", category=" + category +
                    '}';
        }

        public static class AreaBean {
            /**
             * id : 1
             * name : 中国大陆
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "AreaBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }
        }

        public static class QualityBean {
            /**
             * id : 1
             * name : 标清
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "QualityBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }
        }

        public static class FormatBean {
            /**
             * id : 1
             * name : 普通
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "FormatBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }
        }

        public static class CategoryBean {
            /**
             * cate : hd
             * name : 超清
             * tags : 精选
             */

            private String cate;
            private String name;
            private String tags;

            public String getCate() {
                return cate;
            }

            public void setCate(String cate) {
                this.cate = cate;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            @Override
            public String toString() {
                return "CategoryBean{" +
                        "cate='" + cate + '\'' +
                        ", name='" + name + '\'' +
                        ", tags='" + tags + '\'' +
                        '}';
            }
        }
    }
}
