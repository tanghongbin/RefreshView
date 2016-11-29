package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by lx on 2015/8/4.
 */
public class ChatAuthBean extends BaseBean {
    private ChatAuthCanchat param;
    private String returnValue;

    public ChatAuthCanchat getParam() {
        return param;
    }

    public void setParam(ChatAuthCanchat param) {
        this.param = param;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }
}
