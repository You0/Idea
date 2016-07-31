package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.activity.ImageDisplay;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @author DragonJiang
 * @Date 2016/5/10
 * @Time 11:12
 * @description
 */
public class MyScaleSimpleDraweeView extends SimpleDraweeView {
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private final float MAX_SIZE = 4.0F;
    private final float MID_SIZE = 2.0F;
    private final float MIN_SIZE = 1.0f;
    private boolean AutoScaleing = false;
    private float mCurrentScale = 1f;
    private Matrix mCurrentMatrix;
    private float mx;
    private float my;
    private OnClickListener mClickListener;
    private boolean isCheckTopAndBottom = true;
    private boolean isCheckLeftAndRight = true;
    private int mTouchSlop;
    private int lastPointerCount;
    private float mLastX;
    private float mLastY;
    private int position;
    private GenericDraweeHierarchy hierarchy = getHierarchy();

    public void setPosition(int position) {
        this.position = position;
    }

    public MyScaleSimpleDraweeView(Context context) {
        super(context);
        init();
    }

    public MyScaleSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScaleSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        mCurrentMatrix = new Matrix();
        ScaleGestureDetector.OnScaleGestureListener scaleListener = new ScaleGestureDetector
                .SimpleOnScaleGestureListener() {

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                if (mCurrentScale > MAX_SIZE) {
                    if(scaleFactor<1.0f){
                        mCurrentScale *= scaleFactor;
                        mCurrentMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                    }

                }else{
                    mCurrentScale *= scaleFactor;
                    mCurrentMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                }
                if (mCurrentScale > 1.0f) {
                    checkBorder();
                }
                invalidate();
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                super.onScaleEnd(detector);
                if (mCurrentScale > 0.7f && mCurrentScale < 1.0f) {
                    reset();
                }
                if (mCurrentScale < 0.7f) {
                    ImageDisplay.imageD.Finish();
                }
                checkBorder();
            }
        };
        mScaleDetector = new ScaleGestureDetector(getContext(), scaleListener);

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//
//                if (AutoScaleing) {
//                    return true;
//                } else {
//                    AutoScaleing = true;
//                    if (mCurrentScale < MID_SIZE - 0.2) {
//                        postDelayed(new AutoScale(MID_SIZE, e.getX(), e.getY()), 16);
//                    } else {
//                        postDelayed(new AutoScale(MIN_SIZE, e.getX(), e.getY()), 16);
//                        checkBorder();
//                    }
//                    return true;
//                }
//
//
//            }
            public boolean onDoubleTap(MotionEvent e)
            {
                if (AutoScaleing == true)
                    return true;

                float x = e.getX();
                float y = e.getY();
                //Log.e("DoubleTap", getScale() + " , " + initScale);
                if (mCurrentScale < MID_SIZE)
                {
                    postDelayed(
                            new AutoScaleRunnable(MID_SIZE, x, y), 16);
                    AutoScaleing = true;
                } else if (mCurrentScale >= MID_SIZE
                        && mCurrentScale < MAX_SIZE)
                {
                    postDelayed(
                            new AutoScaleRunnable(MAX_SIZE, x, y), 16);
                    AutoScaleing = true;
                } else
                {
                    postDelayed(
                            new AutoScaleRunnable(MIN_SIZE, x, y), 16);
                    AutoScaleing = true;
                }

                return true;
            }
        };
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
    }


    /**
     * 检查图片边界是否移到view以内
     * 目的是让图片边缘不要移动到view里面
     */
    private void checkBorder() {
        RectF rectF = getDisplayRect(mCurrentMatrix);
        RectF rectF1 = new RectF();
        System.out.println(rectF.top + (Config.HEIGHT - rectF1.top));
        hierarchy.getActualImageBounds(rectF1);
        boolean reset = false;
        float dx = 0;
        float dy = 0;
        if (rectF.left > 0) {
            dx = -rectF.left;
            reset = true;
        }
        if ((rectF1.bottom - rectF1.top) * mCurrentScale > Config.HEIGHT) {

            if (rectF.top + rectF1.top * mCurrentScale > 0) {
                dy = -(rectF.top + rectF1.top * mCurrentScale);
                reset = true;
            }

            if (rectF.bottom - (getHeight()-rectF1.bottom)*mCurrentScale < getHeight()) {
                dy = getHeight() - (rectF.bottom - (getHeight()-rectF1.bottom)*mCurrentScale);
                reset = true;
            }


        }else{
            dy = getHeight()*0.5f - (rectF.bottom - (getHeight()-rectF1.bottom)*mCurrentScale)+
                    0.5f*(rectF1.bottom - rectF1.top)*mCurrentScale;
            reset = true;
        }

        if (rectF.right < Config.WIDTH) {
            dx = Config.WIDTH - rectF.right;
            reset = true;
        }

        if (reset) {
            mCurrentMatrix.postTranslate(dx, dy);
            invalidate();
        }
    }

    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @param matrix - Matrix to map Drawable against
     * @return RectF - Displayed Rectangle
     */
    public RectF getDisplayRect(Matrix matrix) {
        RectF rectF = new RectF(0, 0, getWidth(), getBottom());
        matrix.mapRect(rectF);
        return rectF;


    }

    @Override
    public void setImageURI(Uri uri) {
        reset();
        super.setImageURI(uri);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        reset();
        super.setImageBitmap(bm);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.concat(mCurrentMatrix);
        if (Config.HEIGHT == 0) {
            Config.HEIGHT = canvas.getHeight();
            Config.WIDTH = canvas.getWidth();
        }
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        mScaleDetector.onTouchEvent(event);

        float x = 0, y = 0;
        // 拿到触摸点的个数
        final int pointerCount = event.getPointerCount();
        // 得到多个触摸点的x与y均值
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        /**
         * 每当触摸点发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            mLastX = x;
            mLastY = y;
        }

        lastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                if (mCurrentScale > 1.0f) {
                    float dx = x - mLastX;
                    float dy = y - mLastY;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (getDisplayRect(mCurrentMatrix).left == 0 && dx > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                reset();
                            }
                        }, 1500);



                    }

                    if (getDisplayRect(mCurrentMatrix).right == getWidth() && dx < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                reset();
                            }
                        }, 1500);
                    }

                    RectF rectF = getDisplayRect(mCurrentMatrix);
                    mCurrentMatrix.postTranslate(dx, dy);
                    checkBorder();
                    invalidate();
                    mLastX = x;
                    mLastY = y;
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                break;

        }
        return true;
    }


    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;

        /**
         * 缩放的中心
         */
        private float x;
        private float y;

        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         *
         * @param targetScale
         */
        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (mCurrentScale < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }

        }

        @Override
        public void run() {
            // 进行缩放
            mCurrentScale *= tmpScale;
            mCurrentMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorder();
            invalidate();

            final float currentScale = mCurrentScale;
            // 如果值在合法范围内，继续缩放
            if (((tmpScale > 1f) && (currentScale < mTargetScale))
                    || ((tmpScale < 1f) && (mTargetScale < currentScale))) {
                postDelayed(this, 16);
            } else
            // 设置为目标的缩放比例
            {

                final float deltaScale = mTargetScale / currentScale;
                mCurrentMatrix.postScale(deltaScale, deltaScale, x, y);
                if(mTargetScale==MIN_SIZE){
                    reset();
                    mCurrentScale=1.0f;
                }
                checkBorder();
                invalidate();
                AutoScaleing = false;
            }

        }
    }





