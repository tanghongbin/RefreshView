package com.xqd.chatmessage.gson;

/**
 * Created by 010 on 2015/7/17.
 */
public class UserPeopleBean {
    private long uid;
    private String tel;
    private String name;
    private long headId;
    private int sex;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHeadId() {
        return headId;
    }

    public void setHeadId(long headId) {
        this.headId = headId;
    }
}
