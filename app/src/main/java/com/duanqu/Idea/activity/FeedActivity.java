package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Adapter.FeedAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.JsonParse.FeedJsonParse;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE1;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE2;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE3;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE4;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE5;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE6;
import com.duanqu.Idea.ViewHolder.Item_content_TYPE7;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.test.TestAdapter;
import com.duanqu.Idea.test.TestViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FeedActivity extends AppCompatActivity implements View.OnClickListener {
    ListView feed;
    Button send;
    public static EditText send_edit;
    TextView at;
    FeedAdapter adapter;
    MainMessageBean mainMessageBean;
    ArrayList<MainMessageBean> arrays = null;
    Toolbar toolbar;
    String FeedId;
    String ToName;
    String FromName;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            feed.setSelection(msg.what);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);
        Intent intent = getIntent();
        mainMessageBean = (MainMessageBean) intent.getSerializableExtra("first");

        Log.e("FeedActivity", "mainMessageBean:" + mainMessageBean.toString() + "    is null" +
                (mainMessageBean == null));
//        mainMessageBean = (MainMessageBean) intent.
//                getBundleExtra("bundle").getSerializable("first");
        initView();
        bindData();
    }


    private void bindData() {
        if (arrays == null) {
            arrays = new ArrayList<>();
        }

        arrays.add(mainMessageBean);
        String test = "{\"despatcherName\":\"五颜六色\",\"despatcherAvatar\":\"http://115.159.159.65:8080/EAsy/Headurl/f6aa7245-5d3c-4887-8e78-8482fb28.jpg\",\"time\":\"Sun Aug 14 2016 22:25:09 GMT+0800 (中国标准时间)\",\"feedId\":\"1245264\",\"content\":\"顶起来\",\"_id\":\"57b05a4c17515db454688afd\",\"__v\":0,\"attachComments\":[{\"fromName\":\"zs1\",\"toName\":\"root1\",\"content\":\"楼中楼\"},{\"fromName\":\"zs1\",\"toName\":\"root1\",\"content\":\"楼中楼\"},{\"fromName\":\"zs1\",\"toName\":\"root1\",\"content\":\"楼中楼\"}],\"likeCount\":0,\"like\":[]}";
        FeedJsonParse fjp = new FeedJsonParse();
        MainMessageBean temp = fjp.Parse(test);
        arrays.add(temp);

        temp = fjp.Parse("{\"despatcherName\":\"五颜六色\",\"despatcherAvatar\":\"http://115.159.159.65:8080/EAsy/Headurl/f6aa7245-5d3c-4887-8e78-8482fb28.jpg\",\"time\":\"Sun Aug 14 2016 22:25:09 GMT+0800 (中国标准时间)\",\"feedId\":\"1245264\",\"content\":\"老弟有句话不知道当讲不当讲.\",\"_id\":\"57b05a4c17515db454688afd\",\"__v\":0,\"attachComments\":[],\"likeCount\":0,\"like\":[]}");
        arrays.add(temp);

        temp = fjp.Parse("{\"despatcherName\":\"五颜六色\",\"despatcherAvatar\":\"http://115.159.159.65:8080/EAsy/Headurl/f6aa7245-5d3c-4887-8e78-8482fb28.jpg\",\"time\":\"Sun Aug 14 2016 22:25:09 GMT+0800 (中国标准时间)\",\"feedId\":\"1245264\",\"content\":\"老弟有句话不知道当讲不当讲.\",\"_id\":\"57b05a4c17515db454688afd\",\"__v\":0,\"attachComments\":[],\"likeCount\":0,\"like\":[]}");
        arrays.add(temp);

        temp = fjp.Parse("{\"despatcherName\":\"五颜六色\",\"despatcherAvatar\":\"http://115.159.159.65:8080/EAsy/Headurl/f6aa7245-5d3c-4887-8e78-8482fb28.jpg\",\"time\":\"Sun Aug 14 2016 22:25:09 GMT+0800 (中国标准时间)\",\"feedId\":\"1245264\",\"content\":\"老弟有句话不知道当讲不当讲.\",\"_id\":\"57b05a4c17515db454688afd\",\"__v\":0,\"attachComments\":[],\"likeCount\":0,\"like\":[]}");
        arrays.add(temp);

        adapter = new FeedAdapter(this,
                new BaseAdapter.Builder()
                        .addType(0, Item_content_TYPE1.class)
                        .addType(1, Item_content_TYPE2.class)
                        .addType(2, Item_content_TYPE3.class)
                        .addType(4, Item_content_TYPE4.class)
                        .addType(3, Item_content_TYPE5.class)
                        .addType(5, Item_content_TYPE6.class)
                        .addType(6, Item_content_TYPE7.class)
                        .setDatas(arrays).build());
        feed.setAdapter(adapter);

        feed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int CurrentPosition = position;
                MainMessageBean bean = arrays.get(position-1);
                FeedId = bean.getFeedId();
                ToName = bean.getUserInfo().get("nickname");
                FromName = Config.nickname;
                send_edit.setFocusable(true);
                send_edit.requestFocus();


                InputMethodManager inputManager =
                        (InputMethodManager)send_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(send_edit, 0);
                handler.sendEmptyMessageDelayed(position,500);


                //InputMethodManager imm = (InputMethodManager)FeedActivity.getSystemService(FeedActivity.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        feed = (ListView) findViewById(R.id.feed);
        send = (Button) findViewById(R.id.send);
        send_edit = (EditText) findViewById(R.id.send_edit);
        at = (TextView) findViewById(R.id.at);

        toolbar.setTitle("详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {


    }
}
