package com.duanqu.Idea.ViewHolder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.FeedActivity;
import com.duanqu.Idea.activity.ParallaxOtherUserInfoDisplayActivity;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.player.view.SuperVideoPlayer;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/4.
 */
public abstract class MainMessageBaseViewHolder extends BaseItemImp<MainMessageBean> {
    MainMessageBean data;
    private ImageView zan;
    private TextView comments;
    private TextView resend;
    private SimpleDraweeView messageHead;
    private TextView username;
    private TextView sign;
    private TextView time;
    //转发
    private LinearLayout fwd;
    //评论
    private LinearLayout cmt;
    //点赞
    private LinearLayout prz;
    private HashMap userInfo;
    @Override
    public int getViewRes() {
        return getViewLayout();
    }

    @Override
    public void onFindView(@NonNull View parent) {
        messageHead = (SimpleDraweeView) parent.findViewById(R.id.messageHead);
        username = (TextView) parent.findViewById(R.id.username);
        sign = (TextView) parent.findViewById(R.id.sign);
        time = (TextView) parent.findViewById(R.id.time);
        fwd = (LinearLayout) parent.findViewById(R.id.fwd);
        cmt = (LinearLayout) parent.findViewById(R.id.cmt);
        prz = (LinearLayout) parent.findViewById(R.id.prz);
        zan = (ImageView) parent.findViewById(R.id.zan);
        comments = (TextView) parent.findViewById(R.id.comments);
        resend = (TextView) parent.findViewById(R.id.resend);
        messageHead.setOnClickListener(this);
        username.setOnClickListener(this);
        sign.setOnClickListener(this);
        time.setOnClickListener(this);
        //转发
        fwd.setOnClickListener(this);
        cmt.setOnClickListener(this);
        prz.setOnClickListener(this);
        FindView(parent);
    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MainMessageBean data, int dynamicType) {
        this.data =data;
        String url = (String) data.getUserInfo().get("headurl");
        Uri uri = Uri.parse(url);
        messageHead.setImageURI(uri);
        userInfo = data.getUserInfo();
        username.setText((String)userInfo.get("nickname"));
        sign.setText((String)userInfo.get("sign"));
        //初始化评论数目
        comments.setText(String.valueOf((Integer) data.getMessageInfo().get("comment")));

        //初始化转发数目
        resend.setText(String.valueOf((Integer) data.getMessageInfo().get("resent")));

        //初始化点赞图标
        if((Integer)data.getMessageInfo().get("allike")!=0){
            zan.setImageResource(R.drawable.tl_menu_icon_prz_press);
            zan.setTag(true);
        }else{
            zan.setTag(false);
        }

        bindData(data);
    }

    protected abstract void bindData(MainMessageBean data);
    protected abstract int getViewLayout();
    protected abstract void  FindView(View parent);

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.fwd:{

                break;
            }
            case R.id.cmt:{
                Intent intent = new Intent(getActivityContext(), FeedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("first",data);
                intent.putExtras(bundle);
                getActivityContext().startActivity(intent);
                break;
            }

            case R.id.prz:{

                if(!(Boolean)zan.getTag()){
                    zan.setImageResource(R.drawable.tl_menu_icon_prz_press);
                    zan.setTag(true);
                }else{
                    zan.setImageResource(R.drawable.tl_menu_icon_prz_nor);
                    zan.setTag(false);
                }
                break;
            }

            case R.id.messageHead:
            case R.id.username:{
                Intent intent = new Intent(getActivityContext(), ParallaxOtherUserInfoDisplayActivity.class);
                intent.putExtra("userid", String.valueOf(data.getUserInfo().get("id")));
                getActivityContext().startActivity(intent);
                break;
            }

        }

    }
}
