package com.duanqu.Idea.bean;

import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/6/30.
 */
public class MainMessageBean implements Serializable{
    public final int DEAUFALT = 0;
    public HashMap<String, Object> userInfo;
    public HashMap<String, Object> MessageInfo;
    public String TextContent;
    public HashMap<String, Object> ReSendInfo;
    public String VideoUri;
    public LinkedList images;
    public String FeedId;
    public int type = 0;
    public boolean feed = false;
    public ArrayList<InnerFeedBean> innerBeans;

    public void setFeedId(String feedId) {
        FeedId = feedId;
    }

    public String getFeedId() {
        return FeedId;
    }

    public void setFeed(boolean feed) {
        this.feed = feed;
    }

    public void setInnerBeans(ArrayList<InnerFeedBean> innerBeans) {
        this.innerBeans = innerBeans;
    }

    public ArrayList<InnerFeedBean> getInnerBeans() {
        return innerBeans;
    }

    public int getType() {
        return type;
    }

    public LinkedList getImages() {
        return images;
    }

    public HashMap<String, Object> getMessageInfo() {
        return MessageInfo;
    }

    public HashMap<String, Object> getUserInfo() {
        return userInfo;
    }

    public HashMap<String, Object> getReSendInfo() {
        return ReSendInfo;
    }

    public String getTextContent() {
        return TextContent;
    }

    public String getVideoUri() {
        return VideoUri;
    }

    public void setImages(LinkedList images) {
        this.images = images;
    }

    public void setType(int type) {
        if(feed==true){
            this.type = 6;
            return;
        }

        if (type == DEAUFALT) {
            int length = getImages().size();

            if(length==0) {
                this.type = 5;
            }else if (length == 1) {
                Log.e("mainmessage",getVideoUri());
                if(!getVideoUri().equals("null")){
                    this.type = 4;
                }else{
                    this.type = 0;
                }

            } else if (length <= 3) {
                this.type = 1;
            } else if (length == 4) {
                this.type = 2;
            } else if (length > 4) {
                this.type = 3;
            }
        } else {
            this.type = type;
        }
    }


    public void setReSendInfo(HashMap<String, Object> reSendInfo) {
        ReSendInfo = reSendInfo;
    }

    public void setMessageInfo(HashMap<String, Object> messageInfo) {
        MessageInfo = messageInfo;
    }

    public void setTextContent(String textContent) {
        TextContent = textContent;
    }

    public void setUserInfo(HashMap<String, Object> userInfo) {
        this.userInfo = userInfo;
    }

    public void setVideoUri(String videoUri) {
        VideoUri = videoUri;
    }

    @Override
    public String toString() {
        return "MainMessageBean{" +
                "DEAUFALT=" + DEAUFALT +
                ", userInfo=" + userInfo +
                ", MessageInfo=" + MessageInfo +
                ", TextContent='" + TextContent + '\'' +
                ", ReSendInfo=" + ReSendInfo +
                ", VideoUri='" + VideoUri + '\'' +
                ", images=" + images +
                ", FeedId='" + FeedId + '\'' +
                ", type=" + type +
                ", feed=" + feed +
                ", innerBeans=" + innerBeans +
                '}';
    }
}
