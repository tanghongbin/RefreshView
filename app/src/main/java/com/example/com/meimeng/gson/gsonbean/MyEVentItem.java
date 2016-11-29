package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 */
public class MyEVentItem {

   private List<MyEventList> myActivityList;

    public List<MyEventList> getMyActivityList() {
        return myActivityList;
    }

    public void setMyActivityList(List<MyEventList> myActivityList) {
        this.myActivityList = myActivityList;
    }

    @Override
    public String toString() {
        return "MyEVentItem{" +
                "myActivityList=" + myActivityList +
                '}';
    }
}
