package com.yaya.myvr.bean;

/**
 * Created by admin on 2017/5/16.
 */

public class LoginInfo {

    /**
     * errCode : 0
     * errMsg : success
     * data : {"loginKey":"a30eeb4536daf2cf169ea91325cc9119"}
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
        return "LoginInfo{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * loginKey : a30eeb4536daf2cf169ea91325cc9119
         */

        private String loginKey;

        public String getLoginKey() {
            return loginKey;
        }

        public void setLoginKey(String loginKey) {
            this.loginKey = loginKey;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "loginKey='" + loginKey + '\'' +
                    '}';
        }
    }
}
