package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/10/15.
 */
public class AttentionBean {
    private String name;
    private String url;
    private String username;
    private boolean checked;

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }



    public String getName() {
        return name;
    }
}
