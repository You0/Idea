package com.duanqu.Idea.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.MaterialButton;
import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.test.Datas;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/10/9.
 */
public class RegisterPoupWindow extends PopupWindow implements View.OnClickListener {
    private Activity context;
    private View view;
    private MaterialEditText nickname;
    private Button button;
    private SharedPreferences sharedPreferences;
    private String token;
    private ProgressDialog mProgressDialog;
    private String username;
    private String temp_nickname;


    public RegisterPoupWindow(final Activity context, String username, String token) {
        this.context = context;
        this.token = token;
        this.username = username;
        final LayoutInflater inflater = context.getLayoutInflater().from(context);
        view = inflater.inflate(R.layout.register_poupwindow, null);

        nickname = (MaterialEditText) view.findViewById(R.id.nickname);
        button = (Button) view.findViewById(R.id.register);

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.ScaleBig);
        // 实例化一个ColorDrawable颜色为半透明
        // /
        ColorDrawable dw = new ColorDrawable(0x96000000);
//        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mProgressDialog = ProgressDialog.show(context, null, "请稍后...");
        String nick_name = nickname.getText().toString();
        temp_nickname = nick_name;
        try {
            nick_name =  URLEncoder.encode(URLEncoder.encode(nick_name, "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtils.post().url(Datas.Nickname)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addParams("username", username)
                .addParams("nickname", nick_name)
                .addParams("Token", token).build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        Log.e("xxx","注册成功");
                        if(response.code()==200)
                        {
                            ((FirstStepPoupWindowForRegister)context).handler
                                    .sendEmptyMessage(2);
                            //StartActivity(temp_nickname);
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        // Response response1 = (Response) response;


                    }
                });


        //这里传递一些信息给主页面。

    }

    public void StartActivity() {
        if (sharedPreferences == null) {
            sharedPreferences = MyApplication.getContext().getSharedPreferences("userInfo", context.MODE_PRIVATE);
        }
        //sharedPreferences.edit().commit();
        sharedPreferences.edit().clear().commit();
        //将用户参数保存到本地

        sharedPreferences.edit().putString("Token", token)
                .putString("username",username)
                .putString("nickname",temp_nickname)
                .putBoolean("ifLogin", true).commit();

        Config.Token = token;
        Config.username = username;
        Config.nickname = temp_nickname;


        //然后启动主activity
        //这里可以传一些参数给main_aty

        mProgressDialog.cancel();
        RegisterPoupWindow.this.dismiss();
        Intent intent = new Intent(context, MainActivity1.class);
        intent.putExtra("tj",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

