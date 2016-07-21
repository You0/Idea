package com.duanqu.Idea.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.duanqu.Idea.CustomView.CustomProgressBar;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ImageDisplay;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/7/11.
 */
public class GridViewAdapter extends ArrayAdapter {
    private int resourse;
    private mSimpleDraweeView imageView;
    private ViewHolder holder;
    private LinkedList data;
    private ArrayList arrayList = new ArrayList();
    private Uri uri;

    public GridViewAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resourse = resource;
        data = (LinkedList) objects;
        arrayList.addAll(data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(resourse,null);
            holder = new ViewHolder();
            imageView = (mSimpleDraweeView) convertView.findViewById(R.id.image);
            uri = Uri.parse((String) data.get(position));
            GenericDraweeHierarchy hierarchy =imageView.getHierarchy();
            hierarchy.setProgressBarImage(new CustomProgressBar());
            hierarchy.setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.huise));
            imageView.setImageURI(uri);

            holder.image = imageView;
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
            uri = Uri.parse((String) data.get(position));
            GenericDraweeHierarchy hierarchy =imageView.getHierarchy();
            hierarchy.setProgressBarImage(new CustomProgressBar());
            hierarchy.setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.huise));
            holder.image.setImageURI(uri);

        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageDisplay.class);
                intent.putCharSequenceArrayListExtra("images",arrayList);
                getContext().startActivity(intent);
            }
        });




        return convertView;
    }

    class ViewHolder
    {
        public mSimpleDraweeView image;
    }

}