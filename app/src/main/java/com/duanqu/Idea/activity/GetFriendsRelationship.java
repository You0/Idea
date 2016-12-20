package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.duanqu.Idea.Adapter.FriendsListViewAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.DatabaseUtils.MyDatabaseHelper;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.Friends_TYPE0;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.FriendsListBean;
import com.duanqu.Idea.test.Datas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/14.
 */
public class GetFriendsRelationship extends PopupWindow implements View.OnClickListener{
    private Context context;
    private View view;
    private ProgressBar pb_rotate;
    private ListView listView;
    private FriendsListViewAdapter adapter;
    private ArrayList<FriendsListBean> friends = new ArrayList<>();
    private Set<String> al_checked;

    private final int READY = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case READY:{
                    //这里需要修改，以后是网络返回的内容赋给本地的数据。
                    adapter = new FriendsListViewAdapter((Activity) context,
                            new FriendsListViewAdapter.Builder<FriendsListBean>().
                                    addType(0,Friends_TYPE0.class)
                                    .setDatas(friends).build());

                    pb_rotate.setVisibility(View.GONE);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                }


            }
        }
    };

    public GetFriendsRelationship(Context context)
    {
        this.context = context;
        //加载布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.friends_relation, null);
        pb_rotate = (ProgressBar) view.findViewById(R.id.pb_rotate);
        listView = (ListView) view.findViewById(R.id.friends);
        initListView();

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int)Config.WIDTH/3*2);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        // /
        ColorDrawable dw = new ColorDrawable(0x00000000);
//        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);




    }

    //初始化listview的操作，在这里请求网络数据，和获得已经checked的数据，并使用handler返回。
    private void initListView() {
       // al_checked = SendActivity.getViews();
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(MyApplication.getContext()
        ,"user",null,1);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM friends",null);

        if(cursor.moveToFirst()){
            do{
                //username,userhead,nickname,sign
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                String userhead = cursor.getString(cursor.getColumnIndex("userhead"));
                String username = cursor.getString(cursor.getColumnIndex("username"));;
                FriendsListBean pojo = new FriendsListBean();
                pojo.setUrl(userhead);
                pojo.setName(nickname);
                pojo.setUsername(username);
                friends.add(pojo);
            }while (cursor.moveToNext());
        }

        handler.sendEmptyMessage(READY);

        //if(Cached()) 这里先写flase
//        if(false){
//            //下面请求网络。
//            handler.sendEmptyMessage(READY);
//            //handler.sendEmptyMessageDelayed(READY,200)
//        }else{
//            handler.sendEmptyMessage(READY);
//        }


    }

    private boolean Cached()
    {
        if(friends!=null&&friends.size()!=0){
            return true;
        }else{
            return false;
        }
    }




    @Override
    public void onClick(View v) {

    }
}
