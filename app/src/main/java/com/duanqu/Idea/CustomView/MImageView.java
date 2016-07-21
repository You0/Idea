package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/7/11.
 */
public class MImageView extends ImageView {
    public MImageView(Context context) {
        super(context);
    }

    public MImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width*1);
        setMeasuredDimension(width,height);
    }
}
