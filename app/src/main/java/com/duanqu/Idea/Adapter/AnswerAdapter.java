package com.duanqu.Idea.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.HistoryAndCacheBean;
import java.util.ArrayList;

/**
 * Created by Me on 2017/1/24.
 */

public class AnswerAdapter extends android.widget.BaseAdapter {
    private Context context;
    private int resourse;
    private ArrayList beans;

    public AnswerAdapter(Context context, int resourse, ArrayList arrayList){
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
            vh.answer = (TextView) view.findViewById(R.id.answer);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
        HistoryAndCacheBean bean = (HistoryAndCacheBean) beans.get(i);
        vh.content.setText("原贴:"+MyApplication.Decode(bean.getContent()));
        vh.tip.setText(MyApplication.Decode(bean.getMedia()));
        vh.answer.setText("我的回复:"+MyApplication.Decode(bean.getAnswer()));
        return view;
    }

    class ViewHolder{
        TextView content;
        TextView tip;
        TextView answer;
    }

}
