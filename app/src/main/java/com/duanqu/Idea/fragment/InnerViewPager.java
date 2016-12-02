package com.duanqu.Idea.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqu.Idea.Adapter.CotainViewPager;
import com.duanqu.Idea.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class InnerViewPager extends BaseFragment{
    public static List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private LinkedList<Fragment> fragments = new LinkedList<>();
    private CotainViewPager cotainViewPagerAdapter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.innerviewpager,null);

        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.vp_view);

        //顶部标题栏设置
        mTitleList.add("动态");
        mTitleList.add("好友");
        mTitleList.add("消息");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i=0;i<mTitleList.size();i++){

            if(i==1){
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)).setCustomView(R.layout.tablayoutmessage));
            }else{
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));//添加tab选项卡
            }
        }


        DisplayFragment displayFragment = new DisplayFragment();
        MessageFragment messageFragment = new MessageFragment();
        test test1 = new test();
        test tst = new test();
        fragments.add(displayFragment);
        fragments.add(tst);
        fragments.add(messageFragment);
        cotainViewPagerAdapter = new CotainViewPager(getActivity().getSupportFragmentManager());
        cotainViewPagerAdapter.setFragments(fragments);
        //viewPager.setAdapter(cotainViewPagerAdapter);
        viewPager.setAdapter(cotainViewPagerAdapter);//给ViewPager设置适配器


        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);
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
                mTabLayout.setScrollPosition(position,positionOffset,true);
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
        //mTabLayout.setTabsFromPagerAdapter(cotainViewPagerAdapter);//给Tabs设置适配器
        return view;
    }
}
