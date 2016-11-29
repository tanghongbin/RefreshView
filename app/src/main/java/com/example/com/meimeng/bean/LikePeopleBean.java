package com.example.com.meimeng.bean;

/**
 * Created by 010 on 2015/7/17.
 */
public class LikePeopleBean {


    private int ty;
//    private int type;
//    private String name;

    private Long uid;
    private long headPic;
    private String city;
    private String firstName;
    private int sex;
    private int height;
    private int age;
    private int type;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }

    @Override
    public String toString() {
        return "LikePeopleBean{" +
                "uid=" + uid +
                ", headPic=" + headPic +
                ", city='" + city + '\'' +
                ", firstName='" + firstName + '\'' +
                ", sex=" + sex +
                ", height=" + height +
                ", age=" + age +
                ", type=" + type +
                '}';
    }
}
