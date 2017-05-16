package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/16.
 */

public class SmsInfo {

    /**
     * errCode : 0
     * errMsg :
     * data : {"status":"success","result":"0"}
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
        return "SmsInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * status : success
         * result : 0
         */

        private String status;
        private String result;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "status='" + status + '\'' +
                    ", result='" + result + '\'' +
                    '}';
        }
    }
}
