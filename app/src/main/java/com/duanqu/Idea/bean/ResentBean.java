package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/6/30.
 */
public class ResentBean {
    public String userId;
    public String NickName;
    public String[] images;
    public String VedioUri;

    public String getNickName() {
        return NickName;
    }

    public String getUserId() {
        return userId;
    }

    public String getVedioUri() {
        return VedioUri;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVedioUri(String vedioUri) {
        VedioUri = vedioUri;
    }
}

