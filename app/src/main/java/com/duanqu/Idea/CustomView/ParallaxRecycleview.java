package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ParallaxRecycleview extends RecyclerView{
    private String TAG = "ParallaxRecycleview";
    private SimpleDraweeView draweeView;
    private int RawHeight;
    private int RawWidth;
    private RectF rect = new RectF();
    private Matrix matrix = new Matrix();
    private GenericDraweeHierarchy hierarchy;

    public ParallaxRecycleview(Context context) {
        super(context);
    }

    public ParallaxRecycleview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxRecycleview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDraweeView(SimpleDraweeView draweeView) {
        this.draweeView = draweeView;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        RawHeight = draweeView.getHeight();
        RawWidth = draweeView.getWidth();
        hierarchy = draweeView.getHierarchy();
        hierarchy.getActualImageBounds(rect);
        super.onWindowFocusChanged(hasWindowFocus);
    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // deltaY : 竖直方向的瞬时偏移量 / 变化量 dx   顶部到头下拉为-, 底部到头上拉为+
        // scrollY : 竖直方向的偏移量 / 变化量
        // scrollRangeY : 竖直方向滑动的范围
        // maxOverScrollY : 竖直方向最大滑动范围
        // isTouchEvent : 是否是手指触摸滑动, true为手指, false为惯性
        if(isTouchEvent&&deltaY<0)
        {
            draweeView.getLayoutParams().height -= deltaY/3;
            draweeView.requestLayout();
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

}
