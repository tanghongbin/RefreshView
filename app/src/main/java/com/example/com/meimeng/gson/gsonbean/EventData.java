package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/7/12.
 */
public class EventData {

    private long activityId;//活动id
    private String title;//活动标题
    private int state;//活动状态
    private int type;//活动类型
    private String place;//活动地点
    private String placeDetail;//活动地点具体地点
    private Long startTime;//开始时间
    private long pic;//活动图片id
    private int applyAllCount;//活动报名人数，针对官方活动，私人果冻
    private int applyManCount;//六人约会报名男用户数
    private int applyWomanCount;//六人约会报名女用户数
    private int manLimit;//六人约会男用户数上限
    private int womanLimit;//六人约会女用户数上限
    private int placeDetailShowFlg;
 
    private int allLimit;//六人约会人数限制


    public long getActivityId() {
        return activityId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public int getAllLimit() {
        return allLimit;
    }

    public int getApplyAllCount() {
        return applyAllCount;
    }

    public int getApplyManCount() {
        return applyManCount;
    }

    public int getApplyWomanCount() {
        return applyWomanCount;
    }

    public int getManLimit() {
        return manLimit;
    }

    public String getPlace() {
        return place;
    }

    public int getState() {
        return state;
    }

    public int getType() {
        return type;
    }

    public int getWomanLimit() {
        return womanLimit;
    }

    public long getPic() {
        return pic;
    }

    public String getTitle() {
        return title;
    }

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }

    public int getPlaceDetailShowFlg() {
        return placeDetailShowFlg;
    }

    public void setPlaceDetailShowFlg(int placeDetailShowFlg) {
        this.placeDetailShowFlg = placeDetailShowFlg;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "activityId=" + activityId +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", type=" + type +
                ", place='" + place + '\'' +
                ", placeDetail='" + placeDetail + '\'' +
                ", startTime=" + startTime +
                ", pic=" + pic +
                ", applyAllCount=" + applyAllCount +
                ", applyManCount=" + applyManCount +
                ", applyWomanCount=" + applyWomanCount +
                ", manLimit=" + manLimit +
                ", womanLimit=" + womanLimit +
                ", allLimit=" + allLimit +
                '}';
    }
}
