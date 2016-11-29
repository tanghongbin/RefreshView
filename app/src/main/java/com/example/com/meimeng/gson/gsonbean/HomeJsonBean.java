package com.example.com.meimeng.gson.gsonbean;

/**
 * Gson解析用
 * 首页列表解析Json数据的Bean
 */
public class HomeJsonBean {
    private HomeListParam param;
    private String error;
    private String returnValue;
    private boolean success;

    public HomeListParam getParam() {
        return param;
    }

    public void setParam(HomeListParam param) {
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
