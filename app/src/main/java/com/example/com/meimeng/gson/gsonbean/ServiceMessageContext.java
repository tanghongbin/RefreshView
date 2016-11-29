package com.example.com.meimeng.gson.gsonbean;

import java.util.ArrayList;

/**
 * Created by 010 on 2015/8/13.
 */
public class ServiceMessageContext {

    private int sender;

    private ArrayList<Integer> reciver;

    private int type;

    private String content;

    private long activityId;

    private String activityTitle;

    private String placeDetail;

    private String uid;

    private long giftId;

    private String gift;

    private String authenticationName;

    public String getAuthenticationName() {
        return authenticationName;
    }

    public void setAuthenticationName(String authenticationName) {
        this.authenticationName = authenticationName;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public long getGiftId() {

        return giftId;
    }

    public void setGiftId(long giftId) {
        this.giftId = giftId;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public ArrayList<Integer> getReciver() {
        return reciver;
    }

    public void setReciver(ArrayList<Integer> reciver) {
        this.reciver = reciver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
