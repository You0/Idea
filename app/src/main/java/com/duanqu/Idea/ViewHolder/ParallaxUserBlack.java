package com.duanqu.Idea.ViewHolder;

import android.support.annotation.NonNull;
import android.view.View;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.Type;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUserBlack extends BaseItemImp<Type>{
    private com.duanqu.Idea.bean.ParallaxUserBlack parallaxUserBlackBean;

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull Type data, int dynamicType) {
        parallaxUserBlackBean = (com.duanqu.Idea.bean.ParallaxUserBlack) data;
    }

    @Override
    public int getViewRes() {
        return R.layout.black;
    }

    @Override
    public void onFindView(@NonNull View parent) {
    }
}
