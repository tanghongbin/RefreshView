package com.example.com.meimeng.bean;


import java.io.Serializable;

/**
 * Created by 010 on 2015/7/29.
 */
public class FilterBean implements Serializable{

    private int ageStart;

    private int ageEnd;

    private int heightStart;

    private int heightEnd;

    private int residence = -1;

    private  Integer marstate;

    private String marstateStr;

    public String getMarstateStr() {
        return marstateStr;
    }

    public void setMarstateStr(String marstateStr) {
        this.marstateStr = marstateStr;
    }

    public String getEducationStr() {
        return educationStr;
    }

    public void setEducationStr(String educationStr) {
        this.educationStr = educationStr;
    }

    public String getIndustryStr() {
        return industryStr;
    }

    public void setIndustryStr(String industryStr) {
        this.industryStr = industryStr;
    }

    public String getHouseStateStr() {
        return houseStateStr;
    }

    public void setHouseStateStr(String houseStateStr) {
        this.houseStateStr = houseStateStr;
    }

    public String getCarStateStr() {
        return carStateStr;
    }

    public void setCarStateStr(String carStateStr) {
        this.carStateStr = carStateStr;
    }

    public String getYearIncomStr() {
        return yearIncomStr;
    }

    public void setYearIncomStr(String yearIncomStr) {
        this.yearIncomStr = yearIncomStr;
    }

    private Integer education;

    private String educationStr;

    private Integer industry;

    private String industryStr;

    private Integer houseState;

    private String houseStateStr;

    private Integer carState;

    private String carStateStr;

    private Integer yearIncome;

    private String yearIncomStr;

    private String place ;

    private String nativeStr;

    private Integer nativeStrId;

    public String getNativeStr() {
        return nativeStr;
    }

    public void setNativeStr(String nativeStr) {
        this.nativeStr = nativeStr;
    }

    public Integer getNativeStrId() {
        return nativeStrId;
    }

    public void setNativeStrId(Integer nativeStrId) {
        this.nativeStrId = nativeStrId;
    }

    public int getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(int ageStart) {
        this.ageStart = ageStart;
    }

    public int getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(int ageEnd) {
        this.ageEnd = ageEnd;
    }

    public int getHeightStart() {
        return heightStart;
    }

    public void setHeightStart(int heightStart) {
        this.heightStart = heightStart;
    }

    public int getHeightEnd() {
        return heightEnd;
    }

    public void setHeightEnd(int heightEnd) {
        this.heightEnd = heightEnd;
    }

    public int getResidence() {
        return residence;
    }

    public void setResidence(int residence) {
        this.residence = residence;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getIndustry() {
        return industry;
    }

    public void setIndustry(Integer industry) {
        this.industry = industry;
    }

    public Integer getHouseState() {
        return houseState;
    }

    public void setHouseState(Integer houseState) {
        this.houseState = houseState;
    }

    public Integer getCarState() {
        return carState;
    }

    public void setCarState(Integer carState) {
        this.carState = carState;
    }

    public Integer getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(Integer yearIncome) {
        this.yearIncome = yearIncome;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getMarstate() {
        return marstate;
    }

    public void setMarstate(Integer marstate) {
        this.marstate = marstate;
    }
}
