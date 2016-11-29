package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by songhuaiyu on 2015/11/28.
 */
public class MyDetailInfoItem {//shy
    private long uid;
    private String firstName;//	姓	String	不可编辑
    private String lastName;//	名	String	不可编辑
    private int sex;//	性别	Integer	不可编辑
    private String birthday;//	生日	Long	不可编辑
    private int zodiacKey;//	生肖	String	不可编辑
    private String zodiacValue;
    private String constellationValue;//	星座	String	不可编辑
    private int constellationKey;
    private int marStatus;
    private int height;//	身高	Integer
    private int weight;//	体重	Integer
    private int residenceKey;//	居住地	Integer
    private String residenceValue;//	居住地	String
    private int nationKey;//	民族	Integer
    private String nationValue;//	民族	String
    private int educationKey;//	学历	Integer
    private String educationValue;//	学历	String
    private String graduateCollege;//	毕业学校	String
    private int companyIndustryKey;//	行业	Integer
    private String companyIndustryValue;//	行业	String
    private int companyPositionKey;//	职位	Integer
    private String companyPositionValue;//	职位	String
    private int outsideExperienceKey;//	海外经历	Integer
    private String outsideExperienceValue;//	海外经历	String
    private int yearIncomeKey;//	年薪	Integer
    private String yearIncomeValue;//	年薪	String
    int maritalStatusKey;
    String maritalStatusValue;
    private int propertyKey;//	资产	Integer
    private String propertyValue;//	资产	String
    private int familyBackgroundKey;//	家庭背景	Integer
    private String familyBackgroundValue;//	家庭背景	String
    private int houseStateKey;//	住房情况	Integer
    private String houseStateValue;//	住房情况	String
    private int carStateKey;//	购车情况	Integer
    private String carStateValue;//	购车情况	String
    private List<String> lstLoveExercise;//	我喜欢的运动	List	String类型的List
    private List<String> lstLoveMusic;//	我喜欢的音乐	List	String类型的List
    private List<String> lstLoveFood;//	我喜欢的食物	List	String类型的List
    private List<String> lstLoveFilm;//	我喜欢的电影	List	String类型的List
    private List<String> lstLoveBook;//	我喜欢的书	List	String类型的List
    private List<String> lstLoveTouristDestination;//	我的旅行足迹	List	String类型的List
    private String userStory;//	我的故事	String

    public String getMaritalStatusValue() {
        return maritalStatusValue;
    }

    public void setMaritalStatusValue(String maritalStatusValue) {
        this.maritalStatusValue = maritalStatusValue;
    }

    public int getMaritalStatusKey() {
        return maritalStatusKey;
    }

    public void setMaritalStatusKey(int maritalStatusKey) {
        this.maritalStatusKey = maritalStatusKey;
    }

    public int getMarStatus() {
        return marStatus;
    }

