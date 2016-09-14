package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;

/**
 * Created by Administrator on 2016/9/13.
 */
public class ToSomeOne extends ViewGroup {
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mLastX = 0;
    private TextView textView;

    public ToSomeOne(Context context) {
        super(context);
        initView();
    }

    public ToSomeOne(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ToSomeOne(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
        //强制为水平
        textView = new TextView(MyApplication.getContext());
        textView.setText("艾特一下@");
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        textView.setTextSize(20);
        addView(textView);

        View view = UserTag.Build(getContext(),"","");
        View view1 = UserTag.Build(getContext(),"","");
        View view2 = UserTag.Build(getContext(),"","");
        addView(view);
        addView(view1);
        addView(view2);
        //setOrientation(LinearLayout.HORIZONTAL);
    }


    //高度和宽度也给限死,不允许用户自定义
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        int PaddingBotton = 0;
        int PaddingTop = 0;
        int WidthCount = 0;
        if (getChildCount() != 0) {
            View ChildView;
            for (int i = 0; i < getChildCount(); i++) {
                int PaddingLeft = 0;
                int PaddingRight = 0;
                ChildView = getChildAt(i);
                PaddingLeft = ChildView.getPaddingLeft();
                PaddingRight = ChildView.getPaddingRight();
                WidthCount += getChildAt(i).getMeasuredWidth() + PaddingLeft + PaddingRight;
                if (PaddingBotton < ChildView.getPaddingBottom()) {
                    PaddingBotton = ChildView.getPaddingBottom();
                }

                if (PaddingTop < ChildView.getPaddingTop()) {
                    PaddingTop = ChildView.getPaddingTop();
                }

            }
            setMeasuredDimension(WidthCount, MyApplication.dip2px(40)+PaddingBotton+PaddingTop);

            System.out.println("width"+WidthCount);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();

                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }

        System.out.println("left"+childLeft);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean interceptor = true;
        mVelocityTracker.addMovement(event);

        int x = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //让滑动更顺滑
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;

            }

            case MotionEvent.ACTION_MOVE: {
                int deltx = x - mLastX;
                scrollBy(-deltx, 0);

            }

            case MotionEvent.ACTION_UP: {
                int scrollx = getScrollX();
                mVelocityTracker.computeCurrentVelocity(100);
                float xVelocity = mVelocityTracker.getXVelocity();
                int deltx = x - mLastX;

                if (Math.abs(xVelocity) < 20) {
                    interceptor = false;
                } else {
                    smoothScrollBy(-deltx, 0);
                }
                mVelocityTracker.clear();
                break;
            }
        }
        mLastX = x;
        return interceptor;
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
