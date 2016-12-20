package com.duanqu.Idea.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.duanqu.Idea.socket.SocketConnect;
import com.duanqu.Idea.utils.ThreadManager;


public class PnService extends Service {

    private static String SERVICE_NAME = "com.ahu.forwisdom.wechat.service.PnService";


    SocketConnect mSocketConnect = SocketConnect.Instance();
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent getIntent()
    {
        return new Intent(SERVICE_NAME);
    }

    @Override
    //创建时调用
    public void onCreate() {
        //摆脱主线程，在子线程里进行连接任务
        Log.e("pnService","serviceStart");
        //加入断线重连任务
        mSocketConnect.AddTask(new Runnable() {
            @Override
            public void run() {
                mSocketConnect.ReconnectionThread();
            }
        });

        //直接连接
        ThreadManager.ConnectTask(new Runnable() {
            @Override
            public void run() {
                mSocketConnect.connect();
            }
        });

        super.onCreate();
    }

    @Override
    //服务每次启动时调用
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
