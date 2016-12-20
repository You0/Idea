package com.duanqu.Idea.ViewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.Type;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUserBlack extends BaseItemImp<Type>{
    private com.duanqu.Idea.bean.ParallaxUserBlack parallaxUserBlackBean;
    private View view;

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull Type data, int dynamicType) {
        parallaxUserBlackBean = (com.duanqu.Idea.bean.ParallaxUserBlack) data;
        int height = parallaxUserBlackBean.getHeight();
        if(height!=0){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,MyApplication.dip2px(height));
            view.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getViewRes() {
        return R.layout.black;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        view = parent.findViewById(R.id.view);
    }
}
