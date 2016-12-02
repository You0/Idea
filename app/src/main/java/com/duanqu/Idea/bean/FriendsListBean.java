package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/9/14.
 */
public class FriendsListBean {
    private String url;
    private String name;
    private String username;
    //private boolean isChecked;

//    public void setChecked(boolean checked) {
//        isChecked = checked;
//    }
//
//
//    public boolean getIsChecked() {
//        return isChecked;
//    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
