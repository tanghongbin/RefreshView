package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/7/23.
 */
public class EventDetail {
    private long activityId;//活动id
    private String title;//活动标题
    private int state;//活动状态
    private int type;//活动类型
    private String place;//活动地点
    private long startTime;//开始时间
    private long pic;//活动图片id
    private int applyAllCount;//活动报名人数，针对官方活动，私人果冻
    private int peopleSize;//活动报名人数，针对官方活动，私人果冻
    private int applyManCount;//六人约会报名男用户数
    private int applyWomanCount;//六人约会报名女用户数
    private int manLimit;//六人约会男用户数上限
    private int womanLimit;//六人约会女用户数上限
    private int allLimit;//六人约会人数限制
    private long oUserId;//发起人id
    private long oHeadPic;//发起人头像
    private String oNickname;//发起人昵称
    private int oLevel;//发起人等级
    private String describe;//活动描述
    private String describes;//活动描述_s
    private String requirement;//活动要求
    private String placeDetail;//活动详情
    private long endTime;//活动结束时间
    private double price;//活动价格
    private boolean hasApply;//判断是否参加
    private int placeDetailShowFlg;//是否显示详细地址 0 显示 1不显示

    public int getPeopleSize() {
        return peopleSize;
    }

    public void setPeopleSize(int peopleSize) {
        this.peopleSize = peopleSize;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public long getPic() {
        return pic;
    }

    public void setPic(long pic) {
        this.pic = pic;
    }

    public int getApplyAllCount() {
        return applyAllCount;
    }

    public void setApplyAllCount(int applyAllCount) {
        this.applyAllCount = applyAllCount;
    }

    public int getApplyManCount() {
        return applyManCount;
    }

    public void setApplyManCount(int applyManCount) {
        this.applyManCount = applyManCount;
    }

    public int getApplyWomanCount() {
        return applyWomanCount;
    }

    public void setApplyWomanCount(int applyWomanCount) {
        this.applyWomanCount = applyWomanCount;
    }

    public int getManLimit() {
        return manLimit;
    }

    public void setManLimit(int manLimit) {
        this.manLimit = manLimit;
    }

    public int getWomanLimit() {
        return womanLimit;
    }

    public void setWomanLimit(int womanLimit) {
        this.womanLimit = womanLimit;
    }

    public int getAllLimit() {
        return allLimit;
    }

    public void setAllLimit(int allLimit) {
        this.allLimit = allLimit;
    }

    public long getoUserId() {
        return oUserId;
    }

    public void setoUserId(long oUserId) {
        this.oUserId = oUserId;
    }

    public long getoHeadPic() {
        return oHeadPic;
    }

    public void setoHeadPic(long oHeadPic) {
        this.oHeadPic = oHeadPic;
    }

    public String getoNickname() {
        return oNickname;
    }

    public void setoNickname(String oNickname) {
        this.oNickname = oNickname;
    }

    public int getoLevel() {
        return oLevel;
    }

    public void setoLevel(int oLevel) {
        this.oLevel = oLevel;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isHasApply() {
        return hasApply;
    }

    public void setHasApply(boolean hasApply) {
        this.hasApply = hasApply;
    }

    public int getPlaceDetailShowFlg() {
        return placeDetailShowFlg;
    }

    public void setPlaceDetailShowFlg(int placeDetailShowFlg) {
        this.placeDetailShowFlg = placeDetailShowFlg;
    }
}

