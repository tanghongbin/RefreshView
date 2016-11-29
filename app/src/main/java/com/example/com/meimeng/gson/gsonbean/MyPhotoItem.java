package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by songhuaiyu on 2015/12/02.
 */
public class MyPhotoItem extends BaseBean {
    private long id;//相片id
    private long uid;//用户id
    private int sort;//相片排序
    private long picId;//相片id
    private int delFlg;//是否删除
    private int verifyState;//是否通过审核
    private int verifyType;//照片类型1：头像 0 默认相册

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getPicId() {
        return picId;
    }

    public void setPicId(long picId) {
        this.picId = picId;
    }

    public int getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(int delFlg) {
        this.delFlg = delFlg;
    }

    public int getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(int verifyState) {
        this.verifyState = verifyState;
    }

    public int getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(int verifyType) {
        this.verifyType = verifyType;
    }
}
