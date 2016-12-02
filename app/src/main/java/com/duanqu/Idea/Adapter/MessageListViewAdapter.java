package com.duanqu.Idea.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duanqu.Idea.CustomView.SwipeLayout;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MessageListViewAdapter extends ArrayAdapter<MessageBean> {
    private int Resource;
    private Context context;
    private List<MessageBean> datas;
    private ArrayList<SwipeLayout> OptenItem = new ArrayList<>();

    public MessageListViewAdapter(Context context, int resource, List<MessageBean> objects) {
        super(context, resource, objects);
        this.Resource = resource;
        datas = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(Resource,null);vh = new ViewHolder();
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.last_message = (TextView) convertView.findViewById(R.id.last_message);
            vh.delete = (TextView) convertView.findViewById(R.id.tv_del);
            vh.rl_message = (RelativeLayout) convertView.findViewById(R.id.rl_message);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_name.setText(datas.get(position).getName());
        vh.last_message.setText(datas.get(position).getLastMessage());

        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                notifyDataSetChanged();
            }
        });

        vh.rl_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("dd","rl_message");
            }
        });


        SwipeLayout swipeLayout = (SwipeLayout)convertView;

        swipeLayout.setSwipeLayoutListener(new SwipeLayout.OnSwipeLayoutListener() {
            @Override
            public void onClose(SwipeLayout mSwipeLayout) {
                OptenItem.remove(mSwipeLayout);
            }

            @Override
            public void onOpen(SwipeLayout mSwipeLayout) {
                OptenItem.add(mSwipeLayout);
            }

            @Override
            public void onDraging(SwipeLayout mSwipeLayout) {

            }

            @Override
            public void onStartClose(SwipeLayout mSwipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout mSwipeLayout) {
                Log.e("startopen","startopen");
                for(SwipeLayout swipeLayout1 : OptenItem){
                    swipeLayout1.close();
                }
            }
        });


        return convertView;
    }
    class ViewHolder{
        TextView tv_name;
        TextView last_message;
        TextView delete;
        RelativeLayout rl_message;
    }


}
