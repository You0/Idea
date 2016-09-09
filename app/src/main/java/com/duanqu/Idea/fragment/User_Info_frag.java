package com.duanqu.Idea.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqu.Idea.R;

/**
 * Created by Administrator on 2016/8/15.
 */
public class User_Info_frag extends BaseFragment {
    private RecyclerView recyclerView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {




        }
    };


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_vp_last,null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        recyclerView.setAdapter(new MyAdapter());



        return view;
    }

    class MyAdapter extends RecyclerView.Adapter
    {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }


        class ViewHolder{




        }



    }



}
