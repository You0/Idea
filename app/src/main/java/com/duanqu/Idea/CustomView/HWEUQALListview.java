package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/7/26.
 */
public class HWEUQALListview extends ListView {
    public HWEUQALListview(Context context) {
        super(context);
    }

    public HWEUQALListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HWEUQALListview(Context context, AttributeSet attrs, int defStyleAttr) {
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
