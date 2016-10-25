package com.duanqu.Idea.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.fragment.InnerViewPager;

import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/5/18.
 */
public class CotainViewPager<T> extends FragmentPagerAdapter{
    LinkedList<T> fragments;//内部viewpager的layout_ID

    public CotainViewPager(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(LinkedList<Fragment> fragments) {
        this.fragments = (LinkedList<T>) fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return InnerViewPager.mTitleList.get(position);
//    }
}
