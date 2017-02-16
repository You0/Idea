package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.duanqu.Idea.CustomView.LoadingButton;
import com.duanqu.Idea.R;

/**
 * Created by Me on 2017/1/16.
 */

public class LoadingButtonPopupWindow extends PopupWindow {
    private Activity activity;
    private Context context;
    private View view;
    private LoadingButton loadingButton;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoadingButtonPopupWindow.this.dismiss();
        }
    };


    public LoadingButtonPopupWindow(Activity activity, Context context)
    {
        this.context = context;
        this.activity = activity;
        //加载布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.loading_button, null);
        loadingButton = (LoadingButton) view.findViewById(R.id.loading_button);
        loadingButton.setTargetProgress(360);
        //设置监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x80000000);
//        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);
        this.setFocusable(false);
    }

    public void Complete()
    {
       loadingButton.setFinish(true);
        loadingButton.setCallback(new LoadingButton.Callback() {
            @Override
            public void complete() {
                handler.sendEmptyMessageDelayed(0,500);
            }
        });

    }






}
