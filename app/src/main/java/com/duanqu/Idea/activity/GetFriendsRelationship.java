package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.duanqu.Idea.Adapter.FriendsListViewAdapter;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.Friends_TYPE0;
import com.duanqu.Idea.bean.FriendsListBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/14.
 */
public class GetFriendsRelationship extends PopupWindow implements View.OnClickListener{
    private Context context;
    private View view;
    private ProgressBar pb_rotate;
    private ListView listView;
    private ArrayList<FriendsListBean> friends;

    private final int READY = 1;
    private Handler handler = new Handler(){
        @Override
        public String getMessageName(Message message) {
            switch (message.what){
                case READY:{
                    pb_rotate.setVisibility(View.GONE);

                    break;
                }


            }
            return super.getMessageName(message);
        }
    };

    public GetFriendsRelationship(Context context)
    {
        this.context = context;
        //加载布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.footnavigate, null);
        pb_rotate = (ProgressBar) view.findViewById(R.id.pb_rotate);
        listView = (ListView) view.findViewById(R.id.friends);
        initListView();

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(400);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(600);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x80000000);
//        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);
        this.setFocusable(false);




    }

    private void initListView() {
        FriendsListViewAdapter adapter = new FriendsListViewAdapter((Activity) context,
                new FriendsListViewAdapter.Builder<FriendsListBean>().setDatas(friends).addType(0,Friends_TYPE0.class).build());


    }


    @Override
    public void onClick(View v) {

    }
}
