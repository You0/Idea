package com.duanqu.Idea.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/6/28.
 */
public abstract class MyGestureDetector extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private GestureDetector mDetector;
    private int mSlop; //晃荡
    private boolean Ignore = false;//是否监听
    private float DownY;

    public abstract void onScrollDown();

    public abstract void onScrollUp();

    public void setIgnore(boolean ignore) {
        Ignore = ignore;
    }

    public MyGestureDetector(Context context) {
        mDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        DownY = e.getY();
        return false;//不拦截
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Ignore) {
            return false;
        }
        if (distanceY == 0) {
            DownY = e2.getY();
        }

        float distance = DownY - e2.getY();

        if (distance < -30) {
            onScrollDown();
        } else if(distance > 30){
            onScrollUp();
        }
        return false;

    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mDetector.onTouchEvent(motionEvent);
        return false;
    }
}
