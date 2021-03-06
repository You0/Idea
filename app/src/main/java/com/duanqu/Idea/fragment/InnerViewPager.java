package com.duanqu.Idea.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.CotainViewPager;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.FriendBean;
import com.duanqu.Idea.bean.FriendsListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class InnerViewPager extends BaseFragment {
    public static List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private LinkedList<Fragment> fragments = new LinkedList<>();
    private CotainViewPager cotainViewPagerAdapter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.innerviewpager, null);

        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.vp_view);
        viewPager.setOffscreenPageLimit(2);
        int icons[] = {R.drawable.select_one,R.drawable.select_two,R.drawable.select_three
        };
        //顶部标题栏设置
//        mTitleList.add("动态");
//        mTitleList.add("消息");
//        mTitleList.add("好友");
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        for (int i = 0; i < mTitleList.size(); i++) {
//            if (i == 1) {
//                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)).setCustomView(R.layout.tablayoutmessage));
//            } else {
//                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));//添加tab选项卡
//            }
//        }

        if(mTabLayout.getTabCount()<1){
            for(int i=0;i<3;i++){
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView(icons[i])));
            }
        }



        DisplayFragment displayFragment = new DisplayFragment();
        MessageFragment messageFragment = new MessageFragment();
        FriendsFragment friendsFragment = new FriendsFragment();
        fragments.add(displayFragment);
        fragments.add(messageFragment);
        fragments.add(friendsFragment);
        cotainViewPagerAdapter = new CotainViewPager(getActivity().getSupportFragmentManager());
        cotainViewPagerAdapter.setFragments(fragments);
        //viewPager.setAdapter(cotainViewPagerAdapter);
        viewPager.setAdapter(cotainViewPagerAdapter);//给ViewPager设置适配器


        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTabAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //mTabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        //viewPager.setCurrentItem(1);
        //viewPager.setCurrentItem(0);//直接设置0的话竟然不起作用,好吧.就这样迂回一下吧

    //mTabLayout.setTabsFromPagerAdapter(cotainViewPagerAdapter);//给Tabs设置适配器
        return view;
    }

    private View getTabView(int drawable){
        //首先为子tab布置一个布局
        View v = LayoutInflater.from(context).inflate(R.layout.tab_layout,null);
//        TextView tv = (TextView) v.findViewById(R.id.text);
//        tv.setText(text);
        ImageView iv = (ImageView) v.findViewById(R.id.icon);
        iv.setImageResource(drawable);
        return v;
    }
}
