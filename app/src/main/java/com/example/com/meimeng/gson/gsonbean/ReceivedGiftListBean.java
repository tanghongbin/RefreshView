package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by wjw on 2015/7/25.
 * 获取用户收到的礼物list
 */
public class ReceivedGiftListBean {
    private ReceivedGifiListParam param;
    private String error;
    private boolean success;
    private String returnValue;

    public String getError() {
        return error;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public ReceivedGifiListParam getParam() {
        return param;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public void setParam(ReceivedGifiListParam param) {
        this.param = param;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public boolean isSuccess() {
        return success;
    }

}
