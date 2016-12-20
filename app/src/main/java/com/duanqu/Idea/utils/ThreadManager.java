package com.duanqu.Idea.utils;

import android.util.Log;


import com.duanqu.Idea.socket.SocketConnect;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/11/22.
 */
public class ThreadManager {
    //单线程池
    static ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    static ExecutorService connectSingerService = Executors.newFixedThreadPool(1);

    public static void ConnectTask(Runnable runnable)
    {
        Log.e("ThreadManager","connectTask");
        try{
            if(connectSingerService.isShutdown()==false){
                connectSingerService.shutdownNow();
                connectSingerService = Executors.newFixedThreadPool(1);
            }
            connectSingerService.submit(runnable);
            SocketConnect.Instance().SetTimer(1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void StubmitTask(Runnable runnable){
        Log.e("ThreadManager","提交任务");
        mExecutorService.submit(runnable);
    }


    public static void ShutDownNow()
    {
        mExecutorService.shutdownNow();
        mExecutorService = Executors.newFixedThreadPool(1);
    }
}
