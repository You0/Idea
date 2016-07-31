package com.duanqu.Idea.Adapter;

import android.support.annotation.DrawableRes;

import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

/**
 * Created by Administrator on 2016/7/28.
 */
public class OnlineVideoListItem extends VideoListItem {

    private final String mOnlineUrl; // 资源文件描述
    private final int radio;
    public OnlineVideoListItem(
            VideoPlayerManager<MetaData> videoPlayerManager,
            String title,
            String imageResource,
            String onlineUrl,
            int radio
    ) {
        super(videoPlayerManager, title, imageResource,radio);
        this.radio = radio;
        mOnlineUrl = onlineUrl;
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager) {
        videoPlayerManager.playNewVideo(currentItemMetaData, player, mOnlineUrl);
    }
}
