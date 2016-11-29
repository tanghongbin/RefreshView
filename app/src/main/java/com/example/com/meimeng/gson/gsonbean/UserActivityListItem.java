package com.example.com.meimeng.gson.gsonbean;

/**
 * Gson解析用
 * 获取我关注的活动列表的activityList的每一项
 */
public class UserActivityListItem {
    private long activityId;
    private String title;
    private int state;
    private int type;
    private String place;
    private long startTime;
    private long pic;
    /**
     * 活动报名人数，官方活动、私人活动需要显示
     */
    private int applyAllCount;
    /**
     * 报名男用户数 6人约会需要显示
     */
    private int applyManCount;
    /**
     * 报名女用户数 6人约会需要显示
     */
    private int applyWomanCount;
    /**
     * 男用户上限 6人约会需要显示
     */
    private int manLimit;
    /**
     * 女用户上限 6人约会需要显示
     */
    private int womanLimit;
    private int allLimit;

    public int getApplyAllCount() {
        return applyAllCount;
    }

    public int getApplyManCount() {
        return applyManCount;
    }

    public int getManLimit() {
        return manLimit;
    }

    public void setManLimit(int manLimit) {
        this.manLimit = manLimit;
    }

    public int getAllLimit() {
        return allLimit;
    }

    public int getApplyWomanCount() {
        return applyWomanCount;
    }

    public int getWomanLimit() {
        return womanLimit;
    }

    public void setAllLimit(int allLimit) {
        this.allLimit = allLimit;
    }

    public void setApplyWomanCount(int applyWomanCount) {
        this.applyWomanCount = applyWomanCount;
    }

    public void setWomanLimit(int womanLimit) {
        this.womanLimit = womanLimit;
    }

    public int getState() {
        return state;
    }

    public int getType() {
        return type;
    }


    public long getActivityId() {
        return activityId;
    }

    public long getPic() {
        return pic;
    }



    public String getTitle() {
        return title;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public void setApplyAllCount(int applyAllCount) {
        this.applyAllCount = applyAllCount;
    }

    public void setApplyManCount(int applyManCount) {
        this.applyManCount = applyManCount;
    }



    public void setPic(long pic) {
        this.pic = pic;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }


}
