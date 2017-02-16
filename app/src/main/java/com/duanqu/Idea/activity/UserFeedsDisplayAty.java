package com.duanqu.Idea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.duanqu.Idea.R;
import com.duanqu.Idea.fragment.DisplayFragment;
import com.duanqu.Idea.fragment.OnlyOneUserDisplayFragment;
import com.duanqu.Idea.service.ServiceManager;

/**
 * Created by Me on 2017/1/25.
 */

public class UserFeedsDisplayAty extends AppCompatActivity {
    private Toolbar toolbar;
    private FrameLayout fragment;
    public static String uid;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_feed_display_aty_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragment = (FrameLayout) findViewById(R.id.fl_content);

        toolbar.setTitle("发言");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getSupportFragmentManager().beginTransaction().
                replace(R.id.fl_content, new OnlyOneUserDisplayFragment()).
                commit();
    }


}
