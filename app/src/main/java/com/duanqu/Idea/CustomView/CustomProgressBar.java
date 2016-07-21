package com.duanqu.Idea.CustomView;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/7/15.
 */
public class CustomProgressBar extends Drawable {
    private Paint paint = new Paint();
    private RectF oval = new RectF();
    private int mProgress = 0;


    @Override
    public void draw(Canvas canvas) {
        paint.setAntiAlias(true);// 设置是否抗锯齿
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
        paint.setColor(Color.WHITE);// 设置画笔灰色
        paint.setStrokeWidth(7);// 设置画笔宽度
        paint.setStyle(Paint.Style.STROKE);// 设置中空的样式
        int padding = canvas.getHeight()/3;
        oval.set(padding, padding, 2*padding, 2*padding);
        canvas.drawArc(oval, 0, ((float) mProgress/10000)*360, false,
                paint);

    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }


    @Override
    protected boolean onLevelChange(int level) {
        mProgress = level;
        if(level > 0 && level < 10000) {
            invalidateSelf();
            return true;
        }else {
            return false;
        }
    }
}
