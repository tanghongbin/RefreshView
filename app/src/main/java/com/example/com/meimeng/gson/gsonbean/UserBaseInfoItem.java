package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by 010 on 2015/7/22.
 */
public class UserBaseInfoItem {
    private long uid;

    private String nickname;

    private String firstName;//姓

    private String lastName;//名

    private int vipLevel;//会员等级

    private long headPic;

    private int followNum;

    private int followerNum;

    private int followeeNum;

    private int userDateState;
    private int postActivityNum;

    private int ideStatus;

    private int propertyStatus;

    private int eduStatus;

    private int jobStatus;

    private int marStatus;

    private boolean hasFollow;
    private int unread;
    private int giftNum;

    private Integer sex;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }





    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getUserDateState() {
        return userDateState;
    }

    public void setUserDateState(int userDateState) {
        this.userDateState = userDateState;
    }

    public long getUid() {
        return uid;
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

    public int getLevel() {
        return vipLevel;
    }

    public void setLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public long getHeadPic() {
        return headPic;
    }

    public void setHeadPic(long headPic) {
        this.headPic = headPic;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getFolloweeNum() {
        return followeeNum;
    }

    public void setFolloweeNum(int followeeNum) {
        this.followeeNum = followeeNum;
    }

    public int getPostActivityNum() {
        return postActivityNum;
    }

    public void setPostActivityNum(int postActivityNum) {
        this.postActivityNum = postActivityNum;
    }

    public int getIdeStatus() {
        return ideStatus;
    }

    public void setIdeStatus(int ideStatus) {
        this.ideStatus = ideStatus;
    }

    public int getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(int propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public int getEduStatus() {
        return eduStatus;
    }

    public void setEduStatus(int eduStatus) {
        this.eduStatus = eduStatus;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getMarStatus() {
        return marStatus;
    }

    public void setMarStatus(int marStatus) {
        this.marStatus = marStatus;
    }

    public boolean isHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(boolean hasFollow) {
        this.hasFollow = hasFollow;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
