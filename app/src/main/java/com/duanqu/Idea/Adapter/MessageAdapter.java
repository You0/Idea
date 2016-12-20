package com.duanqu.Idea.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.MessageListBean;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jauker.widget.BadgeView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
public class MessageAdapter extends ArrayAdapter<MessageListBean> {
    private int resourse;
    private Context context;
    BadgeView badgeView;

    public MessageAdapter(Context context, int resource, List<MessageListBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourse = resource;
        badgeView = new BadgeView(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,resourse,null);
            viewHolder.local = (TextView) convertView.findViewById(R.id.local);
            viewHolder.iv_image = (SimpleDraweeView) convertView.findViewById(R.id.iv_image);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.last_message = (TextView) convertView.findViewById(R.id.last_message);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MessageListBean bean = getItem(position);
        badgeView.setTargetView(viewHolder.local);
        badgeView.setBadgeCount(Integer.valueOf(bean.getCount()));
        if(Integer.valueOf(bean.getCount()) !=0){
            badgeView.setVisibility(View.VISIBLE);
        }else{
            badgeView.setVisibility(View.GONE);
        }


        Log.e("adapter",bean.toString());
        viewHolder.tv_name.setText(bean.getNickname());
        viewHolder.last_message.setText(bean.getLastmessage());
        if(bean.getUserhead().equals("http://115.159.159.65:8080/userhead/")||
                bean.getUserhead()==null||bean.getUserhead().equals("")){
            bean.setUserhead(Config.defaultHeader);
        }
        SetCircleImage(viewHolder.iv_image,bean.getUserhead());
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
                newInstance(context.getResources()).setRoundingParams(rp).setFadeDuration(300).build();
        image.setHierarchy(hierarchy);
        image.setImageURI(Uri.parse(url));
    }


    class ViewHolder{
        TextView local;
        TextView tv_name;
        SimpleDraweeView iv_image;
        TextView last_message;
    }
}
