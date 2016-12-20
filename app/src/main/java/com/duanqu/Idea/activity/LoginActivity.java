package com.duanqu.Idea.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.ParallaxOtherUserBean;
import com.duanqu.Idea.test.Datas;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/10/10.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText userNameText;
    private MaterialEditText passwdText;
    private Button bnRegister;
    private ProgressDialog mProgressDialog;
    private String Token;
    private String username;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private volatile ParallaxOtherUserBean bean;
    private Object mutitex = new Object();
    final int Error = 0;
    final int Success = 1;
    final int InfoGet = 2;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Error: {
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                }

                case Success: {
                    if (editor == null) {
                        mSharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                        editor = mSharedPreferences.edit();
                    }
                    editor.clear().commit();

                    editor.putString("Token", Token)
                            .putString("username", username)
                            .commit();

                    if(Token == null){
                        handler.sendEmptyMessage(Error);
                        return;
                    }

                    OkHttpUtils.post().url(Datas.GetUserInfo)
                            .addParams("username", username)
                            .addParams("contact", username)
                            .addParams("Token", Token).build().execute(new com.zhy.http.okhttp.callback.Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response, int id) throws Exception {
                            String json = response.body().string();
                            Gson gson = new Gson();


                            synchronized (mutitex) {
                                bean = gson.fromJson(json, ParallaxOtherUserBean.class);
                                Log.e("bean", bean.toString());
                            }
                            handler.sendEmptyMessage(InfoGet);
                            return null;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(Object response, int id) {

                        }
                    });
                    break;
                }


                case InfoGet: {
                    synchronized (mutitex) {
                        Log.e("Login", bean.toString());
                        editor.putBoolean("ifLogin", true)
                                .putString("imageurl", bean.getImageurl())
                                .putString("sign", bean.getSign())
                                .putString("headurl", bean.getHeadurl())
                                .putString("nickname", bean.getNickname())
                                .putString("sex", bean.getSex())
                                .putString("grades", bean.getGrades())
                                .putString("major", bean.getMajor())
                                .putString("school", bean.getSchool())
                                .putString("email", bean.getE_mail())
                                .commit();
                        MyApplication.LoadPreferences();
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
                }

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_aty);

        userNameText = (MaterialEditText) findViewById(R.id.userNameText);
        passwdText = (MaterialEditText) findViewById(R.id.passwdText);
        bnRegister = (Button) findViewById(R.id.button);

        bnRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            final String SchoolId = userNameText.getText().toString();
            String Passwd = passwdText.getText().toString();
            username = SchoolId;
            if (SchoolId.equals("") || Passwd.equals("")) {
                Snackbar.make(bnRegister, "请输入账号密码", Snackbar.LENGTH_LONG)
                        .show();

                return;
            }

            mProgressDialog = ProgressDialog.show(this, null, "请稍后...");
            //mProgressDialog.setCancelable(true);

            OkHttpUtils
                    .post()
                    .url(Datas.LoginUrl)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addParams("username", SchoolId)
                    .addParams("password", Passwd)
                    .build()
                    .execute(new Callback() {

                        @Override
                        public Object parseNetworkResponse(Response response, int id) throws Exception {
                            try {
                                //登录成功之后返回Token
                                String result = response.body().string();
                                if (result.length() > 10) {
                                    Token = result;
                                }
                                handler.sendEmptyMessage(Success);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            handler.sendEmptyMessage(Error);
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            // Response response1 = (Response) response;


                        }
                    });
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }
}
