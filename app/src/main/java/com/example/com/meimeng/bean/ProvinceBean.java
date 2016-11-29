package com.example.com.meimeng.bean;

import android.content.Context;

import com.example.com.meimeng.util.DataUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/2.
 */
public class ProvinceBean {

    private String provinceName;
    private int provinceId;
    private ArrayList<CityBean> cityBeans = new ArrayList<>();
    private Context context;
    private DataUtil dataUtil;

    public ProvinceBean(String provinceName,int provinceId,Context context){
        this.context = context;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        dataUtil = new DataUtil(context);
        setMunicipalities();


    }

    private void setMunicipalities() {
        String[] mCity = {"北京市","上海市","天津市","重庆市"};
        for(String str : mCity){

            if(provinceName.equals(str)){

                cityBeans.add(new CityBean(str,provinceId));


                break;
            }else {
                cityBeans = dataUtil.getCityList(provinceId);
            }
        }
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public ArrayList<CityBean> getCityBeans() {
        return cityBeans;
    }

    public void setCityBeans(ArrayList<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
    }

    @Override
    public String toString() {
        return "ProvinceBean{" +
                "provinceName='" + provinceName + '\'' +
                ", provinceId=" + provinceId +
                '}';
    }
}
