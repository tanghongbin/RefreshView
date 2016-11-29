package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by songhuaiyu on 2015/11/28.
 */
public class MyBaseInfoItem {//shy
    private long uid;
    private int vipLevel;
    private  String firstName;
    private String lastName;
    private long headPic;
    private String city;
    private String birthday;
    private int followerNum;
    private int infoSize;
    private  int photoLock;
    private int age;
    private int sex;
    private int userVerfiy;
    private int height;
    private int yearIncome;
    private int ideStatus;

    public int getIdeStatus() {
        return ideStatus;
    }

    public void setIdeStatus(int ideStatus) {
        this.ideStatus = ideStatus;
    }

    private List<MyPhotoItem> myPhotos;
    private List<Long> identityPicId;


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<Long> getIdentityPicId() {
        return identityPicId;
    }

    public void setIdentityPicId(List<Long> identityPicId) {
        this.identityPicId = identityPicId;
    }


    public int getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(int yearIncome) {
        this.yearIncome = yearIncome;
    }

    public int getUserVerfiy() {
        return userVerfiy;
    }

    public void setUserVerfiy(int userVerfiy) {
        this.userVerfiy = userVerfiy;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
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

    public long getHeadPic() {
        return headPic;
    }

    public void setHeadPic(long headPic) {
        this.headPic = headPic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    public int getInfoSize() {
        return infoSize;
    }

    public void setInfoSize(int infoSize) {
        this.infoSize = infoSize;
    }

    public int getPhotoLock() {
        return photoLock;
    }

    public void setPhotoLock(int photoLock) {
        this.photoLock = photoLock;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public List<MyPhotoItem> getMyPhotos() {
        return myPhotos;
    }

    public void setMyPhotos(List<MyPhotoItem> myPhotos) {
        this.myPhotos = myPhotos;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
