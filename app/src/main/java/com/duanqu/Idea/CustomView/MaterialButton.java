package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.duanqu.Idea.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.util.Property;

public class MaterialButton extends Button {
    private Paint backgroundPaint;
    private float paintX, paintY, radius;

    public MaterialButton(Context context) {
        super(context);
        init(null, 0);
    }

    public MaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MaterialButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MaterialButton, defStyle, 0);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawCircle(paintX, paintY, radius, backgroundPaint);
        canvas.restore();
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //记录坐标
            paintX = event.getX();
            paintY = event.getY();
            //启动动画
            startAnimator();
        }
        return super.onTouchEvent(event);
    }

    private void startAnimator() {

        //计算半径变化区域
        int start, end;

        if (getHeight() < getWidth()) {
            start = getHeight();
            end = getWidth();
        } else {
            start = getWidth();
            end = getHeight();
        }

        float startRadius = (start / 2 > paintY ? start - paintY : paintY) * 0.85f;
        float endRadius = (end / 2 > paintX ? end - paintX : paintX) * 1.15f;

        //新建动画
        AnimatorSet set = new AnimatorSet();
        //添加变化属性
        set.playTogether(
                //半径变化
                ObjectAnimator.ofFloat(this, mRadiusProperty, startRadius, endRadius),
                //颜色变化 黑色到透明
                ObjectAnimator.ofObject(this, mBackgroundColorProperty, new ArgbEvaluator(), Color.BLUE, Color.TRANSPARENT)
        );
        // 设置时间
        set.setDuration((long) (1200 / end * endRadius));
        //先快后慢
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }


    private Property<MaterialButton, Float> mRadiusProperty = new Property<MaterialButton, Float>(Float.class, "radius") {
        @Override
        public Float get(MaterialButton object) {
            return object.radius;
        }

        @Override
        public void set(MaterialButton object, Float value) {
            object.radius = value;
            //刷新Canvas
            invalidate();
        }
    };

    private Property<MaterialButton, Integer> mBackgroundColorProperty = new Property<MaterialButton, Integer>(Integer.class, "bg_color") {
        @Override
        public Integer get(MaterialButton object) {
            return object.backgroundPaint.getColor();
        }

        @Override
        public void set(MaterialButton object, Integer value) {
            object.backgroundPaint.setColor(value);
        }
    };
}