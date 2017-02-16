package com.duanqu.Idea.ViewHolder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.MaterialDialog;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.FeedActivity;
import com.duanqu.Idea.activity.LoadingButtonPopupWindow;
import com.duanqu.Idea.activity.ParallaxOtherUserInfoDisplayActivity;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.player.view.SuperVideoPlayer;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/4.
 */
public abstract class MainMessageBaseViewHolder extends BaseItemImp<MainMessageBean> {
    MainMessageBean data;
    private ImageView zan;
    private TextView comments;
    private TextView resend;
    private SimpleDraweeView messageHead;
    private TextView username;
    private TextView sign;
    private TextView time;
    //转发
    private LinearLayout fwd;
    //评论
    private LinearLayout cmt;
    //点赞
    private LinearLayout prz;
    private HashMap userInfo;

    //贴子是否点击进入了查看页面
    private boolean ifInDeatal = true;

    private int feedId = 0;

    private MaterialEditText editText;
    private LoadingButtonPopupWindow popupWindow;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(context, "转发出现错误！", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public int getViewRes() {
        return getViewLayout();
    }

    @Override
    public void onFindView(@NonNull View parent) {
        messageHead = (SimpleDraweeView) parent.findViewById(R.id.messageHead);
        username = (TextView) parent.findViewById(R.id.username);
        sign = (TextView) parent.findViewById(R.id.sign);
        time = (TextView) parent.findViewById(R.id.time);
        fwd = (LinearLayout) parent.findViewById(R.id.fwd);
        cmt = (LinearLayout) parent.findViewById(R.id.cmt);
        prz = (LinearLayout) parent.findViewById(R.id.prz);
        zan = (ImageView) parent.findViewById(R.id.zan);
        comments = (TextView) parent.findViewById(R.id.comments);
        resend = (TextView) parent.findViewById(R.id.resend);
        messageHead.setOnClickListener(this);
        username.setOnClickListener(this);
        sign.setOnClickListener(this);
        time.setOnClickListener(this);
        //转发
        fwd.setOnClickListener(this);
        cmt.setOnClickListener(this);
        prz.setOnClickListener(this);
        FindView(parent);
    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MainMessageBean data, int dynamicType) {
        this.data =data;
        feedId = (int) data.getMessageInfo().get("id");
        String url = (String) data.getUserInfo().get("headurl");
        Uri uri = Uri.parse(url);
        messageHead.setImageURI(uri);
        userInfo = data.getUserInfo();
        username.setText((String)userInfo.get("nickname"));
        sign.setText((String)userInfo.get("sign"));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date blogDate = df.parse((String)data.getMessageInfo().get("time"));
            Date nowDate = new Date();
            Log.e("时间差距","blogTime"+blogDate.getTime()+"nowDate"+nowDate.getTime());
            long s =  nowDate.getTime() - blogDate.getTime();
            s = s/1000;
            setTime(s);

        }catch (Exception e){
            e.printStackTrace();
        }



        //初始化评论数目
        comments.setText(String.valueOf((Integer) data.getMessageInfo().get("comment")));

        //初始化转发数目
        resend.setText(String.valueOf((Integer) data.getMessageInfo().get("resent")));

        //初始化点赞图标
        if((Integer)data.getMessageInfo().get("allike")!=0){
            zan.setImageResource(R.drawable.tl_menu_icon_prz_press);
            zan.setTag(true);
        }else{
            zan.setTag(false);
        }

//        mMaterialDialog = new MaterialDialog(getActivityContext())
//                .setTitle("转发")
//                .setMessage("请输入转发的理由")
//                .setPositiveButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //.showAsDropDown(cmt);
//                        popupWindow.showAtLocation(cmt, Gravity.CENTER_VERTICAL,0,0);
//                        String addContent = editText.getText().toString();
//                        ResendFeed(addContent);
//
//                        try{
//                            mMaterialDialog.dismiss();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                })
//                .setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try{
//                            mMaterialDialog.dismiss();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
        popupWindow = new LoadingButtonPopupWindow(getActivityContext(),getActivityContext());
//        View view = View.inflate(getActivityContext(),R.layout.edit,null);
//        editText = (MaterialEditText) view.findViewById(R.id.reason);
//        editText.setTextColor(0x00000000);
//        editText.setHintTextColor(0x55000000);
//        mMaterialDialog.setContentView(view);
        bindData(data);
    }

