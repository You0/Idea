package com.duanqu.Idea.bean;

import android.view.View;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUserItem1 implements Type{
    private String title;
    private int count;
    private int type;
    private int ico;

    public int getIco() {
        return ico;
    }

    public void setIco(int ico) {
        this.ico = ico;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
