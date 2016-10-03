package com.duanqu.Idea.CustomView;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.duanqu.Idea.app.MyApplication;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ParallaxListview  extends ListView{
    private static final String TAG = "ParallaX";
    private SimpleDraweeView draweeView;
    private int RawHeight;
    private int RawWidth;
    private LinearLayout user_info;
    private RectF rect = new RectF();
    private Matrix matrix = new Matrix();
    private float mLastY;

    private GenericDraweeHierarchy hierarchy;


    public ParallaxListview(Context context) {
        this(context,null);
    }

    public ParallaxListview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ParallaxListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDraweeView(SimpleDraweeView draweeView) {
        this.draweeView = draweeView;
    }

    public void setUser_info(LinearLayout user_info) {
        this.user_info = user_info;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        RawHeight = draweeView.getHeight();
        RawWidth = draweeView.getWidth();
        hierarchy = draweeView.getHierarchy();
        hierarchy.getActualImageBounds(rect);
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        if(getFirstVisiblePosition()==0&&getChildAt(0).getTop()==0){
            setSelectionFromTop(0,-10);
        }

        //scrollBy(0,10);
        super.onWindowFocusChanged(hasWindowFocus);
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);

        //invalidate();
    }

    public void SetSelectionFromTop()
    {
        setSelectionFromTop(0,-10);
    }

    public void setTouchDisable()
    {
        if(getFirstVisiblePosition()==1){

        }
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return true;
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        View view = getChildAt(2);
//        view.getLayoutParams().height =100;
//        view.requestLayout();
//        float Y = ev.getY();
//        float deltY = Y-mLastY;
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_MOVE:{
//                Log.d(TAG,"getScrollY"+getScrollY());
//
//               // Log.d(TAG,"getChildAt(getFirstVisiblePosition()).getTop()"+getChildAt(getFirstVisiblePosition()).getTop());
//                Log.d(TAG,"deltY"+deltY);
//                if (getChildAt(getFirstVisiblePosition()).getTop()==0){
//                    if(deltY!=0f){
//                        scrollTo(0, (int) -deltY);
//                    }
//                }
//                break;
//            }
//
//            case MotionEvent.ACTION_UP:{
//                // 执行回弹动画, 方式一: 属性动画\值动画
//                // 从当前高度mImage.getHeight(), 执行动画到原始高度mOriginalHeight
//
//                break;
//            }
//        }
//        mLastY = Y;
//
//        return super.onTouchEvent(ev);
//    }

    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }


    public void ResetAnimator()
    {
        final int startHeight = draweeView.getLayoutParams().height;
        final int endHeight = RawHeight;
        ValueAnimator valueAnimator = new ValueAnimator().ofInt(1);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction =animation.getAnimatedFraction();
                int newheight = evaluate(fraction,startHeight,endHeight);
                draweeView.getLayoutParams().height = newheight;
                draweeView.requestLayout();
                RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int top = draweeView.getBottom()-MyApplication.dip2px(50);
                params.setMargins(0,top,0,0);
                user_info.setLayoutParams(params);
            }
        });
        valueAnimator.start();
    }



    public void setParallaxHead(Boolean isTouchEvent,int deltaY){
        if(isTouchEvent&&deltaY<0)
        {
            draweeView.getLayoutParams().height -= deltaY/3;
            draweeView.requestLayout();
            int top = draweeView.getBottom()-MyApplication.dip2px(50);
            Log.e(TAG,top+"top");
//            user_info.layout(0,top,user_info.getWidth(),top+user_info.getHeight());
//            user_info.invalidate();
            RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,top,0,0);
            user_info.setLayoutParams(params);

        }


    }


        @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
            setParallaxHead(isTouchEvent,deltaY);
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
