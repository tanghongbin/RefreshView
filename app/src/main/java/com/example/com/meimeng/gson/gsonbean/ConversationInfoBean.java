package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by songhuaiyu on 2015/11/28.
 */
public class ConversationInfoBean extends BaseBean {

    private ConversationInfoParam param;
    private Object returnValue;

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public ConversationInfoParam getParam() {
        return param;
    }

    public void setParam(ConversationInfoParam param) {
        this.param = param;
    }
}
