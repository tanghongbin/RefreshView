package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CheckOsVersionParamVersion {

    private Long id;

    private String androidVerNum;

    private List<String> lstAndroidContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAndroidVerNum() {
        return androidVerNum;
    }

    public void setAndroidVerNum(String androidVerNum) {
        this.androidVerNum = androidVerNum;
    }

    public List<String> getLstAndroidContent() {
        return lstAndroidContent;
    }

    public void setLstAndroidContent(List<String> lstAndroidContent) {
        this.lstAndroidContent = lstAndroidContent;
    }
}
