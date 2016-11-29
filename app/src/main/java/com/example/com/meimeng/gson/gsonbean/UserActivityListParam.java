package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Gson解析用
 * 获取我关注的活动列表部分的Param
 */
public class UserActivityListParam {
    private List<UserActivityListItem> activityList;

    public List<UserActivityListItem> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<UserActivityListItem> activityList) {
        this.activityList = activityList;
    }
}
