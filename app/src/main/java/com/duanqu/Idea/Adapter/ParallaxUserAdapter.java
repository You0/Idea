package com.duanqu.Idea.Adapter;

import android.app.Activity;
import android.util.Log;

import com.duanqu.Idea.bean.Type;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUserAdapter extends BaseAdapter<Type> {
    public ParallaxUserAdapter(Activity context, Builder mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public boolean isEnabled(int position) {
        if(position==0||position ==6){
            return false;
        }
        return super.isEnabled(position);
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }
}
