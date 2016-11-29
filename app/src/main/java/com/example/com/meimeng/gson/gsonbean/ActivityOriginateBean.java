package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/9/11.
 */
public class ActivityOriginateBean {
    private ActivityOriginateParam param;
    private String error;
    private String returnValue;
    private boolean success;

    public ActivityOriginateParam getParam() {
        return param;
    }

    public void setParam(ActivityOriginateParam param) {
        this.param = param;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
