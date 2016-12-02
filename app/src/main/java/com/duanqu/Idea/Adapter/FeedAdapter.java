package com.duanqu.Idea.Adapter;

import android.app.Activity;
import android.util.Log;

import com.duanqu.Idea.bean.FeedBean;
import com.duanqu.Idea.bean.MainMessageBean;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FeedAdapter extends BaseAdapter<MainMessageBean>{


    public FeedAdapter(Activity context, Builder<MainMessageBean> mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public int getItemViewType(int position) {
        MainMessageBean bean = datas.get(position);
        //Log.e("FeedAdapter",bean.toString()+" is null :" +(bean==null));
        return bean.getType();


    }
}
