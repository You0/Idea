package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.duanqu.Idea.Adapter.CotainViewPager;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.config.RequestCode;
import com.duanqu.Idea.fragment.DisplayFragment;
import com.duanqu.Idea.fragment.InnerViewPager;
import com.duanqu.Idea.fragment.SuggestFragment;
import com.duanqu.Idea.fragment.test;
import com.duanqu.Idea.result.RecordResult;
import com.duanqu.Idea.service.ServiceManager;
import com.duanqu.Idea.utils.MyGestureDetector;
import com.duanqu.Idea.utils.ViewHideAnimationUtils;
import com.duanqu.qupai.sdk.android.QupaiService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView tv_msg;
    private TextView tv_tj;
    private ViewPager viewPager;
    private ImageButton menu;
    private ImageView search;
    public static LinearLayout footnagavite;
    private ImageView image;
    private ImageView video;
    private ImageView editor;

    //private PopupWindow popWindow = DisplayFragment.popupWindow;
    private LinearLayout containLL;
    public static DrawerLayout mDrawerLayout;
    public static Activity mActivity;
    private LinkedList<Fragment> fragments = new LinkedList<>();
    private CotainViewPager cotainViewPagerAdapter;
    private boolean Once = true;
    private RelativeLayout layout;
    private Intent intent;
    private boolean once = true;
    private final int SEND_IMAGE = 99;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mActivity = this;
        intent = getIntent();
        initView();
        blindListenerView();
        InitAnimation();
        //开启服务，监听服务端信息
        ServiceManager mServiceManager = new ServiceManager(this);
        mServiceManager.startService();
    }


    private void blindListenerView() {
        menu.setOnClickListener(this);
        search.setOnClickListener(this);
        tv_msg.setOnClickListener(this);
        tv_tj.setOnClickListener(this);


        video.setOnClickListener(this);
        image.setOnClickListener(this);
        editor.setOnClickListener(this);


        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                if(popWindow==null)
//                {
//                    popWindow = DisplayFragment.popupWindow;
//                }
//                popWindow.dismiss();
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tv_msg.setTextColor(getResources().getColor(R.color.biz_audio_progress_first));
                    tv_tj.setTextColor(getResources().getColor(R.color.colorHuise));
                } else {
                    tv_msg.setTextColor(getResources().getColor(R.color.colorHuise));
                    tv_tj.setTextColor(getResources().getColor(R.color.biz_audio_progress_first));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        //popWindow = new MyPopWindow(this, this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getLayoutInflater().inflate(R.layout.tool_bar, toolbar);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_tj = (TextView) findViewById(R.id.tv_tj);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        menu = (ImageButton) findViewById(R.id.menu);
        search = (ImageView) findViewById(R.id.search);

        footnagavite = (LinearLayout) findViewById(R.id.footnagavite);
        image = (ImageView) findViewById(R.id.sendImage);
        video = (ImageView) findViewById(R.id.sendVideo);
        editor = (ImageView) findViewById(R.id.sendText);
        containLL = (LinearLayout) findViewById(R.id.containLL);


        InnerViewPager innerFragment = new InnerViewPager();
        SuggestFragment suggestFragment = new SuggestFragment();
        fragments.add(innerFragment);
        fragments.add(suggestFragment);
        cotainViewPagerAdapter = new CotainViewPager(getSupportFragmentManager());
        cotainViewPagerAdapter.setFragments(fragments);
        viewPager.setAdapter(cotainViewPagerAdapter);//给ViewPager设置适配器


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search: {
                System.out.println("搜索");

                break;
            }

            case R.id.menu: {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            }

            case R.id.tv_tj: {
                System.out.println("tjtjtjtjjtjtjtj");
                viewPager.setCurrentItem(1, true);
                break;
            }
            case R.id.tv_msg: {
                System.out.println("memememmemme");
                viewPager.setCurrentItem(0);
                break;
            }

            case R.id.sendVideo: {
                QupaiService qupaiService = AlibabaSDK
                        .getService(QupaiService.class);
                qupaiService.showRecordPage(this, RequestCode.RECORDE_SHOW, false);
                break;
            }

            case R.id.sendImage: {
                Intent intent = new Intent(MainActivity1.this, ImageWatchActivity.class);
                //1表示多选图片
                intent.putExtra("tag", 1);
                startActivityForResult(intent, SEND_IMAGE);
                break;
            }

            case R.id.sendText: {
                Intent intent = new Intent(this, SendActivity.class);
                intent.putExtra("id", "text");
                startActivity(intent);
                break;
            }
        }
    }

    private void InitAnimation() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String videoFile;
        String thum[];
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.RECORDE_SHOW) {
                RecordResult result = new RecordResult(data);
                //得到视频地址，和缩略图地址的数组，返回十张缩略图
                videoFile = result.getPath();
                thum = result.getThumbnail();
                result.getDuration();

                //Toast.makeText(MainActivity1.this, thum[0], Toast.LENGTH_LONG).show();
                // tv_result.setText("视频路径:" + videoFile + "图片路径:" + thum[0]);
                //打开发送界面
                Intent intent = new Intent(MainActivity1.this, SendActivity.class);
                intent.putExtra("id", "video");
                intent.putExtra("videoUri", videoFile);
                intent.putExtra("thum", thum[0]);
                startActivity(intent);
                //startUpload();//可以在这里调用上传的方法
            }

            if(requestCode == SEND_IMAGE){
                Intent intent = new Intent(MainActivity1.this, SendActivity.class);
                intent.putExtra("id", "image");
                String image;
                ArrayList arrayList = data.getCharSequenceArrayListExtra("data_return");
                if (arrayList==null){
                    image = data.getStringExtra("data_return");
                    intent.putExtra("tag",0);
                    intent.putExtra("image",image);
                }else{
                    intent.putExtra("tag",1);
                    intent.putStringArrayListExtra("images",arrayList);
                }

                startActivity(intent);
            }
        } else {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity1.this, "取消", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //获取宽高
            Config.FootNagaviteWeight = footnagavite.getMeasuredWidth();
            Config.FootNagaviteHeight = footnagavite.getMeasuredHeight();
            //获得屏幕的宽高。
            Config.WIDTH = MyApplication.getScreenMetrics(this).widthPixels;
            Config.HEIGHT = MyApplication.getScreenMetrics(this).heightPixels;

            //&&intent.getBooleanExtra("tj",false)
            if (once) {
//                System.out.println("你TM为什么不弹窗");
//                if ( poupWindow==null){
//                    System.out.println("你TM为什么不弹窗+1");
//                    poupWindow = new AttentionPoupWindow(this);
//                }
//                System.out.println("你TM为什么不弹窗+2");
//                poupWindow.showAtLocation(viewPager,Gravity.NO_GRAVITY,0,0);


                once = false;
            }

        }
    }
}
