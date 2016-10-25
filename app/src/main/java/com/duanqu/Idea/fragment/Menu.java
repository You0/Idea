package com.duanqu.Idea.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ParallaxUserInfoDisplayActivity;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/5/17.
 */
public class Menu extends BaseFragment implements View.OnClickListener{
    RelativeLayout tv_userdata;
    RelativeLayout tv_favorite;
    RelativeLayout tv_bookmark;
    LinearLayout ll_menu;
    SimpleDraweeView bg;
    SimpleDraweeView userhead;
    TextView tv_sentence;
    TextView tv_id;
    private  View view;
    private GenericDraweeHierarchy hierarchy;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu,null);

        tv_userdata = (RelativeLayout) view.findViewById(R.id.tv_userdata);
        tv_favorite = (RelativeLayout) view.findViewById(R.id.tv_favorite);
        tv_bookmark = (RelativeLayout) view.findViewById(R.id.tv_bookmark);
        ll_menu = (LinearLayout) view.findViewById(R.id.ll_menu);
        bg = (SimpleDraweeView) view.findViewById(R.id.bg);
        tv_sentence = (TextView) view.findViewById(R.id.tv_sentence);
        tv_id = (TextView) view.findViewById(R.id.tv_id);
        userhead = (SimpleDraweeView) view.findViewById(R.id.userhead);
        tv_userdata.setOnClickListener(this);
        tv_favorite.setOnClickListener(this);
        tv_bookmark.setOnClickListener(this);

        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        //获取GenericDraweeHierarchy对象
         hierarchy = GenericDraweeHierarchyBuilder.
                newInstance(getResources()).setRoundingParams(rp).build();

        userhead.setHierarchy(hierarchy);
        RefreshUI();
        return view;
    }

    public void RefreshUI() {

        //获取SimpleDraweeView
        userhead.setImageURI(Uri.parse(Config.headurl));

        if(!Config.imageurl.equals("")){
            bg.setImageURI(Uri.parse(Config.imageurl));
        }
        if(!Config.sign.equals("")){
            tv_sentence.setText(Config.sign);
        }
        if(!Config.nickname.equals("")){
            tv_id.setText(Config.nickname);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        RefreshUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_userdata:{
                Intent intent = new Intent(getActivity(), ParallaxUserInfoDisplayActivity.class);
                //在这里传递一些数据进入
                startActivity(intent);
                break;
            }
        }
    }
}
