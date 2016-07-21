package com.duanqu.Idea.ViewHolder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.player.view.SuperVideoPlayer;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/4.
 */
public abstract class MainMessageBaseViewHolder extends BaseItemImp<MainMessageBean> {
    private SimpleDraweeView messageHead;
    private TextView username;
    private TextView sign;
    private TextView time;
    private LinearLayout fwd;
    private LinearLayout cmt;
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

        messageHead.setOnClickListener(this);
        username.setOnClickListener(this);
        sign.setOnClickListener(this);
        time.setOnClickListener(this);
        fwd.setOnClickListener(this);
        cmt.setOnClickListener(this);
        prz.setOnClickListener(this);
        FindView(parent);
    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MainMessageBean data, int dynamicType) {
        String url = data.getUserInfo().get("headurl");
        Uri uri = Uri.parse(url);
        messageHead.setImageURI(uri);
        userInfo = data.getUserInfo();
        username.setText((String)userInfo.get("nickname"));
        sign.setText((String)userInfo.get("sign"));

        bindData(data);
    }

    protected abstract void bindData(MainMessageBean data);
    protected abstract int getViewLayout();
    protected abstract void  FindView(View parent);
}
