package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by lx on 2015/8/6.
 */
public class UserAnswerData {

    private Long qid ;

    private String question ;

    private List<Integer> aswid ;

    private List<String> answer ;

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Integer> getAswid() {
        return aswid;
    }

    public void setAswid(List<Integer> aswid) {
        this.aswid = aswid;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }


}
