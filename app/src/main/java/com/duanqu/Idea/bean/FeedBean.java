package com.duanqu.Idea.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FeedBean {
    public final int DEAUFALT = 0;
    public HashMap<String, String> userInfo;
    public HashMap<String, String> MessageInfo;
    public String TextContent;
    public ResentBean ReSendInfo;
    public String VideoUri;
    public LinkedList images;
    public String feedId;
    public int type;
    public boolean feed = false;
    public ArrayList<InnerFeedBean> innerBeans;

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedId() {
        return feedId;
    }

    public ArrayList<InnerFeedBean> getInnerBeans() {
        return innerBeans;
    }

    public void setInnerBeans(ArrayList<InnerFeedBean> innerBeans) {
        this.innerBeans = innerBeans;
    }

    public int getType() {
        return type;
    }

    public LinkedList getImages() {
        return images;
    }

    public String getVideoUri() {
        return VideoUri;
    }

    public void setVideoUri(String videoUri) {
        VideoUri = videoUri;
    }

    public void setFeed(boolean feed) {
        this.feed = feed;
    }

    public void setImages(LinkedList images) {
        this.images = images;
    }

    public void setMessageInfo(HashMap<String, String> messageInfo) {
        MessageInfo = messageInfo;
    }

    public void setReSendInfo(ResentBean reSendInfo) {
        ReSendInfo = reSendInfo;
    }

    public void setTextContent(String textContent) {
        TextContent = textContent;
    }

    public void setUserInfo(HashMap<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    public ResentBean getReSendInfo() {
        return ReSendInfo;
    }

    public String getTextContent() {
        return TextContent;
    }

    public HashMap<String, String> getMessageInfo() {
        return MessageInfo;
    }

    public HashMap<String, String> getUserInfo() {
        return userInfo;
    }

    public void setType(int type) {
        if (feed == true) {
            this.type = 6;
            return;
        }
        if (type == DEAUFALT) {
            int length = getImages().size();
            if (length == 0) {
                if (getVideoUri().equals("")) {
                    this.type = 5;
                } else {
                    this.type = 4;
                }
            } else if (length == 1) {
                this.type = 0;
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


}
