package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/11/30.
 */
public class InnerFeedBean {
    private String leftusername;
    private String rightusername;
    private String content;
    private String FeedId;

    public void setContent(String content) {
        this.content = content;
    }

    public void setFeedId(String feedId) {
        FeedId = feedId;
    }

    public String getFeedId() {
        return FeedId;
    }

    public void setLeftusername(String leftusername) {
        this.leftusername = leftusername;
    }

    public void setRightusername(String rightusername) {
        this.rightusername = rightusername;
    }

    public String getContent() {
        return content;
    }

    public String getLeftusername() {
        return leftusername;
    }

    public String getRightusername() {
        return rightusername;
    }
}
