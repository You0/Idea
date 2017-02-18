package com.duanqu.Idea.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.duanqu.Idea.Config;
import com.duanqu.Idea.DatabaseUtils.MyDatabaseHelper;
import com.duanqu.Idea.JsonParse.MainMessageParse;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ChatActivity;
import com.duanqu.Idea.activity.FeedActivity;
import com.duanqu.Idea.activity.MyAnswerAty;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.ChatBean;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.bean.MessageBean;
import com.duanqu.Idea.socket.SocketConnect;
import com.duanqu.Idea.test.Datas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/22.
 */
public class JsonParse {
    MyDatabaseHelper databaseHelper = new MyDatabaseHelper(MyApplication.getContext()
            ,"user",null,1);
    //保存上一次的json,防止超时重传的时候,收到重复的数据
    String LastJson = null;

    //自己封装的一个HttpUtils方便轻量的网络访问。
    HttpConnectionUtils utils = new HttpConnectionUtils(Datas.GetInfoUseFeedId);


    public void Parse(String Json) {
        try {
            Log.e("parse",Json);
            JSONObject mObject = new JSONObject(Json);
            String tag = mObject.getString("tag");
            // 回应:{tag:"response":[{message:"xxx",time:"xxx"}]}
            // 推送:{tag:"push",title:"xxx",content:"xxxxx"}

            if ("response".equals(tag)) {
                //这个是消息回复
                processResponse(Json);

            } else if ("push".equals(tag)) {
                //这个是服务器的推送
                processPush(Json);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processPush(String json) {


    }

    // 回应:{tag:"response",message:[{message:"xxx",time:"xxx",from="xxx",type="xxx"}]}
    //由于客户端采用的是超时重传机制，所以这里这样写没问题
    //否则这里贸然取消timer可能是下一个任务的timer
    private void processResponse(String json) {
        boolean tag = false;
        if(LastJson == null){
            LastJson = json;
        }else{
            if(LastJson.equals(json)){
                return;
            }else {
                LastJson = json;
            }
        }

        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("message");
            for (int i = 0; i < array.length(); i++) {
                JSONObject message = array.getJSONObject(i);
                String msg = message.getString("message");
                String time = message.getString("time");
                String from = message.getString("from");
                String type = message.getString("type");
                MessageBean bean = new MessageBean();
                bean.setFrom(from);
                bean.setMsg(msg);
                bean.setTime(time);
                bean.setType(type);

                //收到确认消息
                if (from.equals("server")&&msg.equals("success")) {
                    try {
                       // Log.e("JsonParse","success");
                        //收到服务器响应，执行下一个任务
                        if(SocketConnect.Instance().TimeOut!=null){
                            SocketConnect.Instance().TimeOut.cancel();
                            SocketConnect.Instance().TimeOut = null;
                        }

                        if(SocketConnect.Instance().connectTimeOut!=null){
                            SocketConnect.Instance().connectTimeOut.cancel();
                            SocketConnect.Instance().connectTimeOut = null;
                        }
                        //弹出当前任务
                        SocketConnect.Instance().PopTask();
                        //执行下一个任务
                        Runnable r = SocketConnect.Instance().getTask();
                        if (r != null) {
                            ThreadManager.StubmitTask(r);
                        }else{
                            Log.e("JsonParse","任务队列目前为空");
                        }



                    } catch (Exception e) {
                        //已经触发超时重传则不做任何事情
                        Log.e("SERVICE", "已经触发超时重传");
                    }
                }else if(!from.equals("server")){
                    //如果不是来自服务器发送，则是用户message，则要发送getmsg标记
                    UpdateDatabase(bean);
                    //如果此时正处于聊天界面
                    if(ChatActivity.currentusername!=null){
                        //处于聊天界面但不处于新消息用户的聊天界面
                        if(!ChatActivity.username.equals(from)){
                            MyApplication.getHandlers("messageActivity").sendEmptyMessage(0);
                            Notification(msg);
                            return;
                        }
                        ChatBean chatBean = new ChatBean();
                        chatBean.setText(msg);
                        chatBean.setType(chatBean.LEFT_TEXT);
                        Message msg1 = new Message();
                        msg1.obj=chatBean;
                        Handler handler = MyApplication.getHandlers("chatActivity");
                        handler.sendMessage(msg1);



                     //不处于聊天界面
                    }else{
                        MyApplication.getHandlers("messageActivity").sendEmptyMessage(0);
                        Notification(msg);
                    }
//                    ChatBean chatBean = new ChatBean();
//                    chatBean.setText(msg);
//                    chatBean.setType(chatBean.LEFT_TEXT);
//                    System.out.println(i+":"+msg);
//                    Message msg1 = new Message();
//                    msg1.obj=chatBean;
//                    Handler handler = MyApplication.getHandlers("chatActivity");
//                    if(ChatActivity.username!=null&&!ChatActivity.username.equals(from)){
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
//                        builder.setSmallIcon(R.mipmap.ic_launcher);
//                        builder.setContentTitle("有人私信你啦");
//                        builder.setContentText(msg);
//                        Notification notification = builder.build();
//                        NotificationManager manager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                        manager.notify(1200, notification);
//                        long[] pattern = {0,100,50,100};
//                        VibratorUtil.Vibrate(pattern,false);
//                    }
                   // handler.sendEmptyMessage(0);
                    //把消息发回去
                    //handler.sendMessage(msg1);
                    //MessageActivity.handler.sendEmptyMessage(0);
                    //MainActivity.handler.sendMessage(msg1);

                    tag = true;
                }else {
                    //既不是用户信息也不是服务器的反馈信息,则就是一些通知了
                    //比如说有人@你了，有人回复你了，这2种情况则直接去获取帖子信息就行了
                    //{feedId:1234,message:msg,type=at/hf/tz}
                    JSONObject object1 = new JSONObject(msg);
                    String feedId = object1.getString("feedId");
                    String message1=null;
                    try{
                        message1 = object1.getString("message");
                    }catch (Exception e){

                    }

                    String type1 = object1.getString("type");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
                    builder.setSmallIcon(R.mipmap.ic_launcher);

//                    .addParams("userId",Config.userid)
//                            .addParams("feedId",id)
//                            .addParams("token","123")




                    String Url  = Datas.GetInfoUseFeedId +"?userId="+ Config.userid+"&feedId="+feedId + "&token=123";
                    URL url = new URL(Url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection
                            .getInputStream()));
                    String line = null;
                    String result = "";
                    try {
                        while ((line = reader.readLine()) != null) {
                            result += line ;
                        }
                    } finally {
                        reader.close();
                    }


                    Log.e("JsonParse","result:"+result);
                    MainMessageParse mainMessageParse = new MainMessageParse();
                    MainMessageBean mainMessageBeen = mainMessageParse.Parse(result);
                    mainMessageBeen.setFeedId(feedId);
                    Intent intent = new Intent(MyApplication.getContext(), FeedActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("first",mainMessageBeen);
                    intent.putExtras(bundle);

                    PendingIntent pi = PendingIntent.getActivity(MyApplication.getContext(), 0, intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);

                    if(type1.equals("hf")){
                        builder.setContentTitle("您收到一条回复");
                    } else if (type1.equals("at")) {
                        builder.setContentTitle("有人@你啦");
                    }else if(type1.equals("tz")){
                        builder.setContentTitle("有条推送请您查收");
                    }
                    if(message1!=null){
                        builder.setContentText(message1);
                    }
                    builder.setContentIntent(pi);
                    builder.setAutoCancel(true);
                    Notification notification = builder.build();

                    NotificationManager manager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(1200, notification);
                    long[] pattern = {0,100,50,100};
                    VibratorUtil.Vibrate(pattern,false);
                    tag = true;
                }
            }
            if(tag==true){
                MyApplication.getHandlers("messageActivity").sendEmptyMessage(0);
                Log.e("JsonParse","getmsg");
                SocketConnect.Instance().ExecuteTaskByCustomMessage("{tag:\"getmsg\"}");
                SocketConnect.Instance().SetTimer();
                tag = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void Notification(String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("有人私信你啦");
        builder.setContentText(msg);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1200, notification);
        long[] pattern = {0,100,50,100};
        VibratorUtil.Vibrate(pattern,false);
    }

    //    String msg;
//    String time;
//    String from;
//    String type;
    private void UpdateDatabase(MessageBean bean) {
        SQLiteDatabase database =  databaseHelper.getWritableDatabase();

        //插入message表项
        database.execSQL("INSERT INTO message (time,message,username,left,right,type)" +
                "values(?,?,?,?,?,?)",new Object[]{bean.getTime(),bean.getMsg(),bean.getFrom(),1,0,bean.getType()});

        //更新messagelist列表项
        Cursor cursor = database.rawQuery("SELECT COUNT(id) FROM messagelist WHERE username=?",new String[]{bean.getFrom()});
        if(cursor.moveToFirst()){
           int count =  cursor.getInt(0);
           if(count==0){
               database.execSQL("INSERT INTO messagelist (userhead,username,lastmessage,count,nickname) " +
                       "values(?,?,?,?,?)",new Object[]{"head",bean.getFrom(),bean.getMsg(),1,"NULL"});
           }else{
                database.execSQL("UPDATE messagelist SET lastmessage=?,count = count + 1 WHERE username=?",new Object[]{bean.getMsg(),bean.getFrom()});
            }
        }
        cursor.close();
    }
}
