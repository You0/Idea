package com.duanqu.Idea.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;

import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/5/18.
 */
public class CotainViewPager extends PagerAdapter{
    LinkedList<Integer> layouts;//内部viewpager的layout_ID


    public CotainViewPager(LinkedList linkedList)
    {
        layouts = linkedList;
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    /**
     * true: 表示不去创建，使用缓存  false:去重新创建
     * view： 当前滑动的view
     * object：将要进入的新创建的view，由instantiateItem方法创建
     */
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override

    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(MyApplication.getContext()).inflate(layouts.get(position),null);
        //View view = View.inflate(MyApplication.getContext(),layouts.get(position),null);
        //ViewPager innerViwpager = (ViewPager) view.findViewById(R.id.innerViewpager);
        container.addView(view);
        //将在这里面处理内部的viewPager

        return view;
    }


}
