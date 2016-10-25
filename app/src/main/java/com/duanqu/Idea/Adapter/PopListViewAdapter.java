package com.duanqu.Idea.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ImageWatchActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Me on 2016/3/12 0012.
 */
public class PopListViewAdapter extends ArrayAdapter<ImageWatchActivity.ImageFloder> {
    private int Resource;
    private ArrayList<ImageWatchActivity.ImageFloder> arrayList;
    private ImageLoader mImageloader;
    private DisplayImageOptions options;

    public PopListViewAdapter(Context context, int resource, List<ImageWatchActivity.ImageFloder> objects) {
        super(context, resource, objects);
        Resource = resource;
        arrayList = (ArrayList<ImageWatchActivity.ImageFloder>) objects;
        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT )
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(Resource, null);
            viewHolder = new ViewHolder();
            viewHolder.id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
            viewHolder.id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
            viewHolder.id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.id_dir_item_count.setText(String.valueOf(arrayList.get(position).getPicSize()));
        viewHolder.id_dir_item_name.setText(arrayList.get(position).getDirpath().getName());
        //Uri uri =  Uri.fromFile(new File(arrayList.get(position).getFirstImagePath()));
        mImageloader.displayImage("file://"+arrayList.get(position).getFirstImagepath(),viewHolder.id_dir_item_image,options);
        return convertView;
    }


    class ViewHolder {
        TextView id_dir_item_name;
        TextView id_dir_item_count;
        ImageView id_dir_item_image;
    }

    public int getRio(int h,int w)
    {
        int max;
        max = h/100;
        if(w/100>max)
        {
            max=w/100;
        }
        if(max<1){
            max = 1;
        }
        return max;
    }

}
