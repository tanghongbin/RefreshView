package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/9/23.
 */
public class RequestCodeBean {
    private String error;
    private boolean success;
    private boolean returnValue;
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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
}
