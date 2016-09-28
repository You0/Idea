package com.duanqu.Idea.bean;

import android.view.View;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUserItem1 implements Type{
    private int res;
    private String title;
    private int count;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
