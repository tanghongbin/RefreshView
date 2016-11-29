package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by wjwon 2015/7/25.
 * 分页获取商城礼物list
 */
public class GiftMallListBean {
    private GiftMallListParam param;
    private String error;
    private boolean success;
    private String returnValue;

    public String getError() {
        return error;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public GiftMallListParam getParam() {
        return param;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setParam(GiftMallListParam param) {
        this.param = param;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }
}

