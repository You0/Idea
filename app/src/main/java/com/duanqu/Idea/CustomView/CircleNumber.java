package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.duanqu.Idea.R;

/**
 * Created by Administrator on 2016/10/20.
 */
public class CircleNumber extends View{
    private Paint paint;
    private String car = "0";
    private int height;
    private int width;


    public CircleNumber(Context context) {
        this(context,null);
    }

    public CircleNumber(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CircleNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCar(String car) {
        this.car = car;
        invalidate();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = getMeasuredHeight();
        width = getMeasuredWidth();

    }

    private int min(int height, int width) {
        return height < width ? height : width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("xxxx","width"+width+"height"+height+"");
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawCircle(width/2,height/2,
                min(height,width)/2,paint);

        canvas.drawCircle(width/2,height/2,10,paint);

        if(car!=null){
            paint.setColor(Color.WHITE);
            //canvas.drawCircle(width/2,height/2,10,paint);
            paint.setTextSize(min(height,width)/4*3);
            Rect rect = new Rect();
            paint.getTextBounds(car,0,car.length(),rect);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
            float fontWidth = fontMetrics.leading - fontMetrics.ascent;
            canvas.drawText(car, width/2-fontWidth/3
                    ,height/ 2+fontTotalHeight/4, paint);
        }

    }
}
