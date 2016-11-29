package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/8/19.
 */
public class MatchMakerParam {
    private String matchmaker;
    private int restChance;
    public int getRestChance() {
        return restChance;
    }

    public void setRestChance(int restChance) {
        this.restChance = restChance;
    }

    public void setMatchmaker(String matchmaker) {
        this.matchmaker = matchmaker;
    }

    public String getMatchmaker() {
        return matchmaker;
    }
}
