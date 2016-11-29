package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by 003 on 2015/8/10.
 */
public class CertificateStatus {
    private long uid;
    private List<Long> identityPicId;
    private Integer ideStatus;
    private List<Long> eduPicId;
    private Integer eduStatus;
    private List<Long> propertyPicId;
    private Integer propertyStatus;
    private List<Long> jobPicId;
    private Integer jobStatus;
    private int marPicId;
    private Integer marStatus;


    public Integer getMarStatus() {
        return marStatus;
    }

    public void setMarStatus(Integer marStatus) {
        this.marStatus = marStatus;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<Long> getIdentityPicId() {
        return identityPicId;
    }

    public void setIdentityPicId(List<Long> identityPicId) {
        this.identityPicId = identityPicId;
    }

    public Integer getIdeStatus() {
        return ideStatus;
    }

    public void setIdeStatus(Integer ideStatus) {
        this.ideStatus = ideStatus;
    }

    public Integer getEduStatus() {
        return eduStatus;
    }

    public void setEduStatus(Integer eduStatus) {
        this.eduStatus = eduStatus;
    }

    public List<Long> getEduPicId() {
        return eduPicId;
    }

    public void setEduPicId(List<Long> eduPicId) {
        this.eduPicId = eduPicId;
    }

    public List<Long> getPropertyPicId() {
        return propertyPicId;
    }

    public void setPropertyPicId(List<Long> propertyPicId) {
        this.propertyPicId = propertyPicId;
    }

    public Integer getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(Integer propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public List<Long> getJobPicId() {
        return jobPicId;
    }

    public void setJobPicId(List<Long> jobPicId) {
        this.jobPicId = jobPicId;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getMarPicId() {
        return marPicId;
    }

    public void setMarPicId(int marPicId) {
        this.marPicId = marPicId;
    }
}
