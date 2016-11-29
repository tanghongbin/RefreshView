package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/21.
 */
public class EventListBean {
    private MyList param;
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
 
    public List<EventData> getlist() {
        return param.getList();
    }

    public static class MyList {
        public List<EventData> list;
        public List<EventData> getList() {
            return list;
        }
    }
    }
    


