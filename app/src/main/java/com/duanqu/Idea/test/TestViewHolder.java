package com.duanqu.Idea.test;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.CustomView.CircleImageView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.MainMessageBean;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/7/2.
 */
public class TestViewHolder extends BaseItemImp<MainMessageBean> {
    private SimpleDraweeView messageHead;
    private TextView username;
    private TextView sign;
    private TextView time;
    private TextView content;
    private ImageView image1;
    private LinearLayout fwd;
    private LinearLayout cmt;
    private LinearLayout prz;


    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MainMessageBean data, int dynamicType) {
        String url = data.getUserInfo().get("headurl");
        Uri uri = Uri.parse(url);
        messageHead.setImageURI(uri);

    }

    @Override
    public int getViewRes() {
        return R.layout.item_content_type1;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        messageHead = (SimpleDraweeView) parent.findViewById(R.id.messageHead);
        username = (TextView) parent.findViewById(R.id.username);
        sign = (TextView) parent.findViewById(R.id.sign);
        time = (TextView) parent.findViewById(R.id.time);
        content = (TextView) parent.findViewById(R.id.content);
        image1 = (ImageView) parent.findViewById(R.id.image1);
        fwd = (LinearLayout) parent.findViewById(R.id.fwd);
        cmt = (LinearLayout) parent.findViewById(R.id.cmt);
        prz = (LinearLayout) parent.findViewById(R.id.prz);

        messageHead.setOnClickListener(this);
        username.setOnClickListener(this);
        //sign.setOnClickListener(this);
        //time.setOnClickListener(this);
        //content.setOnClickListener(this);
        image1.setOnClickListener(this);
        fwd.setOnClickListener(this);
        cmt.setOnClickListener(this);
        prz.setOnClickListener(this);


    }






}
