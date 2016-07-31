package com.duanqu.Idea.Adapter;

import android.app.Activity;

import com.duanqu.Idea.bean.SuggestGridBean;

/**
 * Created by Administrator on 2016/7/27.
 */
public class SuggestGridAdapter extends BaseAdapter<SuggestGridBean> {

    public SuggestGridAdapter(Activity context, Builder<SuggestGridBean> mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
