package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by 003 on 2015/8/5.
 */
public class LstUserAnswer {
    private int qid;
    private List<Integer> aswid;

    public int getQid() {
        return qid;
    }

    public void setAswid(List<Integer> aswid) {
        this.aswid = aswid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public List<Integer> getAswid() {
        return aswid;
    }
}
