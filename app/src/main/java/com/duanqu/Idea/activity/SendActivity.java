package com.duanqu.Idea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.HorizontalScrollViewEx;
import com.duanqu.Idea.CustomView.ToSomeOne;
import com.duanqu.Idea.CustomView.UserTag;
import com.duanqu.Idea.R;
import com.duanqu.Idea.fragment.BaseFragment;
import com.duanqu.Idea.fragment.ImagePostFragment;
import com.duanqu.Idea.fragment.TextPostFragment;
import com.duanqu.Idea.fragment.VideoPostFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SendActivity extends AppCompatActivity implements ViewGroup.OnClickListener{
    private String id;
    private Toolbar toolbar;
    private ToSomeOne toSomeOne;
    private FrameLayout frameLayout;
    private static HorizontalScrollViewEx hs;
    private LinearLayout at;
    private GetFriendsRelationship popWindow = null;
    private BaseFragment fragment;
    private Intent intent;

    private static HashMap<String,View> views = new HashMap<>();

    public static int IMAGE_CHANGE = 99;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.sent_layout);
        intent = getIntent();
        id = intent.getStringExtra("id");
        init();
        chooseFragment();
    }

    private void chooseFragment() {
        if(id.equals("image")){
            //将image信息传递进fragment
            fragment = new ImagePostFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();
            ArrayList paths = intent.getStringArrayListExtra("images");
            if(paths==null)
            {
                paths = new ArrayList();
                paths.add(intent.getStringExtra("image"));
            }
            ((ImagePostFragment)fragment).setPath(paths);
        }else if(id.equals("text")){
            fragment = new TextPostFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();
        }else if(id.equals("video")){
            fragment = new VideoPostFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();
            ((VideoPostFragment) fragment).setData(intent.getStringExtra("videoUri"),
                    intent.getStringExtra("thum"));

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RESULT_OK){
            if(requestCode==IMAGE_CHANGE){
                ((ImagePostFragment)fragment).setPath(data.getStringArrayListExtra("data_return"));
            }

        }
    }

    public static void removeViews(String name){
        //及时remove掉view
        hs.removeView(views.get(name));
        views.remove(name);

    }

    public static void removeViews(){
        views.clear();
    }

    public static Set<String> getViews(){
        return  views.keySet();
    }

    public static void setViews(String name,View view) {
        views.put(name,view);
        hs.addView(view);
    }

    private void init() {

        at = (LinearLayout) findViewById(R.id.at);
        hs = (HorizontalScrollViewEx) findViewById(R.id.hs);

        //初始化标题栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("发送");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //加入点击
        hs.setOnClickListener(this);
        at.setOnClickListener(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(popWindow==null){
            popWindow = new GetFriendsRelationship(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(id.equals("image")){


        }else if(id.equals("text")){




        }else if(id.equals("video")){

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.at:{
                System.out.println("hs click");
                if(popWindow==null){
                    popWindow = new GetFriendsRelationship(this);
                }
                popWindow.showAsDropDown(toolbar, (int)Config.WIDTH/3,0);
                //popWindow.showAtLocation(at,Gravity.RIGHT|Gravity.TOP,0,0);
                break;
            }

        }
    }
}
