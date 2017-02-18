package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/7/11.
 */
public class MyGriView extends GridView
{


    public MyGriView(Context context) {
        super(context);
    }

    public MyGriView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGriView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
