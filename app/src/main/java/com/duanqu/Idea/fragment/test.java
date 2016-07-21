package com.duanqu.Idea.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.duanqu.Idea.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class test extends BaseFragment {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displayfragment, null);
        return view;
    }
}
