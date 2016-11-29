package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/8/19.
 */
public class MatchMakerBean {
    private MatchMakerParam param;
    private String error;
    private String returnValue;
    private boolean success;

    public MatchMakerParam getParam() {
        return param;
    }

    public String getError() {
        return error;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setParam(MatchMakerParam param) {
        this.param = param;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
