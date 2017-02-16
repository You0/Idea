package com.duanqu.Idea.bean;

/**
 * Created by Me on 2017/1/24.
 */

public class HistoryAndCacheBean {
    private String feedId;
    private String media;
    private String content;
    private String answer;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMedia() {
        return media;
    }
}
