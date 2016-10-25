package com.duanqu.Idea.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.duanqu.Idea.test.Datas;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/10/10.
 */
public class FirstStepPoupWindowForRegister extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText userNameText;
    private MaterialEditText passwdText;
    private Button bnRegister;
    private RegisterPoupWindow mregisterPoupWindow;
    private ProgressDialog mProgressDialog;
    private String Token;
    private String username;

    private final int STUDENTCHECKSUCCESS = 0;
    private final int STUDENTCHECKFAULT = 1;
    private final int REGISTERSUCCESS = 2;
    private final int REGISTERFAULT = 3;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
            switch (msg.what) {
                case STUDENTCHECKSUCCESS: {
                    if (mregisterPoupWindow == null) {
                        mregisterPoupWindow = new RegisterPoupWindow(FirstStepPoupWindowForRegister.this,username,Token);
                    }
                    mregisterPoupWindow.showAtLocation(userNameText, Gravity.NO_GRAVITY, 0, 0);
                    break;
                }

                case STUDENTCHECKFAULT: {
                    Toast.makeText(FirstStepPoupWindowForRegister.this, "注册失败，账号密码错误或账号已存在", Toast.LENGTH_SHORT).show();
                    break;
                }

                case REGISTERSUCCESS:{
                    mregisterPoupWindow.StartActivity();
                    break;
                }

                case REGISTERFAULT:{
                    Toast.makeText(FirstStepPoupWindowForRegister.this, "注册失败!", Toast.LENGTH_SHORT).show();
                    break;
                }





            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register_framelayout);

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

            if(SchoolId.equals("")||Passwd.equals("")){
                Snackbar.make(bnRegister, "请输入账号密码", Snackbar.LENGTH_LONG)
                        .show();

                return ;
            }

            mProgressDialog = ProgressDialog.show(this, null, "请稍后...");
            //mProgressDialog.setCancelable(true);

            OkHttpUtils
                    .post()
                    .url(Datas.RegistUrl)
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .addParams("username", SchoolId)
                    .addParams("password", Passwd)
                    .build()
                    .execute(new Callback() {

                        @Override
                        public Object parseNetworkResponse(Response response, int id) throws Exception {
                            try {
                                String result = response.body().string();

                                if (result.length()==32) {
                                    username = SchoolId;
                                    Token = result;
                                    handler.sendEmptyMessage(STUDENTCHECKSUCCESS);
                                } else {
                                    handler.sendEmptyMessage(STUDENTCHECKFAULT);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            handler.sendEmptyMessage(STUDENTCHECKFAULT);
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
