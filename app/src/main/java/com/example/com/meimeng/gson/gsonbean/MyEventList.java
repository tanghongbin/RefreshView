package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/12/3.
 */
public class MyEventList {

    private Long pic;
    private Long activityId;
    private int type;
    private String title;
    private int state;
    private String place;
    private String placeDetail;
    private double price;
    private Long startTime;
    private int payState;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getPic() {
        return pic;
    }

    public void setPic(Long pic) {
        this.pic = pic;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    @Override
    public String toString() {
        return "MyEventList{" +
                "pic=" + pic +
                ", activityId=" + activityId +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", place='" + place + '\'' +
                ", placeDetail='" + placeDetail + '\'' +
                ", startTime=" + startTime +
                ", payState=" + payState +
                '}';
    }
}
