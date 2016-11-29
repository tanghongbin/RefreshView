package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Gson解析用
 * HomeJsonBean中Param部分
 */
public class HomeListParam {

    private List<HomeListItem> recommendation;

    public List<HomeListItem> getHomeRecommendation() {
        return recommendation;
    }

    public void setHomeRecommendation(List<HomeListItem> recommendation) {
        this.recommendation = recommendation;
    }
}
