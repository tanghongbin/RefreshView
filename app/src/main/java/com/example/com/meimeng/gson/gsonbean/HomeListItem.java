package com.example.com.meimeng.gson.gsonbean;

/**
 * Gson解析用
 * HomeJsonBean 中param部分 recommendation的每一项
 */
public class HomeListItem {
    private long uid;
    private long headPic;
    private String nickname;
    private long storyPic;
    private String storyBrief;
    private long voteNum;
    private int type;
    private String city;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getHeadPic() {
        return headPic;
    }

    public void setHeadPic(long headPic) {
        this.headPic = headPic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getStoryPic() {
        return storyPic;
    }

    public void setStoryPic(long storyPic) {
        this.storyPic = storyPic;
    }

    public String getStoryBrief() {
        return storyBrief;
    }

    public void setStoryBrief(String storyBrief) {
        this.storyBrief = storyBrief;
    }

    public long getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(long voteNum) {
        this.voteNum = voteNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "HomeListItem [uid=" + uid + ",headPic=" + headPic + ",nikename=" + nickname + ",storyPic" + storyPic + ",storyBrief" + storyBrief + ",type=" + type + ",voteNum=" + voteNum+",city="+city;
    }
}