//    private class AutoScale implements Runnable {
//        private float target;
//        private float tmp;
//        private float x, y;
//        private final float BIGGER = 1.07f;
//        private final float SMALLER = 0.93f;
//        private boolean Big = true;
//
//        public AutoScale(float target, float x, float y) {
//            this.target = target;
//            this.x = x;
//            this.y = y;
//            if (mCurrentScale > target) {
//                Big = false;
//            }
//
//        }
//
//        @Override
//        public void run() {
//            if (target == MID_SIZE) {
//                mCurrentScale = BIGGER * mCurrentScale;
//                mCurrentMatrix.postScale(BIGGER, BIGGER, x, y);
//                checkBorder();
//                invalidate();
//
//            } else if (target == MIN_SIZE) {
//                mCurrentScale = SMALLER * mCurrentScale;
//                mCurrentMatrix.postScale(SMALLER, SMALLER, x, y);
//                checkBorder();
//                invalidate();
//
//            }
//
//            if (mCurrentScale < MID_SIZE && mCurrentScale > MIN_SIZE) {
//                postDelayed(this, 16);
//                //System.out.println("mCurrent"+mCurrentScale);
//            } else {
//                AutoScaleing = false;
//                if (mCurrentScale < MIN_SIZE) {
//                    tmp = MIN_SIZE / mCurrentScale;
//                    mCurrentMatrix.postScale(tmp, tmp, x, y);
//                    checkBorder();
//                    invalidate();
//                }
//            }
//        }
//    }


    /**
     * Resets the zoom of the attached image.
     * This has no effect if the image has been destroyed
     */
    public void reset() {
        mCurrentMatrix.reset();
        mCurrentScale = 1f;
        invalidate();
    }

    public interface OnClickListener {
        void onClick();
    }


    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
    }


}