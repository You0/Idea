package com.duanqu.Idea.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.duanqu.Idea.Adapter.AttentionAdapter;
import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.AttentionGridViewHolder;
import com.duanqu.Idea.bean.AttentionBean;
import com.duanqu.Idea.CustomView.mGriView;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/15.
 */
public class AttentionFragment extends BaseFragment {
    private View view;
    private mGriView attention_gridview;
    private TextView loading;
    private ArrayList<AttentionBean> datas;
    private AttentionAdapter mAdapter;
    private CheckedChangeListener listener;




    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.atten_grid,null);
        attention_gridview = (mGriView) view.findViewById(R.id.attention_gridview);
        loading = (TextView) view.findViewById(R.id.loading);


        if(datas!=null && datas.size()!=0)
        {
            loading.setVisibility(View.GONE);
            attention_gridview.setVisibility(View.VISIBLE);
            Log.e("xxx","datas"+datas.size());
            mAdapter = new AttentionAdapter(getActivity(),
                    new BaseAdapter.Builder<AttentionBean>(datas).addType(0, AttentionGridViewHolder.class).build());
            attention_gridview.setAdapter(mAdapter);

            attention_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("frag","click");
                    CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkbox);
                    boolean isChecked = checkBox.isChecked();
                    checkBox.setChecked(!isChecked);
                    datas.get(position).setChecked(!isChecked);
                    listener.CheckedChange(datas.get(position).getName(),!isChecked);
                }
            });

            mAdapter.notifyDataSetChanged();
        }
        return view;
    }

    public interface CheckedChangeListener
    {
        void CheckedChange(String name,boolean isChecked);
    }


    public void setListener(CheckedChangeListener listener) {
        this.listener = listener;
    }

    public ArrayList<AttentionBean> getDatas() {
        return datas;
    }

    public void upDateUI(final ArrayList ds)
    {
        datas =ds;
    }



}
