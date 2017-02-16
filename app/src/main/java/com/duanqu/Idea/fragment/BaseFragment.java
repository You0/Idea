package com.duanqu.Idea.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/5/17.
 */
public abstract  class BaseFragment extends Fragment {
    public static Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        Log.e("xxx","onCreateView");
        return initView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("xxx","onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("xxx","onDestroy");
    }


    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
