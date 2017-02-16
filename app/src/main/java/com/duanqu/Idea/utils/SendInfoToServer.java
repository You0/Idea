package com.duanqu.Idea.utils;

import android.util.Log;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.bean.MyMessageBean;
import com.duanqu.Idea.test.Datas;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Me on 2017/1/23.
 */

public class SendInfoToServer {

    public static void insertHistory(MainMessageBean bean) {
        String feedId = String.valueOf(bean.getMessageInfo().get("id"));
        String content = bean.getTextContent();
        String media = "";
        String video = bean.getVideoUri();
        media = getMedia(bean, video);
        media = MyApplication.Encode(media);
        content = MyApplication.Encode(content);
        insertHistory(feedId,media,content);
    }

    private static String getMedia(MainMessageBean bean, String video) {
        String media = "";
        if (video == null || video.equals("null")) {
            LinkedList images = bean.getImages();
            if (images != null) {
                for (int i = 0; i < images.size(); i++) {
                    media += "[图片] ";
                }
            }

        } else {
            media = "[视频]";
        }
        Log.e("media",media);
        return media;
    }


    public static void insertHistory(String feedId, String media, String content) {
        OkHttpUtils.post().url(Datas.insertHistory)
                .addParams("Token", Config.Token)
                .addParams("username", Config.username)
                .addParams("feedId", feedId)
                .addParams("media", media)
                .addParams("content", content)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {

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

    //Token=97669b4fabc569f52d21f64f5c3693ef&username=E41414005
    public static void incFeed() {
        OkHttpUtils.post().url(Datas.incFeed)
                .addParams("Token", Config.Token)
                .addParams("username", Config.username)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
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

    public static void cacheAnswer(MainMessageBean bean,String answer) {
        String feedId = String.valueOf(bean.getMessageInfo().get("id"));
        String content = bean.getTextContent();
        String media = "";
        String video = bean.getVideoUri();
        media = getMedia(bean, video);
        media = MyApplication.Encode(media);
        content = MyApplication.Encode(content);
        answer =  MyApplication.Encode(answer);
        cacheAnswer(content,media,answer,feedId);
    }

        public static void cacheAnswer(String content, String media, String answer, String feedId) {
        OkHttpUtils.post().url(Datas.cacheAnswer)
                .addParams("content", content)
                .addParams("media", media)
                .addParams("username", Config.username)
                .addParams("answer", answer)
                .addParams("Token", Config.Token)
                .addParams("feedId", feedId)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
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

    public static void insertCache(MainMessageBean bean)
    {
        String feedId = String.valueOf(bean.getMessageInfo().get("id"));
        String content = bean.getTextContent();
        String media = "";
        String video = bean.getVideoUri();
        media = getMedia(bean, video);
        media = MyApplication.Encode(media);
        content = MyApplication.Encode(content);
        insertCache(feedId,media,content);
    }

    public static void insertCache(String feedId,String media,String content)
    {
        OkHttpUtils.post().url(Datas.insertCache)
                .addParams("content", content)
                .addParams("media", media)
                .addParams("username", Config.username)
                .addParams("Token", Config.Token)
                .addParams("feedId", feedId).build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
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


    public static void removeContact(String uid)
    {
        OkHttpUtils.post().url(Datas.removeContact)
                .addParams("Token",Config.Token)
                .addParams("username",Config.username)
                .addParams("uid",uid)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
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

    public static void addContact(String uid)
    {
        OkHttpUtils.post().url(Datas.addContact)
                .addParams("Token",Config.Token)
                .addParams("username",Config.username)
                .addParams("uid",uid)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {

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

    public static void cacelCache(String feedId)
    {
        OkHttpUtils.post().url(Datas.cacelCache)
                .addParams("Token",Config.Token)
                .addParams("username",Config.username)
                .addParams("feedId",feedId)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {

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






}
