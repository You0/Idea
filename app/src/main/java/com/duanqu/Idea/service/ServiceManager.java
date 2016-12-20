package com.duanqu.Idea.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by Administrator on 2016/11/22.
 */
public class ServiceManager {
    Context context;


    public ServiceManager(Context context){
        this.context = context;
    }

    public void startService() {
        Thread serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("serviceManager","启动服务");
                Intent intent1 = new Intent(context,PnService.class);
                context.startService(intent1);
            }
        });
        serviceThread.start();
    }

}
