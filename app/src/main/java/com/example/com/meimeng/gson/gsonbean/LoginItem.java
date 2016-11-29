package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by 010 on 2015/7/22.
 */
public class LoginItem {

    private long uid;
    private String nickname;

    public Integer getResidence() {
        return residence;
    }

    public void setResidence(Integer residence) {
        this.residence = residence;
    }

    private Integer residence;
    private long headPic;
    private int level;
    private String token;
    private Integer sex;
    private int userState;

    private int userVerfiy;

    private int userActive;

    private int infoState;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public int getUserVerfiy() {
        return userVerfiy;
    }

    public void setUserVerfiy(int userVerfiy) {
        this.userVerfiy = userVerfiy;
    }

    public int getUserActive() {
        return userActive;
    }

    public void setUserActive(int userActive) {
        this.userActive = userActive;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public int getInfoState() {
        return infoState;
    }

    public void setInfoState(int infoState) {
        this.infoState = infoState;
    }
}
