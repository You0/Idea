package com.duanqu.Idea.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Administrator on 2016/6/30.
 */
public interface BaseItemView<T> {
    int getViewRes();
    void onFindView(@NonNull View parent);
    void onBindData(final int position, @NonNull View v, @NonNull T data, final int dynamicType);
    Activity getActivityContext();
    void setActivityContext(Activity context);
}
