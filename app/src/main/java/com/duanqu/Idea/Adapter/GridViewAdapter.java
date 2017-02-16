package com.duanqu.Idea.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.VideoPlayViewActivity;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Me on 2016/3/10 0010.
 */
public class GridViewAdapter extends ArrayAdapter {
    private int Resource;
    private LinkedList arrayList = new LinkedList();
    private Context context;


    public GridViewAdapter(Context context, int resource, List objects) {
        super(context,resource,objects);
        this.context = context;
        arrayList= new LinkedList();
        arrayList.addAll(objects);
        Resource = resource;
    }






    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = View.inflate(context,R.layout.more_vidoe_gridview_item,null);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.cover_image = (SimpleDraweeView) convertView.findViewById(R.id.cover_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MainMessageBean mainMessageBean = (MainMessageBean) arrayList.get(position);
        holder.cover_image.setImageURI(Uri.parse((String) mainMessageBean.getImages().get(0)));
        holder.content.setText(mainMessageBean.getTextContent());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoPlayViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", mainMessageBean);
                intent.putExtra("data",bundle);
                intent.putExtra("url", Datas.NewestVideoFeed);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private final class ViewHolder {
        SimpleDraweeView cover_image;
        TextView content;

    }
}
