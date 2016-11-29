package com.example.com.meimeng.gson.gsonbean;

import java.util.LinkedHashMap;

/**
 * Created by 010 on 2015/7/24.
 */
public class UserStoryDetailItem {

    private long uid;

    private String nickname;

    private long headPic;

    private long picId;
    private String city;
    private String lastLoginTime;
    private LinkedHashMap<String, String> mapStory;

    private int voteNum;

    private boolean hasVote;
    private boolean hasFollow;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(boolean hasFollow) {
        this.hasFollow = hasFollow;
    }

    public boolean isHasVote() {
        return hasVote;
    }

    public void setHasVote(boolean hasVote) {
        this.hasVote = hasVote;
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

    public long getHeadPic() {
        return headPic;
    }

    public void setHeadPic(long headPic) {
        this.headPic = headPic;
    }

    public long getPicId() {
        return picId;
    }

    public void setPicId(long picId) {
        this.picId = picId;
    }

    public LinkedHashMap<String, String> getMapStory() {
        return mapStory;
    }

    public void setMapStory(LinkedHashMap<String, String> mapStory) {
        this.mapStory = mapStory;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
