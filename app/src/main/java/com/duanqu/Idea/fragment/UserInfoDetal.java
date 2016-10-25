package com.duanqu.Idea.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.MainActivity1;
import com.duanqu.Idea.activity.ParallaxUserInfoDisplayActivity;
import com.duanqu.Idea.test.Datas;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/17.
 */
public class UserInfoDetal extends BaseFragment implements View.OnClickListener{
    private View view;
    private MaterialEditText nickname;
    private ProgressDialog mProgressDialog;
    private CheckBox boy;
    private CheckBox girl;
    private MaterialEditText sign;
    private MaterialEditText grades;
    private MaterialEditText major;
    private MaterialEditText school;
    private MaterialEditText email;
    private HashMap<String,MaterialEditText> edit_views;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private String sex = null;
    private boolean EmailValidate = false;
    private Dialog dialog;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_info_edit, null);
        edit_views = new HashMap<>();
        mSharedPreferences = getActivity().getSharedPreferences("userInfo",getActivity().MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        nickname = (MaterialEditText) view.findViewById(R.id.nickname);
        sign = (MaterialEditText) view.findViewById(R.id.sign);
        grades = (MaterialEditText) view.findViewById(R.id.grades);
        major = (MaterialEditText) view.findViewById(R.id.major);
        school = (MaterialEditText) view.findViewById(R.id.school);
        email = (MaterialEditText) view.findViewById(R.id.email);
        boy = (CheckBox) view.findViewById(R.id.boy);
        girl = (CheckBox) view.findViewById(R.id.girl);

        edit_views.put("nickname",nickname);
        edit_views.put("sign",sign);
        edit_views.put("grades",grades);
        edit_views.put("major",major);
        edit_views.put("school",school);
        edit_views.put("e_mail",email);

        grades.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    dialog = new Dialog(getActivity());
                    ListView listView = new ListView(getActivity());
                    final String[] strings = new String[]{"一年级","二年级","三年级","四年级","毕业啦"};
                    listView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1
                            ,strings));
                    dialog.setContentView(listView);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            grades.setText(strings[position]);
                            dialog.cancel();
                        }
                    });
                    dialog.setTitle("年级");
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return true;
                }
                return  false;
            }
        });


//        grades.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = email.getText().toString();
                if(s.length()!=0){
                    if(!value.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                    {
                        EmailValidate = true;
                        email.setError("邮箱格式不正确");
                    }else {
                        EmailValidate = false;
                    }
                }else{
                    EmailValidate = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setHasOptionsMenu(true);


        SetLocalInfo();
        boy.setOnClickListener(this);
        girl.setOnClickListener(this);

        return view;
    }

    private void SetLocalInfo() {
        nickname.setText(Config.nickname);
        sign.setText(Config.sign);
        grades.setText(Config.grades);
        major.setText(Config.major);
        school.setText(Config.school);
        email.setText(Config.email);
        if(Config.sex.equals("1")){
            girl.setChecked(true);
        }else{
            boy.setChecked(true);
        }


    }






    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.send, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.reply)
        {
            if(CheckData()||EmailValidate){
                ((ParallaxUserInfoDisplayActivity)getActivity())
                        .handler.sendEmptyMessage(2);

                return false;
            }


            mProgressDialog = ProgressDialog.show(getActivity(),null,"请稍后");
            PostFormBuilder  builder=  OkHttpUtils.post();

            builder.addHeader("Content-Type","application/x-www-form-urlencoded");


            if(boy.isChecked()){
                sex = "0";
            }else{
                sex = "1";
            }

            for(Map.Entry<String,MaterialEditText> entry : edit_views.entrySet())
            {
                String vaule = null;
                try {
                    vaule = entry.getValue().getText().toString();
                    editor.putString(entry.getKey(),vaule);
                    vaule = URLEncoder.encode(URLEncoder.encode(vaule, "UTF-8"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                builder.addParams(entry.getKey(),vaule);

            }
            editor.putString("email",email.getText().toString());
            builder.addParams("sex",sex);
            editor.putString("sex",sex);
            builder.addParams("Token",Config.Token);
            builder.addParams("username",Config.username);

            builder.url(Datas.detail).build().execute(new Callback() {
                @Override
                public Object parseNetworkResponse(Response response, int id) throws Exception {
                    mProgressDialog.cancel();
                    Log.e("xxx123","Response");
                    if(response.body().string().equals("success")){
                        Log.e("xxx123","success");
                        editor.commit();
                        Config.email = email.getText().toString();
                        Config.nickname = nickname.getText().toString();
                        Config.sign = sign.getText().toString();
                        Config.sex = sex;
                        Config.grades = grades.getText().toString();
                        Config.school = school.getText().toString();
                        Config.major = major.getText().toString();

                        ((ParallaxUserInfoDisplayActivity)getActivity())
                            .handler.sendEmptyMessage(0);

                        MainActivity1.mDrawerLayout.getChildAt(1).invalidate();

                    }
                    return null;
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    mProgressDialog.cancel();
                }

                @Override
                public void onResponse(Object response, int id) {
                    mProgressDialog.cancel();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean CheckData() {
        for(Map.Entry<String,MaterialEditText> entry : edit_views.entrySet())
        {
            if(entry.getKey().equals("sign")){
                if(entry.getValue().getText().length()>50){
                    return true;
                }
            }else if (entry.getKey().equals("nickname")){
                if(entry.getValue().getText().length()==0||
                        entry.getValue().getText().length()>10){
                    return true;
                }
            }else if(entry.getKey().equals("e_mail")){
                if(entry.getValue().getText().length()>30){
                    return true;
                }
            }else{
                if(entry.getValue().getText().length()>10){
                    return true;
                }
            }
        }
        return  false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.boy:{
                boolean selected = boy.isSelected();
                boy.setChecked(!selected);
                girl.setChecked(selected);
                break;
            }

            case R.id.girl:{
                boolean selected = girl.isSelected();
                girl.setChecked(!selected);
                boy.setChecked(selected);
                break;
            }
        }
    }
}