    public void setMarStatus(int marStatus) {
        this.marStatus = marStatus;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getZodiacKey() {
        return zodiacKey;
    }

    public void setZodiacKey(int zodiacKey) {
        this.zodiacKey = zodiacKey;
    }

    public String getZodiacValue() {
        return zodiacValue;
    }

    public void setZodiacValue(String zodiacValue) {
        this.zodiacValue = zodiacValue;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRediseKey() {
        return residenceKey;
    }

    public void setRediseKey(int rediseKey) {
        this.residenceKey = rediseKey;
    }

    public String getRediseValue() {
        return residenceValue;
    }

    public void setRediseValue(String rediseValue) {
        this.residenceValue = rediseValue;
    }

    public int getNationKey() {
        return nationKey;
    }

    public void setNationKey(int nationKey) {
        this.nationKey = nationKey;
    }

    public String getNationValue() {
        return nationValue;
    }

    public void setNationValue(String nationValue) {
        this.nationValue = nationValue;
    }

    public int getEducationKey() {
        return educationKey;
    }

    public void setEducationKey(int educationKey) {
        this.educationKey = educationKey;
    }

    public String getEducationValue() {
        return educationValue;
    }

    public void setEducationValue(String educationValue) {
        this.educationValue = educationValue;
    }

    public String getGraduateCollege() {
        return graduateCollege;
    }

    public void setGraduateCollege(String graduateCollege) {
        this.graduateCollege = graduateCollege;
    }

    public int getCompanyIndustryKey() {
        return companyIndustryKey;
    }

    public void setCompanyIndustryKey(int companyIndustryKey) {
        this.companyIndustryKey = companyIndustryKey;
    }

    public String getCompanyIndustryValue() {
        return companyIndustryValue;
    }

    public void setCompanyIndustryValue(String companyIndustryValue) {
        this.companyIndustryValue = companyIndustryValue;
    }

    public int getCompanyPositionKey() {
        return companyPositionKey;
    }

    public void setCompanyPositionKey(int companyPositionKey) {
        this.companyPositionKey = companyPositionKey;
    }

    public String getCompanyPositionValue() {
        return companyPositionValue;
    }

    public void setCompanyPositionValue(String companyPositionValue) {
        this.companyPositionValue = companyPositionValue;
    }

    public int getOutsideExperienceKey() {
        return outsideExperienceKey;
    }

    public void setOutsideExperienceKey(int outsideExperienceKey) {
        this.outsideExperienceKey = outsideExperienceKey;
    }

    public String getOutsideExperienceValue() {
        return outsideExperienceValue;
    }

    public void setOutsideExperienceValue(String outsideExperienceValue) {
        this.outsideExperienceValue = outsideExperienceValue;
    }

    public int getYearIncomeKey() {
        return yearIncomeKey;
    }

    public void setYearIncomeKey(int yearIncomeKey) {
        this.yearIncomeKey = yearIncomeKey;
    }

    public String getYearIncomeValue() {
        return yearIncomeValue;
    }

    public void setYearIncomeValue(String yearIncomeValue) {
        this.yearIncomeValue = yearIncomeValue;
    }

    public int getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(int propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public int getFamilyBackgroundKey() {
        return familyBackgroundKey;
    }

    public void setFamilyBackgroundKey(int familyBackgroundKey) {
        this.familyBackgroundKey = familyBackgroundKey;
    }

    public String getFamilyBackgroundValue() {
        return familyBackgroundValue;
    }

    public void setFamilyBackgroundValue(String familyBackgroundValue) {
        this.familyBackgroundValue = familyBackgroundValue;
    }

    public int getHouseStateKey() {
        return houseStateKey;
    }

    public void setHouseStateKey(int houseStateKey) {
        this.houseStateKey = houseStateKey;
    }

    public String getHouseStateValue() {
        return houseStateValue;
    }

    public void setHouseStateValue(String houseStateValue) {
        this.houseStateValue = houseStateValue;
    }

    public int getCarStateKey() {
        return carStateKey;
    }

    public void setCarStateKey(int carStateKey) {
        this.carStateKey = carStateKey;
    }

    public String getCarStateValue() {
        return carStateValue;
    }

    public void setCarStateValue(String carStateValue) {
        this.carStateValue = carStateValue;
    }

    public String getUserStory() {
        return userStory;
    }

    public void setUserStory(String userStory) {
        this.userStory = userStory;
    }

    public String getConstellationValue() {
        return constellationValue;
    }

    public void setConstellationValue(String constellationValue) {
        this.constellationValue = constellationValue;
    }

    public int getConstellationKey() {
        return constellationKey;
    }

    public void setConstellationKey(int constellationKey) {
        this.constellationKey = constellationKey;
    }

    public List<String> getLstLoveExercise() {
        return lstLoveExercise;
    }

    public void setLstLoveExercise(List<String> lstLoveExercise) {
        this.lstLoveExercise = lstLoveExercise;
    }

    public List<String> getLstLoveMusic() {
        return lstLoveMusic;
    }

    public void setLstLoveMusic(List<String> lstLoveMusic) {
        this.lstLoveMusic = lstLoveMusic;
    }

    public List<String> getLstLoveFood() {
        return lstLoveFood;
    }

    public void setLstLoveFood(List<String> lstLoveFood) {
        this.lstLoveFood = lstLoveFood;
    }

    public List<String> getLstLoveFilm() {
        return lstLoveFilm;
    }

    public void setLstLoveFilm(List<String> lstLoveFilm) {
        this.lstLoveFilm = lstLoveFilm;
    }

    public List<String> getLstLoveBook() {
        return lstLoveBook;
    }

    public void setLstLoveBook(List<String> lstLoveBook) {
        this.lstLoveBook = lstLoveBook;
    }

    public List<String> getLstLoveTouristDestination() {
        return lstLoveTouristDestination;
    }

    public void setLstLoveTouristDestination(List<String> lstLoveTouristDestination) {
        this.lstLoveTouristDestination = lstLoveTouristDestination;
    }

}
