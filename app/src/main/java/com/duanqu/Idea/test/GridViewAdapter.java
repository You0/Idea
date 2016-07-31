package com.duanqu.Idea.test;

import android.app.Activity;
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
    private int InnerClassPosition;
    private Uri uri;
    private Activity context;

    public GridViewAdapter(Activity context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourse = resource;
        data = (LinkedList) objects;
        arrayList.addAll(data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourse, null);
            holder = new ViewHolder();
            imageView = (mSimpleDraweeView) convertView.findViewById(R.id.image);
            uri = Uri.parse((String) data.get(position));
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            hierarchy.setProgressBarImage(new CustomProgressBar());
            hierarchy.setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.huise));
            imageView.setImageURI(uri);

            holder.image = imageView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            uri = Uri.parse((String) data.get(position));
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            hierarchy.setProgressBarImage(new CustomProgressBar());
            hierarchy.setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.huise));
            holder.image.setImageURI(uri);

        }
        holder.image.setTag(position);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageDisplay.class);
                intent.putCharSequenceArrayListExtra("images", arrayList);
                intent.putExtra("position", (int) view.getTag());
                getContext().startActivity(intent);
                context.overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
            }
        });


        return convertView;
    }

    class ViewHolder {
        public mSimpleDraweeView image;
    }

}