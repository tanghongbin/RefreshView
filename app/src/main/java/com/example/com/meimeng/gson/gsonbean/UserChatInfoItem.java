package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2016/1/5.
 */
public class UserChatInfoItem {
    private long uid;

    private String realname;

    private String firstName;

    private long headPic;
    private int photoLock;

    private int picVerfiy;

    private int sex;

    private Long importId;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getUid() {

        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getRealname() {

        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getPicVerfiy() {

        return picVerfiy;
    }

    public void setPicVerfiy(int picVerfiy) {
        this.picVerfiy = picVerfiy;
    }

    public int getPhotoLock() {

        return photoLock;
    }

    public void setPhotoLock(int photoLock) {
        this.photoLock = photoLock;
    }

    public Long getImportId() {

        return importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    public long getHeadPic() {

        return headPic;
    }

    public void setHeadPic(long headPic) {
        this.headPic = headPic;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
