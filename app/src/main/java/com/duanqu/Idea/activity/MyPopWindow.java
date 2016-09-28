package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.alibaba.sdk.android.AlibabaSDK;
import com.duanqu.Idea.R;
import com.duanqu.Idea.config.RequestCode;
import com.duanqu.qupai.sdk.android.QupaiService;

import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/6/28.
 */
public class MyPopWindow extends PopupWindow implements View.OnClickListener{
    private View QuickSend;
    private ImageView image;
    private ImageView video;
    private ImageView editor;
    private Activity activity;
    private Context context;

    public MyPopWindow(Activity activity, Context context)
    {
        this.context = context;
        this.activity = activity;
        //加载布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        QuickSend = inflater.inflate(R.layout.footnavigate, null);
        image = (ImageView) QuickSend.findViewById(R.id.sendImage);
        video = (ImageView) QuickSend.findViewById(R.id.sendVideo);
        editor = (ImageView) QuickSend.findViewById(R.id.sendText);

        //设置监听
        image.setOnClickListener(this);
        video.setOnClickListener(this);
        editor.setOnClickListener(this);

        // 设置SelectPicPopupWindow的View
        this.setContentView(QuickSend);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(600);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x80000000);
//        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);
        this.setFocusable(false);




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendVideo:{
                QupaiService qupaiService = AlibabaSDK
                        .getService(QupaiService.class);
                qupaiService.showRecordPage(activity, RequestCode.RECORDE_SHOW, false);
                break;
            }
        }
    }
}
