package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/7/23.
 */
public class EventDetailBean {

    private MyDetail param;
    private String error;
    private Boolean success;
    private String returnValue;

    private java.util.List<EventData> mlist;

    public String getError() {
        return error;
    }

    public Boolean getSuccess() {
        return success;
    }

    public EventDetail getDetail() {
        return param.getDetail();
    }


}