    protected abstract void bindData(MainMessageBean data);
    protected abstract int getViewLayout();
    protected abstract void  FindView(View parent);

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.fwd:{
                //mMaterialDialog.show();
                //showInputDialog();
                if(data.getType()==6){
                    Toast.makeText(context, "纯文字不允许转发", Toast.LENGTH_SHORT).show();
                    return;
                }
                showMaterialDialog();
                break;
            }
            case R.id.cmt:{
                ifInDeatal = !ifInDeatal;
                if(ifInDeatal == false){
                    Intent intent = new Intent(getActivityContext(), FeedActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("first",data);
                    intent.putExtras(bundle);
                    getActivityContext().startActivity(intent);
                }else{
                    return;
                }

                break;
            }

            case R.id.prz:{

                if(!(Boolean)zan.getTag()){
                    IncrOrCancleLike(true);
                    zan.setImageResource(R.drawable.tl_menu_icon_prz_press);
                    zan.setTag(true);
                }else{
                    IncrOrCancleLike(false);
                    zan.setImageResource(R.drawable.tl_menu_icon_prz_nor);
                    zan.setTag(false);
                }
                break;
            }

            case R.id.messageHead:
            case R.id.username:{
                Intent intent = new Intent(getActivityContext(), ParallaxOtherUserInfoDisplayActivity.class);
                intent.putExtra("userid", String.valueOf(data.getUserInfo().get("id")));
                intent.putExtra("nickname", (String)userInfo.get("nickname"));
                getActivityContext().startActivity(intent);
                break;
            }

        }

    }

    public void setTime(long time) {
        if(time/60 < 60){
            this.time.setText(time/60+"分钟前");
        }else if(time/3600<24){
            this.time.setText(time/3600 +"小时前");
        }else{
            this.time.setText((String)data.getMessageInfo().get("time"));
        }
    }

    private void IncrOrCancleLike(boolean like)
    {
        String url = null;
        String uid = String.valueOf(userInfo.get("id"));
        String countInfo = null;
        if(like == true){
            countInfo = Datas.incLove;
            url = Datas.incrLike;
        }else{
            countInfo = Datas.decLove;
            url = Datas.cancelLike;
        }
        OkHttpUtils.post().url(url)
                .addParams("userId", Config.userid)
                .addParams("feedId", String.valueOf(feedId))
                .addParams("token","123")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        if(response.code()==200){
                            Log.e("like","success");
                        }else{
                            System.out.println(response.code());
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("error","xx");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("response","xxx");
                    }
                });


        OkHttpUtils.post().url(countInfo)
                .addParams("uid", uid)
                .addParams("username",Config.username)
                .addParams("Token",Config.Token)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {

                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("error","xx");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("response","xxx");
                    }
                });


    }


    public void ResendFeed(String resentContent){
        OkHttpUtils.post().url(Datas.ResendFeed)
                .addParams("feedId", String.valueOf(feedId))
                .addParams("resentContent",resentContent)
                .addParams("userId",Config.userid)
                .addParams("token","123")
                .addParams("text","")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                if (response.code()==200){
                    popupWindow.Complete();

                }else{
                   Log.e("Server Error","resendFeed");
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("onError","xx");
                popupWindow.dismiss();
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Object response, int id) {
//               Log.e("onResponse","xx");
//                popupWindow.Complete();
            }
        });


    }



    public void showMaterialDialog() {


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivityContext())
                .setTitle("转发")
                .setEditModelEnable(true)
                .showEditModel("","转发的理由",-1)
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .setOnDialogButtonClickListener(new MaterialDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(DialogInterface dialog, Editable editable, boolean isSure) {
                        if(isSure){
                            popupWindow.showAtLocation(cmt, Gravity.CENTER_VERTICAL,0,0);
                            String addContent = editable.toString();

                            addContent = MyApplication.Decode(addContent);

                            Log.e("MaterialDialog",addContent);
                            ResendFeed(addContent);
                            dialog.dismiss();
                        }else{
                            dialog.dismiss();
                        }

                    }
                });

        builder.show();
    }





    /**
     * 显示一个带输入框的Dialog
     */
//    private void showInputDialog() {
//        final MaterialDialogInput dialogInput = new MaterialDialogInput(getActivityContext());
//
//        //dialogInput.setIcon(R.mipmap.ic_launcher);//设置图标
//        dialogInput.setTitle("转发");
//        dialogInput.setDesc("转发的理由：");
//        dialogInput.setNegativeButton("取消", new DialogWithTitle.OnClickListener() {
//            @Override
//            public void click(DialogBase dialog, View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialogInput.setPositiveButton("确定", new DialogWithTitle.OnClickListener() {
//            @Override
//            public void click(DialogBase dialog, View view) {
//                popupWindow.showAtLocation(cmt, Gravity.CENTER_VERTICAL,0,0);
//                String addContent = dialogInput.getUserInput().toString();
//                ResendFeed(addContent);
//                dialog.dismiss();
//            }
//        });
//
//    }



}
