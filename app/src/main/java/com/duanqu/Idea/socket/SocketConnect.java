package com.duanqu.Idea.socket;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.duanqu.Idea.Config;
import com.duanqu.Idea.activity.ChatActivity;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.utils.JsonParse;
import com.duanqu.Idea.utils.ThreadManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/22.
 */
public class SocketConnect {
    Context context = MyApplication.getContext();
    SharedPreferences sharedPreferences = context.getSharedPreferences("pnServer", context.MODE_PRIVATE);
    List<Runnable> tasks;
    BufferedReader reader = null;
    BufferedWriter writer = null;
    Socket socket = null;
    String Token = null;
    String Host;
    int port;
    JsonParse mJsonParse;
    boolean Getreceive = false;
    public Timer TimeOut = null;
    public Timer connectTimeOut = null;
    int waiting = 0;
    final private static SocketConnect instance = new SocketConnect();


    public static SocketConnect Instance() {
        return instance;
    }

    private SocketConnect() {
        //TimeOut = new Timer();
        tasks = new LinkedList<>();
        mJsonParse = new JsonParse();
        Host = sharedPreferences.getString("host", "115.159.159.65");
        port = sharedPreferences.getInt("port", 5222);
        Token = Config.Token;
    }

    //设置超时重传
    public void SetTimer() {
        TimeOut = new Timer();
        final LinkedList<Runnable> task = (LinkedList<Runnable>) tasks;
        TimeOut.schedule(new TimerTask() {
            @Override
            public void run() {
                if (tasks.size() != 0) {
                    //先结束之前的线程，然后执行新的
                    ThreadManager.ShutDownNow();
                    ThreadManager.StubmitTask(task.getFirst());
                }
            }
        }, 5000);
    }

    //超时重传的timer
    public void SetTimer(int i) {
        connectTimeOut = new Timer();
        connectTimeOut.schedule(new TimerTask() {
            @Override
            public void run() {
                if (tasks.size() != 0) {
                    //先结束之前的线程，然后执行新的
                    try {
                        Thread.sleep((long) waiting() * 1000L);
                        ThreadManager.ConnectTask(getTask());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, 5000);
    }




    public void connect() {
        try {
            Log.e("Connect","正在连接");
            socket = new Socket(Host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //{tag:"request",request:"198642cb7d36fb8cb6a75eab8ff720a1"}
            //发送登陆请求
            String Login = "{tag:\"request\",request:\"" + Token + "\"}";
            writer.write(Login + (char)0);
            writer.flush();
            startServerReplyListener(reader);
            StartHeartbeat(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void AddTask(Runnable r) {
        tasks.add(r);
    }


    public void ExecuteTaskByCustomMessage(final String message) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                int temp= 0;
                try {
                    if(writer==null){
                        Log.e("error","未建立连接");
                        if(ChatActivity.datas.size()!=0){
                            ChatActivity.datas.get(ChatActivity.datas.size()-1).setTag(1);
                            //ChatActivity.handler.sendEmptyMessage(0);
                        }
                        PopTask();
                        return;
                    }
                    writer.write(message + (char)0);
                    writer.flush();
                    ChatActivity.datas.get(ChatActivity.datas.size()-1).setTag(2);
                    //ChatActivity.handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    Log.e("error","发送错误");
                    PopTask();
                    e.printStackTrace();
                    if(ChatActivity.datas.size()!=0){
                        ChatActivity.datas.get(ChatActivity.datas.size()-1).setTag(1);
                        //ChatActivity.handler.sendEmptyMessage(0);
                    }

                }
            }
        };
        AddTask(task);
        ThreadManager.StubmitTask(task);
    }


    //获得任务队列里任务
    public Runnable getTask() {
        LinkedList<Runnable> task = (LinkedList<Runnable>) tasks;
        if (task.size() == 0) {
            return null;
        }
        return task.getFirst();
    }

    public Runnable PopTask() {
        LinkedList<Runnable> task = (LinkedList<Runnable>) tasks;
        Runnable r = task.getFirst();
        task.removeFirst();
        return r;
    }


    //超时重传
    public void ReconnectionThread() {
        try {
            Log.e("xxx","正在重新连接");
            waiting++;
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void StartHeartbeat(final BufferedWriter writer){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("SocketConnect","开始发送心跳包");
                String heart = "{tag:\"heart\"}";
                while (true){
                    try {
                        Thread.sleep(1000*60);
                        writer.write(heart + (char)0);
                        writer.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Reconnect();

                    }
                }
            }
        }).start();
    }
    public void startServerReplyListener(final BufferedReader reader) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("SocketConnect", "启动监听");
                    String response;
                    StringBuffer buffer = new StringBuffer();
                    while ((response = reader.readLine()) != null) {
                        buffer.append(response);
                        if ((int) response.charAt(response.length() - 1) == 0) {
                            mJsonParse.Parse(buffer.toString());
                            //Log.e("socketconnect",buffer.toString());
                            //一发一响应
                            Getreceive = true;
                            buffer = new StringBuffer();
                        }
                    }
                } catch (Exception e) {
                    Reconnect();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void Reconnect() {
        waiting = 0;
        AddTask(new Runnable() {
            @Override
            public void run() {
                ReconnectionThread();
            }
        });
        ThreadManager.StubmitTask(getTask());
        SetTimer(1);
    }

    private int waiting() {
        if (waiting > 20) {
            return 600;
        }
        if (waiting > 13) {
            return 300;
        }
        return waiting <= 7 ? 10 : 60;
    }


}
