package com.duanqu.Idea.JsonParse;

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
            messageInfo = object.getJSONObject("MessageInfo");
            content = object.getString("content");
            imgs = object.getJSONArray("imgs");
            video = object.getString("video");
            resendInfo = object.getJSONObject("resendInfo");
            linkedList = new LinkedList<>();
            for(int i = 0; i< imgs.length(); i++)
            {
                linkedList.add(imgs.getString(i));
            }

            HashMap<String,String> UserInfo = new HashMap<>();
            UserInfo.put("nickname",userInfo.getString("nickname"));
            UserInfo.put("headurl",userInfo.getString("headurl"));
            UserInfo.put("sign",userInfo.getString("sign"));
            UserInfo.put("id",userInfo.getString("id"));

            mainMessageBean.setUserInfo(UserInfo);

            HashMap<String,String> MessageInfo = new HashMap<>();
            MessageInfo.put("id",messageInfo.getString("id"));
            MessageInfo.put("allike",messageInfo.getString("allike"));
            MessageInfo.put("time",messageInfo.getString("time"));
            MessageInfo.put("resent",messageInfo.getString("resent"));
            MessageInfo.put("comment",messageInfo.getString("comment"));
            MessageInfo.put("like",messageInfo.getString("like"));
            MessageInfo.put("see",messageInfo.getString("see"));

            mainMessageBean.setMessageInfo(MessageInfo);

            //这里是转发信息的处理
            mainMessageBean.setReSendInfo(null);

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
