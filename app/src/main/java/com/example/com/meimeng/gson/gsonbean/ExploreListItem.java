package com.example.com.meimeng.gson.gsonbean;

/**
 * 发现页list的item
 */
public class ExploreListItem {

    private long uid;//用户唯一id
//    private String nickname;//昵称
    private String firstName;
    private int sex;
    private int photoVote;
//    private long score;//魅力指数，目前来自于被关注数与故事获赞数的总和
    private long headPic;//用户头像id
    private String city;//居住地
    private String lastLoginTime;//在线时间
    private int age;//年龄
    private int vipLevel;//年龄
    private int followerNum;//年龄

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    private int height;//身高
    private int ideStatus;//身份认证
    private int eduStatus;//教育认证
    private int jobStatus;//工作认证
    private int marStatus;//婚姻认证
    private int proStatus;//财产认证

    public int getAge() {
        return age;
    }

//    public String getNickname() {ir
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }


    public String getFristName() {
        return firstName;
    }

    public void setFristName(String firstName) {
        this.firstName = firstName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getPhotoVote() {
        return photoVote;
    }

    public void setPhotoVote(int photoVote) {
        this.photoVote = photoVote;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

//    public long getScore() {
//        return score;
//    }
//
//    public void setScore(long score) {
//        this.score = score;
//    }

    public long getHeadPic() {
        return headPic;
    }

    public void setHeadPic(long headPic) {
        this.headPic = headPic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getIdeStatus() {
        return ideStatus;
    }

    public void setIdeStatus(int ideStatus) {
        this.ideStatus = ideStatus;
    }

    public int getEduStatus() {
        return eduStatus;
    }

    public void setEduStatus(int eduStatus) {
        this.eduStatus = eduStatus;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getMarStatus() {
        return marStatus;
    }

    public void setMarStatus(int marStatus) {
        this.marStatus = marStatus;
    }

    public int getProStatus() {
        return proStatus;
    }

    public void setProStatus(int proStatus) {
        this.proStatus = proStatus;
    }

}
