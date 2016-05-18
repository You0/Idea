package com.duanqu.Idea.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqu.Idea.R;

/**
 * Created by Administrator on 2016/5/17.
 */
public class Menu extends BaseFragment  {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tool_bar,null);
        return view;
    }
}
