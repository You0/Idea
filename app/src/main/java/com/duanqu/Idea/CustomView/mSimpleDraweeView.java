package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/7/11.
 */
public class mSimpleDraweeView extends SimpleDraweeView {
    public mSimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public mSimpleDraweeView(Context context) {
        super(context);
    }

    public mSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public mSimpleDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width*1);
        setMeasuredDimension(width,height);
    }
}
