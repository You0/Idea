package com.duanqu.Idea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.HistoryAndCacheAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.JsonParse.MainMessageParse;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.HistoryAndCacheBean;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.test.Datas;
import com.google.common.primitives.Booleans;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Me on 2017/1/23.
 */

public class HistoryAndCacheCommentActitity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listview;
    private TextView tip;
    private HistoryAndCacheAdapter adapter;
    private String uid;
    private ArrayList beans = new ArrayList();
    private Boolean loading =false;


    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    tip.setVisibility(View.GONE);
                    if (adapter == null) {
                        adapter = new HistoryAndCacheAdapter(HistoryAndCacheCommentActitity.this, R.layout.history_cache_item_layout, beans);
                    }
                    listview.setAdapter(adapter);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            HistoryAndCacheBean bean = (HistoryAndCacheBean) beans.get(i);
                            String id = bean.getFeedId();
                            UseFeedIdGetMainMessageBean(id);
                        }
                    });

                    break;
                }

                case 1: {
                    Intent intent = new Intent(HistoryAndCacheCommentActitity.this, FeedActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("first", mainMessageBeen);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                }


                case 10: {
                    loading = false;
                    adapter.notifyDataSetChanged();
                    break;
                }

            }
        }
    };
    private MainMessageBean mainMessageBeen;

    private void UseFeedIdGetMainMessageBean(String id) {
        OkHttpUtils.get().url(Datas.GetInfoUseFeedId)
                .addParams("userId", Config.userid)
                .addParams("feedId", id)
                .addParams("token", "123")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                MainMessageParse mainMessageParse = new MainMessageParse();
                mainMessageBeen = mainMessageParse.Parse(response.body().string());
                h.sendEmptyMessage(1);
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

    private String type;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_and_cache_comment_layout);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        uid = intent.getStringExtra("uid");
        initView();
        initData();
    }

    private void initData() {
        String url = null;
        if (type.equals("cache")) {
            url = Datas.userCache;
        } else {
            url = Datas.userHistory;
        }

        OkHttpUtils.post().url(url)
                .addParams("Token", Config.Token)
                .addParams("username", Config.username)
                .addParams("page", String.valueOf(page))
                .addParams("pageSize", "10")
                .addParams("uid", uid).build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String json = response.body().string();
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //加入数据
                            Gson gson = new Gson();
                            HistoryAndCacheBean bean = gson.fromJson(jsonArray.get(i).toString(), HistoryAndCacheBean.class);
                            beans.add(bean);
                        }
                        h.sendEmptyMessage(0);
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

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listview = (ListView) findViewById(R.id.listview);
        tip = (TextView) findViewById(R.id.tip);
        if (type.equals("cache")) {
            toolbar.setTitle("我的收藏");
        } else {
            toolbar.setTitle("浏览历史");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listview != null && listview.getChildCount() > 0) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        LoadMore();
                    }

                }
            }
        });


    }

    private void LoadMore() {
        if(loading){
            return;
        }
        loading = true;
        String url = null;
        if (type.equals("cache")) {
            url = Datas.userCache;
        } else {
            url = Datas.userHistory;
        }
        OkHttpUtils.post().url(url)
                .addParams("Token", Config.Token)
                .addParams("username", Config.username)
                .addParams("page", String.valueOf(++page))
                .addParams("pageSize", "10")
                .addParams("uid", uid).build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String json = response.body().string();
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //加入数据
                            Gson gson = new Gson();
                            HistoryAndCacheBean bean = gson.fromJson(jsonArray.get(i).toString(), HistoryAndCacheBean.class);
                            beans.add(bean);
                        }
                        h.sendEmptyMessage(10);
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
