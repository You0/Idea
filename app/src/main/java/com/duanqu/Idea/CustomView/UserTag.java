package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.duanqu.Idea.R;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;

/**
 * Created by Administrator on 2016/9/14.
 */
public class UserTag{
    private View view;
    private mSimpleDraweeView userhead;
    private TextView userid;
    private String url;
    private String name;
    public UserTag(Context context,String url,String name) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate( R.layout.user_tag,null);
        userhead = (mSimpleDraweeView) view.findViewById(R.id.userhead);
        userid = (TextView) view.findViewById(R.id.userid);
        this.name = name;
        this.url = url;
        initView();
    }

    public String getName() {
        return name;
    }

    private void initView() {
        //设置为圆形
        userhead.setImageURI(Uri.parse(url));
        GenericDraweeHierarchy hier = userhead.getHierarchy();
        hier.setRoundingParams(RoundingParams.asCircle());

        userid.setText(name);

    }

    public View getView() {
        view.setPadding(15,1,0,1);
        return view;
    }

    public static View Build(Context context,String url,String name){
        UserTag userTag = new UserTag(context,url,name);
        return userTag.getView();
    }
}
