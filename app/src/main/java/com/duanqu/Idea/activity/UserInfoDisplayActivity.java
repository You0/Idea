package com.duanqu.Idea.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.duanqu.Idea.R;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fasterxml.jackson.databind.type.HierarchicType;

import java.util.List;

/**
 * Created by Administrator on 2016/8/11.
 */
public class UserInfoDisplayActivity extends AppCompatActivity {
    private AppBarLayout mAppBarLayout;
    private RecyclerView recyclerView;
    private TabLayout tabs;
    private LinearLayout linearLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private SimpleDraweeView userhead;
    private TextView username;
    private SimpleDraweeView iv_bg;
    private ViewPager user_vp;
    private List<Fragment> fragments;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_info_display_aty);
        initView();
        initData();


    }

    private void initData() {
    }

    private void initView() {
        //init toorbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //init coollapsingToolbarLayout
        mCollapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        //设置标题
        mCollapsingToolbarLayout.setTitle(" ");
        mCollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.titlecolor));
        mCollapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.titlecolor));


        //其他view
        userhead = (SimpleDraweeView) findViewById(R.id.userhead);
        username = (TextView) findViewById(R.id.username);
        iv_bg = (SimpleDraweeView) findViewById(R.id.bg_iv);
        //recyclerView = (RecyclerView) findViewById(R.id.recycler);
        user_vp = (ViewPager) findViewById(R.id.user_vp);
        tabs = (TabLayout) findViewById(R.id.tabs);

        setTitleBar();
        setTableLayout();
        initFramgs();

        user_vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return null;
            }

            @Override
            public int getCount() {
                return 0;
            }
        });



    }

    private void initFramgs() {



    }

    //这里应该传入一个bean。但是现在是本地测试所以不加入
    private void setTitleBar()
    {
        username.setText(Datas.username);
        //设置头像为圆形
        userhead.setImageURI(Uri.parse(Datas.userHead));
        GenericDraweeHierarchy hier = userhead.getHierarchy();
        hier.setRoundingParams(RoundingParams.asCircle());
        iv_bg.setImageURI(Uri.parse(Datas.ueser_bg));


    }

    private void setTableLayout()
    {
        tabs.addTab(tabs.newTab().setText("最近动态"));
        tabs.addTab(tabs.newTab().setText("个人信息"));
        tabs.setupWithViewPager(user_vp);

    }


}
