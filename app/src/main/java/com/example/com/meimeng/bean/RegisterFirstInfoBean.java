package com.example.com.meimeng.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/9.
 */
public class RegisterFirstInfoBean {

    private String headerPath;

    private Long headPic;

    private String firstName;

    private String lastName;

    private String birthday;
    private int maritalStatus;
    private int yearIncome;

    private int ideStatus;


    public int getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getIdeStatus() {
        return ideStatus;
    }

    public void setIdeStatus(int ideStatus) {
        this.ideStatus = ideStatus;
    }

    public int getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(int yearIncome) {
        this.yearIncome = yearIncome;
    }

    private Integer height;

    private ArrayList<Long> identifyPicIdList;

    private String identifyPath;

    public String getHeaderPath() {
        return headerPath;
    }

    public void setHeaderPath(String headerPath) {
        this.headerPath = headerPath;
    }

    public String getIdentifyPath() {
        return identifyPath;
    }

    public void setIdentifyPath(String identifyPath) {
        this.identifyPath = identifyPath;
    }

    public Long getHeadPic() {
        return headPic;
    }

    public void setHeadPic(Long headPic) {
        this.headPic = headPic;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public ArrayList<Long> getIdentifyPicIdList() {
        return identifyPicIdList;
    }

    public void setIdentifyPicIdList(ArrayList<Long> identifyPicIdList) {
        this.identifyPicIdList = identifyPicIdList;
    }
}
