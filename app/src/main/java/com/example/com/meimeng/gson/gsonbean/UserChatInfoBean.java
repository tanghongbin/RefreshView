package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by 010 on 2015/7/22.
 */
public class UserChatInfoBean {

    private UserChatInfoParam param;
    private String error;
    private boolean returnValue;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isReturnValue() {

        return returnValue;
    }

    public void setReturnValue(boolean returnValue) {
        this.returnValue = returnValue;
    }

    public String getError() {

        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UserChatInfoParam getParam() {
        return param;
    }

    public void setParam(UserChatInfoParam param) {
        this.param = param;
    }
}
