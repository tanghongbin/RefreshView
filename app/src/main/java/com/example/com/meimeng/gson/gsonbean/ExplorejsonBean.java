package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/7/22.
 */
public class ExplorejsonBean {
    private ExploreParam param;//严格按照接口里面的字段来写
    private String error;
    private String returnValue;
    private boolean success;

    public ExploreParam getParam() {
        return param;
    }

    public void setParam(ExploreParam param) {
        this.param = param;
    }


    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getReturnValue() {
        return returnValue;
    }


    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
