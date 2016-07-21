package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.duanqu.Idea.CustomView.MyScaleSimpleDraweeView;
import com.duanqu.Idea.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/15.
 */
public class ImageDisplay extends Activity {
    private ViewPager vp;
    private Intent intent;
    private ArrayList images;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_display);
        intent = getIntent();
        images = intent.getCharSequenceArrayListExtra("images");
        initView();
        bindData();

    }

    


    private void bindData() {
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                MyScaleSimpleDraweeView myScaleView = new MyScaleSimpleDraweeView(getApplicationContext());
                myScaleView.setImageURI(Uri.parse((String) images.get(position)));
                container.addView(myScaleView);
                return myScaleView;
            }
        });

    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.image_disPager);
    }




}
