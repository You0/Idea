package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;



/**
 * 侧拉删除
 * Created by Administrator on 2016/10/27.
 */
public class SwipeLayout extends FrameLayout {
    private ViewDragHelper dragHelper;
    private ViewDragHelper.Callback mCallBack;
    private View backView;
    private View frontView;
    private int frontHeight;
    private int frontWidth;
    private int mRange;
    private float mLastX;
    private float mLastY;
    private Status status = Status.Close;
    private OnSwipeLayoutListener swipeLayoutListener;
    private boolean touchEvent = true;
    private int mLastXIntercept;
    private int mLastYIntercept;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }


    public OnSwipeLayoutListener getSwipeLayoutListener() {
        return swipeLayoutListener;
    }

    public void setSwipeLayoutListener(OnSwipeLayoutListener swipeLayoutListener) {
        this.swipeLayoutListener = swipeLayoutListener;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static enum Status {
        Close, Open, Draging
    }

    public static interface OnSwipeLayoutListener {

        void onClose(SwipeLayout mSwipeLayout);

        void onOpen(SwipeLayout mSwipeLayout);

        void onDraging(SwipeLayout mSwipeLayout);

        // 要去关闭
        void onStartClose(SwipeLayout mSwipeLayout);

        // 要去开启
        void onStartOpen(SwipeLayout mSwipeLayout);
    }


    private void Init() {
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //Log.e("Swip", xvel + "release");
                super.onViewReleased(releasedChild, xvel, yvel);
                if (xvel != 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (xvel == 0 && releasedChild.getLeft() < -mRange / 2f) {
                    open();
                } else if (xvel < 0) {
                    open();
                } else {
                    close();
                }
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //Log.e("swipe",left+"");
                if (child == frontView) {
                    if (left > 0) {
                        return 0;
                    } else if (left < -mRange) {
                        return -mRange;
                    }
                } else if (child == backView) {
                    if (left > frontWidth) {
                        left = frontWidth;
                    } else if (left < frontWidth - mRange) {
                        left = frontWidth - mRange;
                    }
                }
                return left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                if (changedView == frontView) {
                    backView.offsetLeftAndRight(dx);
                    //Log.e("changeView", "front");
                    dispatchSwipeEvent();
                }
                if (changedView == backView) {
                    //Log.e("changeView", "back");
                    frontView.offsetLeftAndRight(dx);
                }


                // 兼容老版本
                invalidate();
            }
        });
    }

    private void dispatchSwipeEvent() {
        //记录上一次状态
        Status preStatus = status;
        status = UpdateStatus();
        //Log.e("swipeLayout", "UpdateStatus");
        if (preStatus != status && swipeLayoutListener != null) {
            if (status == Status.Close) {
                swipeLayoutListener.onClose(this);
            } else if (status == Status.Open) {
                swipeLayoutListener.onOpen(this);
            } else if (status == Status.Draging) {
                //Log.e("swipeLayout", "Draging");
                //Log.e("swipeLayout", preStatus + ":prestatus");
                if (preStatus == Status.Open) {
                    swipeLayoutListener.onStartClose(this);
                } else if (preStatus == Status.Close) {
                    swipeLayoutListener.onStartOpen(this);
                }
            }
        }
    }

    private Status UpdateStatus() {
        int left = frontView.getLeft();
        if (left == 0) {
            return Status.Close;
        } else if (left == -mRange) {
            return Status.Open;
        } else {
            return Status.Draging;
        }
    }


    public void close() {
        int finalLeft = 0;
        if (dragHelper.smoothSlideViewTo(frontView, finalLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void open() {
        int finalLeft = -mRange;
        //开始动画
        if (dragHelper.smoothSlideViewTo(frontView, finalLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        laytoutContent(false);
    }

    private void laytoutContent(boolean isOpen) {
        // 摆放前View
        Rect frontRect = computeFrontViewRect(isOpen);
        frontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
        // 摆放后View
        Rect backRect = computeBackViewViaFront(frontRect);
        backView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

        // 调整顺序, 把mFrontView前置
        bringChildToFront(frontView);
    }

    private Rect computeBackViewViaFront(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left, 0, left + mRange, 0 + frontHeight);
    }

    private Rect computeFrontViewRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mRange;
        }
        return new Rect(left, 0, left + frontWidth, 0 + frontHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        frontHeight = frontView.getMeasuredHeight();
        frontWidth = frontView.getMeasuredWidth();
        mRange = backView.getMeasuredWidth();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        backView = getChildAt(0);
        frontView = getChildAt(1);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("SwipeLayout","onTouchEvent");
        float X = event.getX();
        float Y = event.getY();
        float deltX;
        float deltY;
        try {
            dragHelper.processTouchEvent(event);
        }catch(Exception e){
            e.printStackTrace();
        }


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                break;
            }

            case MotionEvent.ACTION_UP:{

                //return false;
                break;
            }

            case MotionEvent.ACTION_MOVE:{
                deltX = X-mLastX;
                deltY = Y-mLastY;
                if(Math.abs(deltX)>Math.abs(deltY)){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            }
        }
        mLastX =X;
        mLastY = Y;
        super.onTouchEvent(event);
        return true;
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                intercepted = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                intercepted = true;
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
            default:
                break;
        }
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }
}
