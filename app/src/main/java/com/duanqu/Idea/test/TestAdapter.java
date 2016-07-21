package com.duanqu.Idea.test;

import android.app.Activity;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.bean.MainMessageBean;

/**
 * Created by Administrator on 2016/6/30.
 */
public class TestAdapter extends BaseAdapter<MainMessageBean> {


    public TestAdapter(Activity context, Builder mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public int getItemViewType(int position) {
        MainMessageBean bean = datas.get(position);
        return bean.getType();
    }
}
