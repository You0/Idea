package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.duanqu.Idea.test.Datas;
import com.duanqu.Idea.test.TestAdapter;
import com.duanqu.Idea.test.TestViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

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
    int pageNum = 0;
    String commentId;
    private final int ERROR = 0;
    private final int SUCCESS = 1;
    private final int GETSUCCESS =2;
    private int Positon = 0;
    FeedJsonParse fjp = new FeedJsonParse();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //feed.setSelection(msg.what);
            switch (msg.what){
                case SUCCESS:{
                    Snackbar.make(send, "发送成功！", Snackbar.LENGTH_LONG)
                            .show();
                    break;
                }
                case ERROR:{
                    Snackbar.make(send, "出现错误！", Snackbar.LENGTH_LONG)
                            .show();
                    break;
                }

                case GETSUCCESS:{
                    adapter.notifyDataSetChanged();
                    break;
                }

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);
        Intent intent = getIntent();
        mainMessageBean = (MainMessageBean) intent.getSerializableExtra("first");
        FeedId = String.valueOf((Integer)mainMessageBean.getMessageInfo().get("id"));
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
                MainMessageBean bean = arrays.get(position);
                if(position==0){
                    Positon = 0;
                    FeedId = String.valueOf((Integer)arrays.get(0).getMessageInfo().get("id"));
                }else{
                    Positon = position;
                    FeedId = bean.getFeedId();
                }
                ToName = (String) bean.getUserInfo().get("nickname");
                FromName = Config.nickname;
                commentId = (String) bean.getMessageInfo().get("_id");
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


        send.setOnClickListener(this);

        GetCommentFromServer();


    }

    private void GetCommentFromServer() {
        //http://115.159.27.70:3000/comment?pageSize=5&pageNum=1&feedId=78&queryType=1
        OkHttpUtils.get().url(Datas.PushComment)
                .addParams("pageSize", String.valueOf(10))
                .addParams("pageNum", String.valueOf(pageNum))
                .addParams("feedId",String.valueOf((Integer)arrays.get(0).getMessageInfo().get("id")))
                .addParams("queryType","1").build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                JSONObject object = new JSONObject(json);
                JSONArray array = object.getJSONArray("result");
                for(int i=0;i<array.length();i++){
                    JSONObject InnerObject = array.getJSONObject(i);
                    MainMessageBean temp = fjp.Parse(InnerObject.toString());
                    arrays.add(temp);
                }
                handler.sendEmptyMessage(GETSUCCESS);
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
        switch (v.getId())
        {
            case R.id.send:{
                String content = send_edit.getText().toString();
                if(content.equals("")){
                    Snackbar.make(send, "请输入内容！", Snackbar.LENGTH_LONG)
                            .show();
                    return ;
                }
                if(Positon == 0){
                    SendComentToServer(content);
                }else{
                    //commentId=584bdc99a8cf5480096b98ee&fromName=zs1&toName=root1&content=test&feedId=6
                    SendAttachmentToServer(content);
                }



                break;
            }

        }

    }

    private void SendAttachmentToServer(String content) {
        OkHttpUtils.post().url(Datas.Pushattachcomment)
                .addParams("feedId",FeedId)
                .addParams("fromName",FromName)
                .addParams("toName",ToName)
                .addParams("content",content)
                .addParams("commentId",commentId).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                if(response.code()==200){
                    handler.sendEmptyMessage(SUCCESS);
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

    private void SendComentToServer(String content) {
        OkHttpUtils.post().url(Datas.PushComment)
                .addParams("despatcherName", Config.nickname)
                .addParams("despatcherAvatar",Config.headurl)
                .addParams("time", String.valueOf(System.currentTimeMillis()))
                .addParams("feedId", FeedId)
                .addParams("content",content).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                if(response.code()==200){
                    handler.sendEmptyMessage(SUCCESS);
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
}
