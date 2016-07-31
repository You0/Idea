package com.duanqu.Idea.Adapter;

import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.volokh.danylo.video_player_manager.manager.VideoItem;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.CurrentItemMetaData;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.items.ListItem;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 基本视频项, 实现适配项和列表项
 * <p/>
 * Created by wangchenlong on 16/1/27.
 */
public abstract class VideoListItem implements VideoItem, ListItem {

    private final Rect mCurrentViewRect; // 当前视图的方框
    private final VideoPlayerManager<MetaData> mVideoPlayerManager; // 视频播放管理器
    private final String mTitle; // 标题
    private final String CoverImageUrl; // 图片资源
    private final int radio; //视频比例 4:3 还是16:9
    public static int percent;
    private AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);

    private static ExecutorService SinglePool = Executors.newFixedThreadPool(1);
    private VideoWatchAdapter.VideoViewHolder videoViewHolder;
    private Handler handler = new Handler();

    // 构造器, 输入视频播放管理器
    public VideoListItem(
            VideoPlayerManager<MetaData> videoPlayerManager,
            String title,
            String imageResource,int radio) {
        mVideoPlayerManager = videoPlayerManager;
        mTitle = title;
        CoverImageUrl = imageResource;
        this.radio = radio;
        mCurrentViewRect = new Rect();
    }

    // 视频项的标题
    public String getTitle() {
        return mTitle;
    }

    public String getCoverImageUrl() {
        return CoverImageUrl;
    }

    // 显示可视的百分比程度
    @Override
    public int getVisibilityPercents(View view) {
        int percents = 100;

        view.getLocalVisibleRect(mCurrentViewRect);
        int height = view.getHeight();

        if (viewIsPartiallyHiddenTop()) {
            percents = (height - mCurrentViewRect.top) * 100 / height;
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height;
        }
        VideoWatchAdapter.VideoViewHolder viewHolder =
                (VideoWatchAdapter.VideoViewHolder) view.getTag();
        if(percents<60){
            if(viewHolder.getBlack_cover().getVisibility()==View.GONE){
                viewHolder.setBlack_cover();
                viewHolder.BeforePlayView();
                mVideoPlayerManager.resetMediaPlayer();
            }
        }
        //System.out.println("pp"+viewHolder.Position()+"percent"+percents);
        percent = percents;
        return percents;
    }

    public static int getPercent() {
        return percent;
    }

    public int getRadio() {
        return radio;
    }

    @Override
    public void setActive(View newActiveView, int newActiveViewPosition) {
        final VideoWatchAdapter.VideoViewHolder viewHolder =
                (VideoWatchAdapter.VideoViewHolder) newActiveView.getTag();


        if(viewHolder.getBlack_cover().getVisibility()!=View.GONE){
            viewHolder.setBlack_coverGone();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if(viewHolder.getBlack_cover().getVisibility()!=View.GONE){
                   viewHolder.getBlack_cover().setVisibility(View.GONE);
               }
            }
        },300);

       // viewHolder.getBlack_cover().setAnimation(alphaAnimation);
////        playNewVideo(new CurrentItemMetaData(newActiveViewPosition, newActiveView),
////                viewHolder.getVpvPlayer(), mVideoPlayerManager);



        System.out.println("this is active view" + newActiveViewPosition);
    }

    public void start(View newActiveView, int newActiveViewPosition)
    {
        Log.e("start","reset");
        mVideoPlayerManager.resetMediaPlayer();
        SinglePool.shutdownNow();
        SinglePool = Executors.newFixedThreadPool(1);
        VideoWatchAdapter.VideoViewHolder vh = (VideoWatchAdapter.VideoViewHolder)newActiveView.getTag();
        playNewVideo(new CurrentItemMetaData(newActiveViewPosition, newActiveView),
                vh.getVpvPlayer(), mVideoPlayerManager);
        videoViewHolder = vh;
        SinglePool.execute(new BuffCheckTask());

    }

    //这个是在同时有2个active时调用的。
    @Override
    public void deactivate(View currentView, int position) {
        stopPlayback(mVideoPlayerManager);
        VideoWatchAdapter.VideoViewHolder vh = (VideoWatchAdapter.VideoViewHolder)currentView.getTag();
        vh.BeforePlayView();
        //System.out.println("一杀掉另一个播放的");
    }




    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

    // 顶部出现
    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }

    // 底部出现
    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }




    class BuffCheckTask implements Runnable {
        @Override
        public void run() {
            while (true){
                //System.out.println("是否在缓冲"+videoViewHolder.getVpvPlayer().getBuffering());;
            }
        }
    }




}