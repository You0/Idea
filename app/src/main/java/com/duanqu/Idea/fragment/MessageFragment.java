package com.duanqu.Idea.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.duanqu.Idea.Adapter.MessageListViewAdapter;
import com.duanqu.Idea.CustomView.SwipeLayout;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.MessageBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MessageFragment extends BaseFragment {
    private View view;
    private ListView listView;
    private MessageListViewAdapter adapter;
    private ArrayList<MessageBean> datas;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_layout,null);
        listView = (ListView) view.findViewById(R.id.message_listview);
        BindData();
        return view;
    }

    private void BindData() {
        //测试数据
        MessageBean messageBean = new MessageBean();
        messageBean.setLastMessage("Hi,最近还好吗？");
        messageBean.setName("何叔平");
        datas = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            datas.add(messageBean);
        }
        adapter = new MessageListViewAdapter(getActivity(),R.layout.message_listview_item,datas);
        listView.setAdapter(adapter);
    }
}
