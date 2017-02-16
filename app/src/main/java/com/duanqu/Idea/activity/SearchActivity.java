package com.duanqu.Idea.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.duanqu.Idea.Adapter.CommentAdapter;
import com.duanqu.Idea.CustomView.MaterialSearchView.MaterialSearchView;
import com.duanqu.Idea.JsonParse.SearchJsonParse;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.CommentBean;
import com.duanqu.Idea.test.Datas;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Me on 2017/1/22.
 */

public class SearchActivity extends AppCompatActivity {
    private MaterialSearchView mSearchView;
    private SwipeRefreshLayout sr;
    private ArrayList<CommentBean> beans;
    private ListView listview;
    private CommentAdapter mCommentAdapter;

    private int pageNum = 1;
    private int SUCCESS = 2;
    private int REFRESH = 1;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:{
                    sr.setEnabled(true);
                    boolean enable = sr.isRefreshing();
                    sr.setRefreshing(!enable);
                    sr.setEnabled(false);
                    break;
                }
                case 2:{
                    if(mCommentAdapter ==null){
                        mCommentAdapter = new CommentAdapter(SearchActivity.this,beans);
                        listview.setAdapter(mCommentAdapter);
                    }else{
                        mCommentAdapter.notifyDataSetChanged();
                    }
                    break;
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        initView();


    }

    private void initView() {
        mSearchView = (MaterialSearchView) findViewById(R.id.searchView);
        mSearchView.setActivity(this);
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        sr.setEnabled(false);
        listview = (ListView) findViewById(R.id.listview);
        mSearchView.setOnQueryListener(new MaterialSearchView.OnTextQueryListener() {
            @Override
            public boolean onQueryTextSubmit(CharSequence query) {
                handler.sendEmptyMessage(REFRESH);
                QueryFromServer(query.toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(CharSequence newText) {
                return true;
            }
        });


        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void QueryFromServer(String key) {
        OkHttpUtils.get()
                .url(Datas.SearchFeed)
                .addParams("token","123")
                .addParams("serachKey",key)
                .addParams("searchField","content")
                .addParams("pageNum", String.valueOf(pageNum))
                .addParams("pageSize","10")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                SearchJsonParse Parse = new SearchJsonParse();
                beans = Parse.Parse(json);
                handler.sendEmptyMessage(REFRESH);
                handler.sendEmptyMessage(SUCCESS);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                handler.sendEmptyMessage(REFRESH);
            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });


    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            //获取宽高
            mSearchView.open();
        }
    }
}
