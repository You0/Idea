package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/12/7.
 */
public class MessageListBean {
    private String userhead;
    private String username;
    private String nickname;
    private String Count;
    private String lastmessage;
    private int tag;

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    @Override
    public String toString() {
        return "MessageListBean{" +
                "userhead='" + userhead + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", Count='" + Count + '\'' +
                ", lastmessage='" + lastmessage + '\'' +
                ", tag=" + tag +
                '}';
    }
}
