package com.duanqu.Idea.bean;

/**
 * Created by Administrator on 2016/8/10.
 */
public class VideoInfo  {
    String videoUrl;
    String Cover_Url;
    String title;
    String content;
    int scale;//比例

    public VideoInfo(String videoUrl, String cover_Url, String title, String content, int scale) {
        this.videoUrl = videoUrl;
        Cover_Url = cover_Url;
        this.title = title;
        this.content = content;
        this.scale = scale;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCover_Url() {
        return Cover_Url;
    }

    public void setCover_Url(String cover_Url) {
        Cover_Url = cover_Url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
