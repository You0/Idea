package com.duanqu.Idea.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.duanqu.Idea.CustomView.CircleNumber;

/**
 * Created by Administrator on 2016/10/20.
 */
public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CircleNumber circleNumber = new CircleNumber(this);

        setContentView(new CircleNumber(this));
        circleNumber.setCar("7");
    }
}
