package com.duanqu.Idea.Adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.InnerFeedBean;
import com.duanqu.qupai.project.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */
public class InnerFeedAdapter extends android.widget.BaseAdapter {
    private ArrayList<InnerFeedBean> datas;
    private Context context;


    public InnerFeedAdapter(Context context,ArrayList<InnerFeedBean> datas)
    {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.feed_inner_listview_item,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InnerFeedBean bean = datas.get(position);
        viewHolder.textView.setText(bean.getLeftusername()+ " 回复 " +bean.getRightusername()
        +" : " + bean.getContent());

        return convertView;
    }


    class ViewHolder{
        private TextView textView;
    }
}
