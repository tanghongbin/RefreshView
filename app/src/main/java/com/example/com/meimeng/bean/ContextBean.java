package com.example.com.meimeng.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/7.
 */
public class ContextBean {


    private long sender;
    private List<Long> reciver;
    private int type;
    private String content;
    private long uid;
    private int subType;


    private long activityId;
    private String activityTitle;
    private String placeDetail;


    private int contentId;
    private long giftId;
    private String gift;


    private int verfiyFlg;
    private String reason;


    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public List<Long> getReciver() {
        return reciver;
    }

    public void setReciver(List<Long> reciver) {
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

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
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

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public long getGiftId() {
        return giftId;
    }

    public void setGiftId(long giftId) {
        this.giftId = giftId;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public int getVerfiyFlg() {
        return verfiyFlg;
    }

    public void setVerfiyFlg(int verfiyFlg) {
        this.verfiyFlg = verfiyFlg;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ContextBean{" +
                "sender=" + sender +
                ", reciver=" + reciver +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", uid=" + uid +
                ", subType=" + subType +
                ", activityId=" + activityId +
                ", activityTitle='" + activityTitle + '\'' +
                ", placeDetail='" + placeDetail + '\'' +
                ", contentId=" + contentId +
                ", giftId=" + giftId +
                ", gift='" + gift + '\'' +
                ", verfiyFlg=" + verfiyFlg +
                ", reason='" + reason + '\'' +
                '}';
    }
}
