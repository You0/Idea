package com.duanqu.Idea.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.HistoryAndCacheBean;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Me on 2017/1/24.
 */

public class HistoryAndCacheAdapter extends android.widget.BaseAdapter {
    private Context context;
    private int resourse;
    private ArrayList beans;

    public HistoryAndCacheAdapter(Context context, int resourse, ArrayList arrayList){
        this.context = context;
        this.resourse = resourse;
        this.beans = arrayList;
    }


    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int i) {
        return beans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view==null){
            view = View.inflate(context,resourse,null);
            vh = new ViewHolder();
            vh.content = (TextView) view.findViewById(R.id.content);
            vh.tip = (TextView) view.findViewById(R.id.tip);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
        HistoryAndCacheBean bean = (HistoryAndCacheBean) beans.get(i);
        vh.content.setText(MyApplication.Decode(bean.getContent()));
        vh.tip.setText(MyApplication.Decode(bean.getMedia()));
        return view;
    }

    class ViewHolder{
        TextView content;
        TextView tip;
    }

}
