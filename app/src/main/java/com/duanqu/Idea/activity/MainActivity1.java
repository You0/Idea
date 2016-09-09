package com.duanqu.Idea.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
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

import com.alibaba.sdk.android.AlibabaSDK;
import com.duanqu.Idea.Adapter.CotainViewPager;
import com.duanqu.Idea.R;
import com.duanqu.Idea.config.RequestCode;
import com.duanqu.Idea.fragment.DisplayFragment;
import com.duanqu.Idea.fragment.InnerViewPager;
import com.duanqu.Idea.fragment.SuggestFragment;
import com.duanqu.Idea.fragment.test;
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
    private LinearLayout footnagavite;
    private PopupWindow popWindow = DisplayFragment.popupWindow;
    private LinearLayout containLL;
    private DrawerLayout mDrawerLayout;
    private TranslateAnimation translateAnimationUP;
    private TranslateAnimation translateAnimationDOWN;
    public static Activity mActivity;
    private LinkedList<Fragment> fragments = new LinkedList<>();
    private CotainViewPager cotainViewPagerAdapter;
    private boolean Once = true;
    private RelativeLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mActivity = this;
        initView();
        blindListenerView();
        InitAnimation();

    }

    private void blindListenerView() {
        menu.setOnClickListener(this);
        search.setOnClickListener(this);
        tv_msg.setOnClickListener(this);
        tv_tj.setOnClickListener(this);





        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if(popWindow==null)
                {
                    popWindow = DisplayFragment.popupWindow;
                }
                popWindow.dismiss();
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
        containLL = (LinearLayout) findViewById(R.id.containLL);


        InnerViewPager innerFragment = new InnerViewPager();
        SuggestFragment suggestFragment = new SuggestFragment();
        fragments.add(innerFragment);
        fragments.add(suggestFragment);
        cotainViewPagerAdapter = new CotainViewPager(getSupportFragmentManager());
        cotainViewPagerAdapter.setFragments(fragments);
        viewPager.setAdapter(cotainViewPagerAdapter);
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


        }
    }

    private void InitAnimation() {
    }



}
