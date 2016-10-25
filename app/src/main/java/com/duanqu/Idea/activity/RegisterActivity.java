package com.duanqu.Idea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.Idea.anim.DepthPageTransformer;

/**
 * Created by Administrator on 2016/10/8.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager vpDisplay;
    private Button registe;
    private Button login;
    private int[] images = new int[]{
      R.drawable.image_1,R.drawable.iamge_2
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register_aty);
        initView();

    }

    private void initView() {
        vpDisplay = (ViewPager) findViewById(R.id.vp_display);
        registe = (Button) findViewById(R.id.regiter);
        login = (Button) findViewById(R.id.login);
        //fl_contnet = (FrameLayout) findViewById(R.id.fl_content);

        vpDisplay.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView image = new ImageView(RegisterActivity.this);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageResource(images[position]);
                container.addView(image);
                return image;
            }

            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });

        vpDisplay.setPageTransformer(true,new DepthPageTransformer());

        registe.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.regiter:{

                Intent intent = new Intent(RegisterActivity.this,FirstStepPoupWindowForRegister.class);
                startActivity(intent);
                overridePendingTransition(R.anim.scale_anim,0);
                break;
            }

            case R.id.login:{
                break;
            }
        }
    }
}
