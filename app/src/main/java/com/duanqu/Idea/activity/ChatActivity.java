package com.duanqu.Idea.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.duanqu.Idea.Adapter.ChatAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.DatabaseUtils.MyDatabaseHelper;
import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.ChatBean;
import com.duanqu.Idea.socket.SocketConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    LinkedList<ChatBean> historybeans = new LinkedList<>();
    MyDatabaseHelper databaseHelper = new MyDatabaseHelper(this, "user", null, 1);
    SwipeRefreshLayout sr;
    SQLiteDatabase database;
    String nickname;
    public static String username;
    public static String currentusername;
    static String userhead;
    EditText text;
    Button send;
    int page = 1;
    static ListView listView;
    Toolbar toolbar;
    public static LinkedList<ChatBean> datas = new LinkedList<>();
    public static ChatAdapter adapter;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if(msg.what==0){
//                adapter.notifyDataSetChanged();
//                return;
//            }

            ChatBean chatBean = (ChatBean) msg.obj;
            if (chatBean != null) {
                chatBean.setUsername(username);
                chatBean.setUserhead(userhead);
                datas.add(chatBean);
                adapter.notifyDataSetChanged();
                listView.setSelection(datas.size() - 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_aty);
        database = databaseHelper.getWritableDatabase();
        MyApplication.setHandlers("chatActivity", handler);
        Intent intent = getIntent();
        userhead = intent.getStringExtra("userhead");
        nickname = intent.getStringExtra("nickname");
        username = intent.getStringExtra("username");
        currentusername = intent.getStringExtra("username");
        InintView();
        BindData();
        //点进来之后，消息应该清空为0才对
        database.execSQL("UPDATE messagelist SET count = 0 WHERE username=?", new String[]{username});
        MyApplication.getHandlers("messageActivity").sendEmptyMessage(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentusername = username;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentusername = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentusername = null;
    }

    private void BindData() {
        datas = (LinkedList<ChatBean>) getChatHistoryByPage(page);
        setTimeTop(datas);
        adapter = new ChatAdapter(this, datas);
        listView.setAdapter(adapter);
        listView.setSelection(datas.size() - 1);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(listView != null && listView.getChildCount() > 0){
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    setSwipeRefreshEnable(enable);
                }
            }
        });

