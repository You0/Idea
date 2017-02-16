package com.duanqu.Idea.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.JsonParse.MainMessageParse;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE1;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE2;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE3;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE4;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE5;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE6;
import com.duanqu.Idea.activity.FeedActivity;
import com.duanqu.Idea.activity.MainActivity1;
import com.duanqu.Idea.activity.UserFeedsDisplayAty;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.test.Datas;
import com.duanqu.Idea.test.TestAdapter;
import com.duanqu.Idea.utils.MyGestureDetector;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/2.
 */
public class OnlyOneUserDisplayFragment extends BaseFragment {
    private ListView listView;
    private MainMessageBean mainMessageBeen;
    private ArrayList<MainMessageBean> arrayList = new ArrayList<>();
    private boolean Visibility = true;
    SwipeRefreshLayout sr;
    //?userId=111&pageSize=10&pageNum=1
    private String userId;
    private final int  pageSize  =10;
    private int pageNum = 1;
    private int PULL = 0;
    private int LOAD = 1;
    private String uid;
    private boolean loading = false;


    private TestAdapter testAdapter;
    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:{
                    testAdapter.setDatas(arrayList);
                    testAdapter.notifyDataSetChanged();
                    break;
                }

                case 1:{
                    testAdapter.setDatas(arrayList);
                    testAdapter.notifyDataSetChanged();
                    loading = false;
                    break;
                }

                case 9:{
                    //4是单个视频
                     testAdapter = new TestAdapter((Activity) context,
                            new BaseAdapter.Builder()
                                    .addType(4, Item_content_TYPE4.class)
                                    .addType(0, Item_content_TYPE1.class)
                                    .addType(1, Item_content_TYPE2.class)
                                    .addType(2, Item_content_TYPE3.class)
                                    .addType(3, Item_content_TYPE5.class)
                                    .addType(5, Item_content_TYPE6.class)
                                    .setDatas(arrayList).build());
                    listView.setAdapter(testAdapter);
                    break;
                }

            }

            super.handleMessage(msg);
        }
    };
    private MainMessageParse mainMessageParse = new MainMessageParse();

    //public static PopupWindow popupWindow;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.displayfragment, null);
        listView = (ListView) view.findViewById(R.id.displayListview);
        sr = (SwipeRefreshLayout) view.findViewById(R.id.sr);
        initSwipeRefreshLayout();
        //popupWindow = new MyPopWindow(getActivity(), getActivity());

        //这里
        //RefreshListView refreshListView = (RefreshListView)listView;
        //refreshListView.setOnRefreshListener(listener);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        //设置动画
        listView.setOnTouchListener(new MyGestureDetector(getActivity()) {
            @Override
            public void onScrollDown() {
                if (Visibility == false) {
                    Animation DisplayAnimation = new TranslateAnimation(0, 0, Config.FootNagaviteHeight, 0);
                    DisplayAnimation.setDuration(300);
                    DisplayAnimation.setFillAfter(true);
                    MainActivity1.footnagavite.startAnimation(DisplayAnimation);
                    Visibility = true;
                }
            }
            @Override
            public void onScrollUp() {
                if (Visibility == true) {
                    Animation MissAnimation = new TranslateAnimation(0, 0, 0, Config.FootNagaviteHeight);
                    MissAnimation.setDuration(300);
                    MissAnimation.setFillAfter(true);
                    MainActivity1.footnagavite.startAnimation(MissAnimation);
                    Visibility = false;
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(listView != null && listView.getChildCount() > 0){
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    sr.setEnabled(enable);

                    Log.e("nowItem","nums"+(firstVisibleItem+visibleItemCount) + "totalItem"+totalItemCount +"arraysize" + arrayList.size());
                    if(firstVisibleItem + visibleItemCount == totalItemCount)
                    {

                    }

                    if(firstVisibleItem + visibleItemCount == arrayList.size()){
                        LoadMore();
                    }

                }


            }
        });

        initData();
        return view;
    }


    private void initData() {
        mainMessageParse = new MainMessageParse();
        Refresh(mainMessageParse);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("DisplayFragment","Click Item:"+position);
                MainMessageBean mainMessageBean = arrayList.get(position);
                Intent intent = new Intent(getActivity(), FeedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("first",mainMessageBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void Refresh(final MainMessageParse mainMessageParse) {
        OkHttpUtils.get().url(Datas.UserFeed).addParams("uId", UserFeedsDisplayAty.uid)
                .addParams("pageNum","1").addParams("pageSize","10").addParams("token","123")
        .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                JSONArray array = new JSONArray(json);
                arrayList.clear();
                for(int i=0;i<array.length();i++)
                {
                    mainMessageBeen = mainMessageParse.Parse(array.get(i).toString());
                    arrayList.add(mainMessageBeen);
                }
                if(testAdapter!=null){
                    handler.sendEmptyMessage(PULL);
                }else{
                    handler.sendEmptyMessage(9);
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }

    private void initSwipeRefreshLayout() {
        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh(mainMessageParse);
                handler.sendEmptyMessage(PULL);
                sr.setRefreshing(false);
            }
        });
    }

//
    private void LoadMore(){
        if(loading){
            return;
        }
        loading = true;
        OkHttpUtils.get().url(Datas.UserFeed)
                .addParams("uId", UserFeedsDisplayAty.uid)
                .addParams("pageNum",String.valueOf(++pageNum))
                .addParams("pageSize","10")
                .addParams("token","123")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                JSONArray array = new JSONArray(json);
                for(int i=0;i<array.length();i++)
                {
                    mainMessageBeen = mainMessageParse.Parse(array.get(i).toString());
                    arrayList.add(mainMessageBeen);
                }
                handler.sendEmptyMessage(LOAD);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }




}



