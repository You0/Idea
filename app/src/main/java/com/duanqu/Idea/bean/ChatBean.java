package com.duanqu.Idea.bean;

/**
 * Created by Droidroid on 2016/4/25.
 */
public class ChatBean {
    public static final int LEFT_TEXT = 0;
    public static final int LEFT_IMAGE = 1;
    public static final int RIGHT_TEXT = 2;
    public static final int RIGHT_IMAGE = 3;
    public static final int TIME = 4;
    public int type;
    public String userhead;
    public String time;
    public String text;
    public int imgId;
    public int tag;
    public String EncodeString ;
    public String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setEncodeString(String encodeString) {
        EncodeString = encodeString;
    }

    public String getEncodeString() {
        return EncodeString;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
