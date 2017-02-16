package com.duanqu.Idea.JsonParse;

import android.util.Log;

import com.duanqu.Idea.bean.MainMessageBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/2.
 */
public class MainMessageParse extends BaseJsonParse<MainMessageBean> {
    private JSONObject userInfo;
    private JSONObject messageInfo;
    private String content;
    private JSONArray imgs;
    private String video;
    private JSONObject resendInfo;
    private LinkedList<String> linkedList;

    @Override
    public MainMessageBean Parse(String json) {
        MainMessageBean mainMessageBean = null;
        try {
            mainMessageBean = new MainMessageBean();
            object = new JSONObject(json);
            userInfo = object.getJSONObject("userInfo");
            messageInfo = object.getJSONObject("messageInfo");
            resendInfo = object.getJSONObject("resendInfo");
            content = object.getString("content");
            try {
                imgs = object.getJSONArray("imgs");
                linkedList = new LinkedList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    linkedList.add(imgs.getString(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
                linkedList = new LinkedList<>();
            }

            video = object.getString("video");
            resendInfo = object.getJSONObject("resendInfo");



            HashMap<String, Object> UserInfo = new HashMap<>();
            UserInfo.put("nickname", userInfo.getString("nickname"));
            UserInfo.put("headurl", userInfo.getString("headurl"));
            UserInfo.put("sign", userInfo.getString("sign"));
            UserInfo.put("id", userInfo.getInt("id"));

            mainMessageBean.setUserInfo(UserInfo);

            HashMap<String, Object> MessageInfo = new HashMap<>();
            MessageInfo.put("id", messageInfo.getInt("id"));
            MessageInfo.put("allike", messageInfo.getInt("alike"));
            MessageInfo.put("time", messageInfo.getString("time"));
            MessageInfo.put("resent", messageInfo.getInt("resent"));
            MessageInfo.put("comment", messageInfo.getInt("comment"));
            MessageInfo.put("like", messageInfo.getInt("like"));
            MessageInfo.put("see", messageInfo.getInt("see"));

            mainMessageBean.setMessageInfo(MessageInfo);


            //这里处理转发信息，为了图简单直接当做普通信息处理就行。
            HashMap<String, Object> ResendInfo = new HashMap<>();
            resendInfo.put("video",resendInfo.get("video"));
            resendInfo.put("nick",resendInfo.get("nick"));
            resendInfo.put("uid",resendInfo.get("uid"));
            resendInfo.put("imgs",resendInfo.get("imgs"));

            try{
                JSONArray array = resendInfo.getJSONArray("imgs");

                if(array.length()>0)
                {
                    //Log.e("转发不为null",array.toString());
                    imgs = resendInfo.getJSONArray("imgs");
                    linkedList.clear();
                    for (int i = 0; i < imgs.length(); i++) {
                        linkedList.add(imgs.getString(i));
                    }
                    video = (String) resendInfo.get("video");
                }
            }catch (Exception e){
                e.printStackTrace();
            }



            //这里是转发信息的处理
            mainMessageBean.setReSendInfo(ResendInfo);

            mainMessageBean.setImages(linkedList);
            mainMessageBean.setTextContent(content);
            mainMessageBean.setVideoUri(video);
            mainMessageBean.setType(mainMessageBean.DEAUFALT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainMessageBean;
    }
}
