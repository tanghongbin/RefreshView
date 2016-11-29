package com.example.com.meimeng.bean;

/**
 * Created by 010 on 2015/8/12.
 */
public class ServiceMessageBean {

    private long senderId;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    private String nickname;

    private String contentStr;

    private String timeStr;

    private long headPicId;

    private String placeDetail;

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }

    public long getHeadPicId() {
        return headPicId;
    }

    public void setHeadPicId(long headPicId) {
        this.headPicId = headPicId;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    public String getNickname() {

        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
