package com.duanqu.Idea.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.CommentBean;

import java.util.List;

/**
 * Created by Me on 2017/1/22.
 */

public class CommentAdapter extends android.widget.BaseAdapter {
    protected Activity context;
    protected LayoutInflater mInflater;
    protected List datas;

    public CommentAdapter(Activity context,List beans)
    {
        this.datas = beans;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view = mInflater.inflate(R.layout.comment_listview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.content = (TextView) view.findViewById(R.id.content);
            viewHolder.tag = (TextView) view.findViewById(R.id.tag);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        CommentBean bean = (CommentBean) getItem(i);

        viewHolder.content.setText(bean.getContent());
        viewHolder.tag.setText(bean.getVideoTag()+bean.getImageTag());

        return view;
    }

    class ViewHolder
    {
        TextView content;
        TextView tag;
    }



}
