package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.http.cookie.SM;

/**
 * Created by Administrator on 2016/7/20.
 */
public class MyScaleSimpleDraweeView extends SimpleDraweeView {

    private ScaleGestureDetector mScaleGesture;
    private GestureDetector mGestureDetector;
    private boolean OnceLayout = true;
    final private float SCALE_MAX = 4.0F;
    final private float SCALE_MID = 2.0F;
    private boolean IfAutoScale = false;
    private float mCurrentScale = 1.0f;

    private Matrix matrix;
    private float[] matrixValue = new float[9];

    //布局相关宽高
    private float parentHeight;
    private float parentWidth;
    private float height;
    private float width;


    public MyScaleSimpleDraweeView(Context context) {
        super(context);
        Init();

    }

    public MyScaleSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MyScaleSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, 0);
    }

    public MyScaleSimpleDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init();
    }




    private void Init() {
        matrix = new Matrix();
        super.setScaleType(ScaleType.MATRIX);

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(OnceLayout){
                    parentHeight =getHeight();
                    parentWidth = getWidth();
                    Drawable drawable = getDrawable();
                    width = drawable.getIntrinsicWidth();
                    height = drawable.getIntrinsicHeight();
                    System.out.println("pa"+parentHeight+"  "+parentWidth+"dra"+width+height);
                    OnceLayout = false;
                }
            }
        });



        mScaleGesture = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                float currentScale = getCurrentImageScale();

                //拿到图片将要的缩放比例
                float scaleFactor = scaleGestureDetector.getScaleFactor();

                if ((scaleFactor > 1.0f && currentScale < SCALE_MAX) || (scaleFactor < 1.0f && currentScale > mCurrentScale)) {
                    if (scaleFactor * currentScale > SCALE_MAX) {
                        scaleFactor = SCALE_MAX / currentScale;
                    }

                    if (scaleFactor * currentScale < mCurrentScale) {
                        scaleFactor = mCurrentScale / currentScale;
                    }

                    matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                    setImageMatrix(matrix);


                }


                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

            }
        });


        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });


    }



    private void CenterImage()
    {
//        Drawable drawable = getDrawable();
//        System.out.println("drawbal"+drawable);
//        System.out.println(drawable.getIntrinsicHeight()+drawable.getIntrinsicWidth());
//
//        float parentHeight = getHeight();
//        float parentWidth = getWidth();
//        System.out.println("height"+parentHeight+"width"+parentWidth);


    }






    //双击图片自动放大缩小时的task任务
    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;
        //缩放的中心点
        float x, y;

        public AutoScaleRunnable(float TargetScale, float x, float y) {
            this.x = x;
            this.y = y;
            mTargetScale = TargetScale;


            if (getCurrentImageScale() < TargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }
        }

        @Override
        public void run() {
            matrix.postScale(tmpScale, tmpScale, x, y);
            setImageMatrix(matrix);
            final float currentScale = getCurrentImageScale();
            if (currentScale > 1.0f && currentScale < tmpScale || currentScale < 1.0f && currentScale > tmpScale) {
                postDelayed(this, 16);
            } else {
                final float deltaScale = mTargetScale / currentScale;
                matrix.postScale(deltaScale, deltaScale, x, y);

                setImageMatrix(matrix);
                IfAutoScale = false;
            }


        }
    }


    private float getCurrentImageScale() {
        matrix.getValues(matrixValue);
        return matrixValue[Matrix.MSCALE_X];
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGesture.onTouchEvent(event);
        if (!mScaleGesture.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }
}
