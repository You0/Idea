package com.duanqu.Idea.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.RefreshListView;
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
import com.duanqu.Idea.activity.MyPopWindow;
import com.duanqu.Idea.bean.ChatBean;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.test.Datas;
import com.duanqu.Idea.test.TestAdapter;
import com.duanqu.Idea.test.TestViewHolder;
import com.duanqu.Idea.utils.MyGestureDetector;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/2.
 */
public class DisplayFragment extends BaseFragment {
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
    private boolean loading = false;

    private TestAdapter testAdapter;
    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:{
                    if(testAdapter == null){
                        testAdapter = getTestAdapter();
                    }

                    testAdapter.setDatas(arrayList);
                    testAdapter.notifyDataSetChanged();
                    break;
                }

                case 1:{
                    if(testAdapter == null){
                        testAdapter = getTestAdapter();
                    }
                    testAdapter.setDatas(arrayList);
                    testAdapter.notifyDataSetChanged();
                    loading = false;
                    break;
                }

                case 9:{
                    //4是单个视频
                     testAdapter = getTestAdapter();
                    listView.setAdapter(testAdapter);
                    break;
                }

            }

            super.handleMessage(msg);
        }
    };

    @NonNull
    private TestAdapter getTestAdapter() {
        return new TestAdapter((Activity) context,
               new BaseAdapter.Builder()
                       .addType(4, Item_content_TYPE4.class)
                       .addType(0, Item_content_TYPE1.class)
                       .addType(1, Item_content_TYPE2.class)
                       .addType(2, Item_content_TYPE3.class)
                       .addType(3, Item_content_TYPE5.class)
                       .addType(5, Item_content_TYPE6.class)
                       .setDatas(arrayList).build());
    }

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

//        //5张图
//        String json = "{\"userInfo\":{\"nickname\":\"五颜六色\",\"headurl\":\"http://115.159.159.65:8080/EAsy/Headurl/3ebcfd6f-f456-429a-bf64-ca04f508.jpg\",\"sign\":\"天地宽广，向往之。\",\"id\":50},\"MessageInfo\":{\"id\":1,\"allike\":1,\"time\":\"2016/6/30 19:27\",\"resent\":0,\"comment\":1,\"like\":10,\"see\":1000},\"content\":\"然而我也不知道测试什么东西，发几张图和视频测试下\",\"imgs\":[\"http://115.159.159.65:8080/EAsy/ImageSrc/0fc7fa54-705c-4e38-a0a6-d0c06aca.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/8a0be53c-5d1d-40d8-bc07-d8cf906f.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/a60390ad-0ff7-44da-992d-e1a4f20a.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/abfbbeb5-cfdb-4971-bedc-6afe5d2c.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/b5fd8b15-8771-45ac-b80c-9942bba6.jpg\"],\"video\":\"\",\"resendInfo\":{\"video\":\"\",\"nick\":\"\",\"uid\":\"\",\"imgs\":[]}}";
//        mainMessageBeen = mainMessageParse.Parse(json);
//        arrayList.add(mainMessageBeen);
//
//        //视频
//        json = "{\"userInfo\":{\"nickname\":\"动力小车@何叔平\",\"headurl\":\"http://115.159.159.65:8080/EAsy/Headurl/f6aa7245-5d3c-4887-8e78-8482fb28.jpg\",\"sign\":\"天地宽广，向往之。\",\"id\":1},\"MessageInfo\":{\"id\":1,\"allike\":1,\"time\":\"2016/6/30 19:27\",\"resent\":0,\"comment\":1,\"like\":10,\"see\":1000},\"content\":\"这是一段测试用的文字。。。\",\"imgs\":[],\"video\":\"http://115.159.159.65:8080/EAsy/test.mp4\",\"resendInfo\":{\"video\":\"\",\"nick\":\"\",\"uid\":\"\",\"imgs\":[]}}";
//        mainMessageBeen = mainMessageParse.Parse(json);
//        arrayList.add(mainMessageBeen);
//
//
////        //4张
//        json = "{\"userInfo\":{\"nickname\":\"五颜六色\",\"headurl\":\"http://115.159.159.65:8080/EAsy/Headurl/3ebcfd6f-f456-429a-bf64-ca04f508.jpg\",\"sign\":\"天地宽广，向往之。\",\"id\":1},\"MessageInfo\":{\"id\":1,\"allike\":1,\"time\":\"2016/6/30 19:27\",\"resent\":0,\"comment\":1,\"like\":10,\"see\":1000},\"content\":\"这是一段测试用的文字。。。\",\"imgs\":[\"http://115.159.159.65:8080/EAsy/ImageSrc/b5fd8b15-8771-45ac-b80c-9942bba6.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/b5fd8b15-8771-45ac-b80c-9942bba6.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/b5fd8b15-8771-45ac-b80c-9942bba6.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/b5fd8b15-8771-45ac-b80c-9942bba6.jpg\"],\"video\":\"\",\"resendInfo\":{\"video\":\"\",\"nick\":\"\",\"uid\":\"\",\"imgs\":[]}}";
//        mainMessageBeen = mainMessageParse.Parse(json);
//        arrayList.add(mainMessageBeen);
//
//
////        //单张图片
//        json = "{\"userInfo\":{\"nickname\":\"五颜六色\",\"headurl\":\"http://115.159.159.65:8080/EAsy/Headurl/3ebcfd6f-f456-429a-bf64-ca04f508.jpg\",\"sign\":\"天地宽广，向往之。\",\"id\":1},\"MessageInfo\":{\"id\":1,\"allike\":1,\"time\":\"2016/6/30 19:27\",\"resent\":0,\"comment\":1,\"like\":10,\"see\":1000},\"content\":\"这是一段测试用的文字。。。\",\"imgs\":[\"http://115.159.159.65:8080/EAsy/ImageSrc/b5fd8b15-8771-45ac-b80c-9942bba6.jpg\"],\"video\":\"\",\"resendInfo\":{\"video\":\"\",\"nick\":\"\",\"uid\":\"\",\"imgs\":[]}}";
//        mainMessageBeen = mainMessageParse.Parse(json);
//        arrayList.add(mainMessageBeen);
//        //2张
//        json = "{\"userInfo\":{\"nickname\":\"五颜六色\",\"headurl\":\"http://115.159.159.65:8080/EAsy/Headurl/3ebcfd6f-f456-429a-bf64-ca04f508.jpg\",\"sign\":\"天地宽广，向往之。\",\"id\":1},\"MessageInfo\":{\"id\":1,\"allike\":1,\"time\":\"2016/6/30 19:27\",\"resent\":0,\"comment\":1,\"like\":10,\"see\":1000},\"content\":\"然而我也不知道测试什么东西，发几张图和视频测试下\",\"imgs\":[\"http://115.159.159.65:8080/EAsy/ImageSrc/9e00f9d2-90fc-4011-9ec6-78fdbbb5.jpg\",\"http://115.159.159.65:8080/EAsy/ImageSrc/fdac0db5-a6d1-46e2-8522-e83ad0ad.jpg\"],\"video\":\"\",\"resendInfo\":{\"video\":\"\",\"nick\":\"\",\"uid\":\"\",\"imgs\":[]}}";
//        mainMessageBeen = mainMessageParse.Parse(json);
//        arrayList.add(mainMessageBeen);





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
        OkHttpUtils.get().url(Datas.GetFriendFeeds).addParams("userId", Config.userid)
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


    private void LoadMore(){
        if(loading){
            return;
        }
        loading = true;
        OkHttpUtils.get().url(Datas.GetFriendFeeds)
                .addParams("userId", Config.userid)
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