//        final PopupList popupList = new PopupList();
//        ArrayList popupMenuItemList = new ArrayList();
//        popupMenuItemList.add("复制");
//        popupList.init(this, listView, popupMenuItemList, new PopupList.OnPopupListClickListener() {
//            @Override
//            public void onPopupListClick(View contextView, int contextPosition, int position) {
//            String text = datas.get(contextPosition).getText();
//                Toast.makeText(ChatActivity.this, text, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        ImageView indicator = new ImageView(this);
//        indicator.setImageResource(R.drawable.popuplist_default_arrow);
//        popupList.setIndicatorView(indicator);
//        popupList.setIndicatorSize(MyApplication.dip2px(16), MyApplication.dip2px(8));


    }

    private void setTimeTop(LinkedList<ChatBean> list)
    {
        if(list==null || list.size()==0){
            return;
        }
        ChatBean bean = new ChatBean();
        bean.setTime(list.get(0).getTime());
        bean.setType(ChatBean.TIME);
        datas.addFirst(bean);
    }

    private void InintView() {
        text = (EditText) findViewById(R.id.text);
        send = (Button) findViewById(R.id.send);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview);

        toolbar.setTitle(nickname);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initSwipeRefreshLayout();
        InitSend();
    }

    private void InitSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text.getText().toString();
                if(message.length()==0){
                    Toast.makeText(ChatActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                String noEncodeMessage = message;
                try {
                    message = URLEncoder.encode(URLEncoder.encode(message, "UTF-8"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = df.format(new Date());
                String to = username;
                String type = "text";

                JSONObject testObject = new JSONObject();
                try {
                    testObject.put("tag", "message");
                    testObject.put("message", message);
                    testObject.put("time", time);
                    testObject.put("to", to);
                    testObject.put("type", type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ChatBean chatBean = new ChatBean();
                chatBean.setEncodeString(testObject.toString());
                chatBean.setType(chatBean.RIGHT_TEXT);
                chatBean.setTag(0);
                chatBean.setText(noEncodeMessage);
                datas.add(chatBean);
                SocketConnect.Instance().ExecuteTaskByCustomMessage(testObject.toString());
                adapter.notifyDataSetChanged();
                listView.setSelection(datas.size() - 1);
                //插入数据库
                if(chatBean.getTag()!=1){
                    database.execSQL("INSERT INTO message (time,message,username,left,right,type)" +
                            "values(?,?,?,?,?,?)", new Object[]{time, noEncodeMessage, username, 0, 1, type});

                    //发送数据之后，最新的一条消息应该是自己的消息
                    database.execSQL("UPDATE messagelist SET lastmessage=? WHERE username=?", new Object[]{noEncodeMessage, username});
                }
                text.setText("");
            }

        });
    }

    private void initSwipeRefreshLayout() {
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(historybeans.size()==0){
                    sr.setRefreshing(false);
                    return;
                }

                LinkedList<ChatBean> linkedList = (LinkedList) DivisiByRange(historybeans,++page);
                ChatBean chatBean = new ChatBean();
                //Log.e("xxx",linkedList.get(0).getTime());


                for(int i=0;i<linkedList.size();i++){
                    datas.addFirst((ChatBean) linkedList.get(i));
                }
                if(linkedList.size()!=0){
                    setTimeTop(linkedList);
                }
                handler.sendEmptyMessage(0);
                listView.setSelection(linkedList.size()-1);
                sr.setRefreshing(false);
            }
        });
    }


    public void setSwipeRefreshEnable(boolean enable) {
        sr.setEnabled(enable);
    }

    //倒序的page，1表示最后一页(也就是最新的一页),
    //一页默认15条数据
    private List<ChatBean> getChatHistoryByPage(int page) {

        ChatBean bean = null;
        Cursor cursor = database.rawQuery("SELECT * FROM message WHERE username=?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                bean = new ChatBean();
                bean.setTime(cursor.getString(1));
                bean.setText(cursor.getString(2));
                if (cursor.getInt(4) == 1) {
                    bean.setUsername(username);
                    bean.setUserhead(userhead);
                    bean.setType(ChatBean.LEFT_TEXT);
                } else {
                    bean.setUserhead(Config.headurl);
                    bean.setType(ChatBean.RIGHT_TEXT);
                }
                historybeans.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();

        //如果历史消息是0，则返回null
        if(historybeans.size()==0){
            return new LinkedList<ChatBean>();
        }
        List split;
        split = DivisiByRange(historybeans,page);
        return split;
    }

    private List DivisiByRange(List<ChatBean> list, int page) {
        int size = historybeans.size();
        int first = size - page * 15;
        int end = first + 15;

        if (first < 0) {
            first = 0;
        }
        List list_new = new LinkedList();
        //插入时间
        ChatBean chatBean = new ChatBean();
        //Log.e("xxx",list.get(first).getTime());
//        chatBean.setTime(list.get(first).getTime());
//        chatBean.setType(ChatBean.TIME);
//        list_new.add(chatBean);

        for (int i = first; i < end; i++) {
            if(i!=first){
                String time = list.get(i-1).getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date lastdate = sdf.parse(time);
                    Date date = sdf.parse(list.get(i).getTime());
                    long diff = date.getTime() - lastdate.getTime();
                    long mins = diff / (1000 * 60);
                    Log.e("mins",""+mins);
                    if(mins>10){
                        chatBean = new ChatBean();
                        chatBean.setTime(list.get(i).getTime());
                        chatBean.setType(ChatBean.TIME);
                        list_new.add(chatBean);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            list_new.add(list.get(i));
        }
        return list_new;
    }
}
