package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by wjw on 2015/7/25.
 * 获取用户收到的礼物list每一项礼物
 */
public class ReceivedGiftItem {
    private long  giftId;
    private long  giftCount;
    private String name;
    private String comment;
    private String firstName;
    private int sex;
    private long uid;
    private long headPic;
    private long giftPic;
    private String giftName;
    private String nickname ;
    private String gmtCreate;

    public long getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(long giftCount) {
        this.giftCount = giftCount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public long getGiftId() {

        return giftId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getGiftPic() {
        return giftPic;
    }

    public long getHeadPic() {
        return headPic;
    }

    public long getUid() {
        return uid;
    }

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setGiftId(long giftId) {
        this.giftId = giftId;
    }

    public void setGiftPic(long giftPic) {
        this.giftPic = giftPic;
    }

    public void setHeadPic(long headPic) {
        this.headPic = headPic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}