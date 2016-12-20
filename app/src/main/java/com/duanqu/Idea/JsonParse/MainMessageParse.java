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
            messageInfo = object.getJSONObject("messageInfo");
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
