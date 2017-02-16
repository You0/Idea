package com.duanqu.Idea.bean;

import java.util.ArrayList;

/**
 * Created by Me on 2017/1/22.
 */

public class CommentBean {
    private String feedId;
    private String ownerId;
    private String time;
    private String content;
    private String imageTag;
    private String videoTag;
    private String videourl;
    private int type;
    private ArrayList<String> images;


    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
        if (videourl != null && !videourl.equals("")) {
            videoTag = "[视频]";
        } else {
            videoTag = null;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                imageTag += "[图片] ";
            }
        } else {
            imageTag = null;
        }
    }
}
