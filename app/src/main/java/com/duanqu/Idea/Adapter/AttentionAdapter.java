package com.duanqu.Idea.Adapter;

import android.app.Activity;

import com.duanqu.Idea.bean.AttentionBean;

/**
 * Created by Administrator on 2016/10/15.
 */
public class AttentionAdapter extends BaseAdapter<AttentionBean> {

    public AttentionAdapter(Activity context, Builder<AttentionBean> mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
