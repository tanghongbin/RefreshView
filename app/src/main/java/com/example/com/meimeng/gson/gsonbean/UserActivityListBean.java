package com.example.com.meimeng.gson.gsonbean;

/**
 * Gson解析用
 * 获取我关注的活动列表的Json数据的Bean
 */
public class UserActivityListBean {
    private UserActivityListParam param;
    private String error;
    private String returnValue;
    private boolean success;

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

    public UserActivityListParam getParam() {
        return param;
    }

    public void setParam(UserActivityListParam param) {
        this.param = param;
    }
}
