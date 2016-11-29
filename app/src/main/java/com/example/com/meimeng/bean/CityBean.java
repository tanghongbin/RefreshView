package com.example.com.meimeng.bean;

/**
 * Created by Administrator on 2015/12/2.
 */
public class CityBean {

    private String cityName;
    private int cityId = -1;

    public CityBean (String cityName,int cityId){
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public CityBean(){}



    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "cityName='" + cityName + '\'' +
                ", cityId=" + cityId +
                '}';
    }
}
