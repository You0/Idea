package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUserBlack implements Type{
    private int type;
    private int height = 0;

    public void setType(int type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getType() {
        return type;
    }


}
