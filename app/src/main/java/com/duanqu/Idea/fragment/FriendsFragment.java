package com.duanqu.Idea.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;


import com.duanqu.Idea.Adapter.FriendsAdapter;
import com.duanqu.Idea.Adapter.FriendsListViewAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.DatabaseUtils.MyDatabaseHelper;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ParallaxOtherUserInfoDisplayActivity;
import com.duanqu.Idea.bean.ChatBean;
import com.duanqu.Idea.bean.FriendBean;
import com.duanqu.Idea.bean.FriendsListBean;
import com.duanqu.Idea.fragment.BaseFragment;
import com.duanqu.Idea.test.Datas;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.utils.Exceptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/2.
 */
public class FriendsFragment extends BaseFragment {
    private ListView listView;
    private FriendsAdapter adapter = null;
    private ArrayList<FriendBean> datas = new ArrayList<>();
    SwipeRefreshLayout sr;
    MyDatabaseHelper databaseHelper;
    SQLiteDatabase database;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    adapter.notifyDataSetChanged();
                    sr.setRefreshing(false);
                }
            }
        }
    };




    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new MyDatabaseHelper(getActivity(),"user",null,1);
        database = databaseHelper.getWritableDatabase();
        View view = inflater.inflate(R.layout.friends_fragment, null);
        listView = (ListView) view.findViewById(R.id.listview);
        sr = (SwipeRefreshLayout) view.findViewById(R.id.sr);
        initSwipeRefreshLayout();
        initListView();
        InitDataFromLocal();




        return view;
    }

    private void InitDataFromLocal() {
        Cursor c=  database.rawQuery("SELECT * FROM friends",null);
        datas.clear();
        if(c.moveToFirst()){
            do {
                FriendBean pojo = new FriendBean();
                pojo.setUsername(c.getString(1));
                pojo.setHeadurl(c.getString(2));
                pojo.setNickname(c.getString(3));
                pojo.setSign(c.getString(4));
                datas.add(pojo);
            }while (c.moveToNext());
        }
    }

    private void AnsyFriendsInfoFromServer() {
        OkHttpUtils.post().url(Datas.GetContacts)
                .addParams("username", Config.username)
                .addParams("Token", Config.Token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                try {
                    JsonParse(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
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

    private void initListView() {
        if (adapter == null) {
            adapter = new FriendsAdapter(getActivity(), R.layout.friends_listview_item, datas);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ParallaxOtherUserInfoDisplayActivity.class);
                intent.putExtra("contact",datas.get(position).getUsername());
                startActivity(intent);
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
                }
            }
        });

    }

    void JsonParse(String json) {
        datas.clear();
        try {
            database.execSQL("DELETE FROM friends");
            JSONArray mJsonArray = new JSONArray(json);
            for (int i = 0; i < mJsonArray.length(); i++) {
                FriendBean pojo = new FriendBean();
                JSONObject object = mJsonArray.getJSONObject(i);
                pojo.setUsername(object.getString("username"));
                pojo.setNickname(object.getString("nickname"));
                pojo.setSign(object.getString("sign"));
                pojo.setHeadurl(object.getString("headurl"));
                database.execSQL("INSERT INTO friends (username,userhead,nickname,sign) VALUES(?,?,?,?)",
                        new String[]{pojo.getUsername(),pojo.getHeadurl(),pojo.getNickname(),pojo.getSign()});
                datas.add(pojo);
            }
            handler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSwipeRefreshLayout() {
        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AnsyFriendsInfoFromServer();
            }
        });
    }



}
