package com.duanqu.Idea.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.duanqu.Idea.Adapter.MessageAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.DatabaseUtils.MyDatabaseHelper;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ChatActivity;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.MessageListBean;
import com.duanqu.Idea.bean.ParallaxOtherUserBean;
import com.duanqu.Idea.test.Datas;
import com.duanqu.Idea.utils.PopupList;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/10.
 */
public class MessageFragment extends BaseFragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    ListView message_list;
    static MessageAdapter messageAdapter;
    ArrayList<MessageListBean> beans = new ArrayList<>();
    MyDatabaseHelper databaseHelper;
    View view;
    String TAG = "messageFragment";
    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:{
                    SelectFromDatabase();
                    GetInfoFromServer();
                    handler.sendEmptyMessage(1);
                    break;
                }
                case 1:{
                    messageAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    };

    private void GetInfoFromServer() {
        String contacts = "";
        for (MessageListBean bean:beans){
            if(bean.getUserhead().equals("head")||bean.getNickname().equals("NULL")){
                contacts += bean.getUsername()+";";
                Log.e("activity",bean.getUsername());
            }
        }
        if(contacts.length()==0){
            handler.sendEmptyMessage(1);
            return;
        }
        OkHttpUtils.post().url(Datas.GetUserInfoList)
                .addParams("Token", Config.Token)
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addParams("username",Config.username)
                .addParams("contact",contacts)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                Gson gson = new Gson();
                String json = response.body().string();
                Log.e("activity",json);
                JSONArray jsonArray = new JSONArray(json);
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                for(int i=0;i<jsonArray.length();i++){
                    ParallaxOtherUserBean bean = gson.fromJson(jsonArray.getJSONObject(i).toString(), ParallaxOtherUserBean.class);
                    for(MessageListBean bean1:beans){
                        if(bean1.getUsername().equals(bean.getUsername())){
                            bean1.setUserhead(bean.getHeadurl());
                            bean1.setNickname(bean.getNickname());
                            //更新数据库
                            database.execSQL("UPDATE messagelist SET userhead=?,nickname=? WHERE username=?",
                                    new String[]{bean.getHeadurl(),bean.getNickname(),bean.getUsername()});
                        }
                    }
                }
                handler.sendEmptyMessage(1);

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



    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_layout,null);
        MyApplication.setHandlers("messageActivity",handler);
        init();

        return view;
    }

    private void init() {
        message_list = (ListView) view.findViewById(R.id.message_list);
        databaseHelper = new MyDatabaseHelper(getActivity(), "user", null, 1);
        SelectFromDatabase();
        messageAdapter = new MessageAdapter(getActivity(), R.layout.message_item_layout, beans);
        message_list.setAdapter(messageAdapter);
        message_list.setOnItemClickListener(this);
//        message_list.setOnItemLongClickListener(this);

        final PopupList popupList = new PopupList();
        ArrayList popupMenuItemList = new ArrayList();
        popupMenuItemList.add("删除");
        popupList.init(getActivity(), message_list, popupMenuItemList, new PopupList.OnPopupListClickListener() {
            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {

                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                database.execSQL("DELETE FROM messagelist WHERE username=?",new String[]{beans.get(contextPosition).getUsername()});
                beans.remove(contextPosition);
                messageAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), contextPosition + "," + position, Toast.LENGTH_LONG).show();
            }
        });
        ImageView indicator = new ImageView(getActivity());
        indicator.setImageResource(R.drawable.popuplist_default_arrow);
        popupList.setIndicatorView(indicator);
        popupList.setIndicatorSize(MyApplication.dip2px(16), MyApplication.dip2px(8));


        GetInfoFromServer();

    }

    private void SelectFromDatabase() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        beans.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM messagelist", null);

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.
                        getColumnIndex("username"));
                String userhead = cursor.getString(cursor.
                        getColumnIndex("userhead"));
                String lastmessage = cursor.getString(cursor.getColumnIndex
                        ("lastmessage"));
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                int count = cursor.getInt(cursor.getColumnIndex("count"));
                int tag = cursor.getInt(cursor.getColumnIndex("tag"));
                MessageListBean bean = new MessageListBean();
                bean.setCount(String.valueOf(count));
                bean.setLastmessage(lastmessage);
                bean.setNickname(nickname);
                bean.setUserhead(userhead);
                bean.setUsername(username);
                bean.setTag(tag);
                beans.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("username",beans.get(position).getUsername());
        intent.putExtra("nickname",beans.get(position).getNickname());
        intent.putExtra("userhead",beans.get(position).getUserhead());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int Position = position;
        Log.e("postion",""+position);
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }






}
