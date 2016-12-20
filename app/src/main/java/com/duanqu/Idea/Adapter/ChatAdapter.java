package com.duanqu.Idea.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.ChatImageView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ChatActivity;
import com.duanqu.Idea.activity.ParallaxOtherUserInfoDisplayActivity;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.ChatBean;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by Droidroid on 2016/4/25.
 */
public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatBean> mData;

    public ChatAdapter(Context context, List<ChatBean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ChatBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatBean item = getItem(position);
        Viewholder viewholder = null;
        if (convertView == null) {
            viewholder = new Viewholder();
            switch (item.type) {
                case ChatBean.LEFT_IMAGE:
                    convertView = View.inflate(mContext, R.layout.item_image_left, null);
                    viewholder.imageView = (ChatImageView) convertView.findViewById(R.id.chat_image_left);
                    break;
                case ChatBean.LEFT_TEXT:
                    convertView = View.inflate(mContext, R.layout.item_text_left, null);
                    viewholder.textView = (TextView) convertView.findViewById(R.id.tv_text_left);
                    viewholder.userhead = (SimpleDraweeView) convertView.findViewById(R.id.head);
                    break;
                case ChatBean.RIGHT_IMAGE:
                    convertView = View.inflate(mContext, R.layout.item_image_right, null);
                    viewholder.imageView = (ChatImageView) convertView.findViewById(R.id.chat_image_right);
                    break;
                case ChatBean.RIGHT_TEXT:
                    convertView = View.inflate(mContext, R.layout.test_item_text_right, null);
                    viewholder.textView = (TextView) convertView.findViewById(R.id.tv_text_right);
                    viewholder.tag = (TextView) convertView.findViewById(R.id.tag);
                    viewholder.userhead = (SimpleDraweeView) convertView.findViewById(R.id.head);
                    break;

                case ChatBean.TIME:
                    convertView = View.inflate(mContext, R.layout.time_layout, null);
                    viewholder.textView = (TextView) convertView.findViewById(R.id.time);
                    break;

            }
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }
        switch (item.type) {
            case ChatBean.LEFT_IMAGE:
                viewholder.imageView.setImageResource(item.imgId);
                break;
            case ChatBean.LEFT_TEXT:
                if(item.getUserhead()==null || item.getUserhead().equals("")
                        ||item.getUserhead().equals("http://115.159.159.65:8080/userhead/")){
                    item.setUserhead(Config.defaultHeader);
                }
                Log.e("chatadapter",item.getUserhead());
                SetCircleImage(viewholder.userhead,item.getUserhead());
                viewholder.textView.setText(item.text);
                viewholder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("xxx");
                    }
                });

                viewholder.userhead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ParallaxOtherUserInfoDisplayActivity.class);
                        intent.putExtra("contact", item.getUsername());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case ChatBean.RIGHT_IMAGE:
                viewholder.imageView.setImageResource(item.imgId);
                break;
            case ChatBean.RIGHT_TEXT:
                viewholder.textView.setText(item.text);
                viewholder.tag.setVisibility(View.INVISIBLE);
                SetCircleImage(viewholder.userhead,Config.headurl);
                if(item.getTag()==1){
                    viewholder.tag.setVisibility(View.VISIBLE);
                    viewholder.tag.setText("错误");
                    viewholder.tag.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.red));
                }else if(item.getTag()==2){
                    viewholder.tag.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.green));
                    viewholder.tag.setVisibility(View.VISIBLE);
                    viewholder.tag.setText("送达");
                }
                break;

            case ChatBean.TIME:
                viewholder.textView.setText(item.time);
                break;
        }
        return convertView;
    }

    private  void SetCircleImage(SimpleDraweeView image,String url) {
        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        rp.setBorder(Color.BLACK, 1);
        //获取GenericDraweeHierarchy对象
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.
                newInstance(mContext.getResources()).setRoundingParams(rp).setFadeDuration(300).build();
        image.setHierarchy(hierarchy);
        image.setImageURI(Uri.parse(url));
    }

    class Viewholder {
        ChatImageView imageView;
        TextView textView;
        TextView tag;
        SimpleDraweeView userhead;
    }
}
