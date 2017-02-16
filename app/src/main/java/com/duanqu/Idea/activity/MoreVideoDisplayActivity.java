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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.GridViewAdapter;
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

public class MoreVideoDisplayActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private GridView gridView;
    private ArrayList<MainMessageBean> beans = new ArrayList<>();
    private Boolean loading =false;
    private GridViewAdapter mGridViewAdapter = null;


    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    if(mGridViewAdapter == null){
                        mGridViewAdapter = new GridViewAdapter(MoreVideoDisplayActivity.this,R.layout.more_vidoe_gridview_item,beans);
                    }
                    gridView.setAdapter(mGridViewAdapter);
                    break;
                }

                case 1: {
//                    Intent intent = new Intent(HistoryAndCacheCommentActitity.this, FeedActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("first", mainMessageBeen);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    break;
                }


                case 10: {
                    loading = false;

                    break;
                }

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_video_display_activity);

        initView();
        initData();
    }

    private void initData() {
        LoadFromServer();


    }
    int page = 1;
    private void LoadFromServer() {
        OkHttpUtils.get().url(Datas.NewestVideoFeed)
                .addParams("userId", Config.userid)
                .addParams("pageNum", String.valueOf(page))
                .addParams("pageSize", "10")
                .addParams("token", "123")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                JSONArray array = new JSONArray(json);
                MainMessageParse mainMessageParse = new MainMessageParse();
                for (int i = 0; i < array.length(); i++) {
                    MainMessageBean mainMessageBeen = mainMessageParse.Parse(array.get(i).toString());
                    beans.add(mainMessageBeen);
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
        gridView = (GridView) findViewById(R.id.videogrid);
        toolbar.setTitle("小视频");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
