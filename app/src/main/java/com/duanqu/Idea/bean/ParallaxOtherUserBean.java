package com.duanqu.Idea.bean;

public class ParallaxOtherUserBean implements Type{
    private int type;
    private int id;
    private String username;
    private String e_mail;
    private String nickname;
    private String sign;
    private String sex;
    private String grades;
    private String major;
    private String school;
    private String imageurl;
    private String headurl;

    private int coutFayan;
    private int countShouChang;
    private int countLove;
    private int contactsCount;
    private int revcontactsCount;

    public void setContactsCount(int contactsCount) {
        this.contactsCount = contactsCount;
    }

    public void setRevcontactsCount(int revcontactsCount) {
        this.revcontactsCount = revcontactsCount;
    }

    public int getContactsCount() {
        return contactsCount;
    }

    public int getRevcontactsCount() {
        return revcontactsCount;
    }

    private boolean already;

    public void setType(int type) {
        this.type = type;
    }

    public void setAlready(boolean already) {
        this.already = already;
    }

    public boolean getAlready() {
        return already;
    }

    public int getCountLove() {
        return countLove;
    }

    public int getCountShouChang() {
        return countShouChang;
    }

    public int getCoutFayan() {
        return coutFayan;
    }

    public void setCountLove(int countLove) {
        this.countLove = countLove;
    }

    public void setCountShouChang(int countShouChang) {
        this.countShouChang = countShouChang;
    }

    public void setCoutFayan(int coutFayan) {
        this.coutFayan = coutFayan;
    }

    public String getHeadurl() {
        return headurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "ParallaxOtherUserBean{" +
                "type=" + type +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sign='" + sign + '\'' +
                ", sex='" + sex + '\'' +
                ", grades='" + grades + '\'' +
                ", major='" + major + '\'' +
                ", school='" + school + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", headurl='" + headurl + '\'' +
                ", coutFayan=" + coutFayan +
                ", countShouChang=" + countShouChang +
                ", countLove=" + countLove +
                ", contactsCount=" + contactsCount +
                ", revcontactsCount=" + revcontactsCount +
                ", already=" + already +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public int getType() {
        return type;
    }
}
