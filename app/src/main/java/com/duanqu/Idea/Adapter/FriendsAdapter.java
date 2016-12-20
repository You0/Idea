package com.duanqu.Idea.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.FriendBean;
import com.duanqu.Idea.bean.FriendsListBean;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */
public class FriendsAdapter extends ArrayAdapter<FriendBean> {
    int resource;
    ArrayList<FriendBean>  datas;

    public FriendsAdapter(Context context, int resource, List<FriendBean> objects) {
        super(context, resource, objects);
        this.resource = resource;
        datas = (ArrayList<FriendBean>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(getContext(), R.layout.friends_listview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_image = (mSimpleDraweeView) convertView.findViewById(R.id.iv_image);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.last_message = (TextView) convertView.findViewById(R.id.last_message);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FriendBean friendBean = getItem(position);
        if(friendBean.getHeadurl().equals("http://115.159.159.65:8080/userhead/")||
                friendBean.getHeadurl()==null||friendBean.getHeadurl().equals("")){
            friendBean.setHeadurl(Config.defaultHeader);
        }
        SetCircleImage(viewHolder.iv_image,friendBean.getHeadurl());
        //viewHolder.iv_image.setImageURI(Uri.parse(friendBean.getHeadurl()));
        viewHolder.tv_name.setText(friendBean.getNickname());
        viewHolder.last_message.setText(friendBean.getSign());
        return convertView;
    }

    private  void SetCircleImage(SimpleDraweeView image, String url) {
        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        rp.setBorder(Color.BLACK, 1);
        //获取GenericDraweeHierarchy对象
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.
                newInstance(getContext().getResources()).setRoundingParams(rp).setFadeDuration(300).build();
        image.setHierarchy(hierarchy);
        image.setImageURI(Uri.parse(url));
    }

    class ViewHolder{
        mSimpleDraweeView iv_image;
        TextView tv_name;
        TextView last_message;
    }

}
