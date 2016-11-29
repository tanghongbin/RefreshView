package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/8/19.
 */
public class MatchMakerGetBean {
    private MatchMakerGetParam param;
    private String error;
    private String returnValue;
    private boolean success;



    public String getError() {
        return error;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public boolean isSuccess() {
        return success;
    }

    public MatchMakerGetParam getParam() {
        return param;
    }

    public void setParam(MatchMakerGetParam param) {
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
