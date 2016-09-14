package com.duanqu.Idea.Adapter;

import android.app.Activity;

import com.duanqu.Idea.bean.FriendsListBean;

/**
 * Created by Administrator on 2016/9/14.
 */
public class FriendsListViewAdapter extends BaseAdapter<FriendsListBean> {

    public FriendsListViewAdapter(Activity context, Builder<FriendsListBean> mBuilder) {
        super(context, mBuilder);
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
