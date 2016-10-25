package com.duanqu.Idea.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;

import java.io.File;

/**
 * Created by Administrator on 2016/10/9.
 */
public class SplashActivity extends AppCompatActivity {
    private ImageView iv_start;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        sharedPreferences = MyApplication.getContext().getSharedPreferences("userInfo",MODE_PRIVATE);
        initImage();
    }

    private void initImage() {
        File dir = getFilesDir();
        final File imgFile = new File(dir, "start.jpg");
        if (imgFile.exists()) {
            iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        } else {
            iv_start.setImageResource(R.drawable.start);
        }

        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(500);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {


            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //sharedPreferences.getBoolean("ifLogin",false)
                if(sharedPreferences.getBoolean("ifLogin",false))
                {
                    startActivity();
                }else{
                    startLoginActivity();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnim);

    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity1.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }

    private void startLoginActivity()
    {
        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();

    }




}
