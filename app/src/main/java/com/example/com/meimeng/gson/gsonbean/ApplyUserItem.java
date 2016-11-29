package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by 003 on 2015/7/23.
 * 报名我发起的活动的用户数据项
 */
public class ApplyUserItem {
    private long uid;
    private  String nickname;
    private long headPic;
    private int state;
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
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
