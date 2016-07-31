package com.duanqu.Idea.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.OnlineVideoListItem;
import com.duanqu.Idea.Adapter.VideoListItem;
import com.duanqu.Idea.Adapter.VideoWatchAdapter;
import com.duanqu.Idea.CustomView.VideoWatchFragment;
import com.duanqu.Idea.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoItem;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.items.ListItem;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class VideoWatchActivity  extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private SimpleDraweeView messageHead;
    private TextView username;
    private TextView sign;
    private TextView time;
    private LinearLayout fwd;
    private LinearLayout cmt;
    private LinearLayout prz;
    private HashMap userInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        messageHead = (SimpleDraweeView)findViewById(R.id.messageHead);
        username = (TextView)findViewById(R.id.username);
        sign = (TextView)findViewById(R.id.sign);
        time = (TextView)findViewById(R.id.time);
        fwd = (LinearLayout)findViewById(R.id.fwd);
        cmt = (LinearLayout)findViewById(R.id.cmt);
        prz = (LinearLayout)findViewById(R.id.prz);


//        String url = data.getUserInfo().get("headurl");
//        Uri uri = Uri.parse(url);
//        messageHead.setImageURI(uri);
//        userInfo = data.getUserInfo();
//        username.setText((String)userInfo.get("nickname"));
//        sign.setText((String)userInfo.get("sign"));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("查看视频");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fl_container, new VideoWatchFragment())
                .commit();






    }



    @Override
    public void onClick(View v) {

    }



}
