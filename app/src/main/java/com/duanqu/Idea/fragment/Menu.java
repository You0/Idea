package com.duanqu.Idea.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.AboutActivity;
import com.duanqu.Idea.activity.AttentionActivity;
import com.duanqu.Idea.activity.HistoryAndCacheCommentActitity;
import com.duanqu.Idea.activity.MainActivity1;
import com.duanqu.Idea.activity.ParallaxUserInfoDisplayActivity;
import com.duanqu.Idea.activity.RegisterActivity;
import com.duanqu.Idea.app.MyApplication;
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
    //SimpleDraweeView userhead;
    TextView tv_sentence;
    TextView tv_id;
    private  View view;
    private GenericDraweeHierarchy hierarchy;

    private Dialog dialog;

    private TextView tv_account;
    private TextView tv_about;
    private TextView tv_setting;

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
        tv_about = (TextView) view.findViewById(R.id.tv_about);
        tv_setting = (TextView) view.findViewById(R.id.tv_setting);
        tv_account = (TextView) view.findViewById(R.id.tv_account);

        //userhead = (SimpleDraweeView) view.findViewById(R.id.userhead);
        tv_userdata.setOnClickListener(this);
        tv_favorite.setOnClickListener(this);
        tv_bookmark.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        tv_about.setOnClickListener(this);

//        //初始化圆角圆形参数对象
//        RoundingParams rp = new RoundingParams();
//        //设置图像是否为圆形
//        rp.setRoundAsCircle(true);
//        //获取GenericDraweeHierarchy对象
//         hierarchy = GenericDraweeHierarchyBuilder.
//                newInstance(getResources()).setRoundingParams(rp).build();
//
//        userhead.setHierarchy(hierarchy);
        RefreshUI();
        return view;
    }

    public void RefreshUI() {

        //获取SimpleDraweeView
        //userhead.setImageURI(Uri.parse(Config.headurl));

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

            case R.id.tv_favorite:{
                Intent intent = new Intent(getActivity(), AttentionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.scale_anim, 0);
                break;
            }
            case R.id.tv_bookmark:{
                Intent intent = new Intent(getActivity(),HistoryAndCacheCommentActitity.class);
                intent.putExtra("type","cache");
                intent.putExtra("uid",Config.userid);
                startActivity(intent);
                break;
            }

            //账号
            case R.id.tv_account:{
                InitDialog();

                break;
            }

            //设置
            case R.id.tv_setting:{
                //InitDialog();
                break;
            }

            case R.id.tv_about:{
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tv_change:{
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }

            case R.id.tv_pwd:{
                Toast.makeText(MyApplication.getContext(), "暂时还不支持修改密码", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.tv_logout:{
                MyApplication.setSharedPreferences("ifLogin",false);
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }


        }
    }

    private void InitDialog() {
        RelativeLayout tv_change;
        RelativeLayout tv_pwd;
        RelativeLayout tv_logout;

        final View view2 = View.inflate(getActivity(), R.layout.account_dialog, null);

        tv_change = (RelativeLayout) view2.findViewById(R.id.tv_change);
        tv_pwd=(RelativeLayout) view2.findViewById(R.id.tv_pwd);
        tv_logout=(RelativeLayout) view2.findViewById(R.id.tv_logout);

        tv_change.setOnClickListener(this);
        tv_pwd.setOnClickListener(this);
        tv_logout.setOnClickListener(this);



        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                (int) (Config.WIDTH/3*2),
                LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.addContentView(view2, p);
        dialog.setTitle(null);
        dialog.show();
        dialog.setCancelable(true);

    }
}
