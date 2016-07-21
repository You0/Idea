package com.duanqu.Idea.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Administrator on 2016/6/30.
 */
public abstract class BaseItemImp<T> implements BaseItemView<T>,View.OnClickListener{
    protected Activity context;

    public BaseItemImp(){

    }
    public BaseItemImp(Activity context){
        this.context = context;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBindData(int position, @NonNull View v, @NonNull T data, int dynamicType) {
        bindData(position, v, data, dynamicType);
    }

    @Override
    public Activity getActivityContext() {
        return context;
    }

    @Override
    public void setActivityContext(Activity context) {
        this.context=context;
    }

    protected abstract void bindData(int position, @NonNull View v, @NonNull T data, final int dynamicType);

}
