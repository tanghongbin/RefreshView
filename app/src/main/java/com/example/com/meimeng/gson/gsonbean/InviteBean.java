package com.example.com.meimeng.gson.gsonbean;

/**
 * 邀请用户的Bean
 */
public class InviteBean {
    private Invitecode param;
    private String error;
    private boolean success;
    private String returnValue;

    public boolean isSuccess() {
        return success;
    }

    public Invitecode getParam() {
        return param;
    }

    public void setParam(Invitecode param) {
        this.param = param;
    }

    public String getError() {
        return error;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

}
