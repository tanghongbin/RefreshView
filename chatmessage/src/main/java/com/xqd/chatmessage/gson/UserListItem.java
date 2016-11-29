package com.xqd.chatmessage.gson;

/**
 * Created by 010 on 2015/7/23.
 */
public class UserListItem {
    private long uid;
    private String tel;
    private String nickname;
    private long headPic;
    private int sex;
    public long getUid() {

        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
