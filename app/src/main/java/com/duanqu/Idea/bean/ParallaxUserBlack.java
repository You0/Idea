package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUserBlack implements Type{
    private int type;
    private int res;


    public int getRes() {
        return res;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRes(int res) {
        this.res = res;
    }
    @Override
    public int getType() {
        return type;
    }


}
