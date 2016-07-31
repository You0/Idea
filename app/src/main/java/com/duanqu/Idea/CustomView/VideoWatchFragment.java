package com.duanqu.Idea.CustomView;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqu.Idea.Adapter.OnlineVideoListItem;
import com.duanqu.Idea.Adapter.VideoListItem;
import com.duanqu.Idea.Adapter.VideoWatchAdapter;
import com.duanqu.Idea.R;
import com.duanqu.Idea.fragment.BaseFragment;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/29.
 */
public class VideoWatchFragment extends BaseFragment{
    private RecyclerView mRecyclerView;
    private List<VideoListItem> mLists = new ArrayList<>();
    private RecyclerViewItemPositionGetter mItemsPositionGetter;
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mLists);

    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {
        }
    });

    private int mScrollState;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    private static final String URL =
            "http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4";

    private static VideoWatchAdapter adapter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_watch_layout,null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.video_watch_list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initData();

        adapter = new VideoWatchAdapter(mLists);
        mRecyclerView.setAdapter(adapter);


        //////////////////////////////////////////////
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;
                if(scrollState == RecyclerView.SCROLL_STATE_IDLE && !mLists.isEmpty()){
                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(!mLists.isEmpty()){
                    mVideoVisibilityCalculator.onScroll(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                            mScrollState);
                }
            }
        });

        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, mRecyclerView);

        /////////////////////////////////////////////




        return view;
    }

    private void initData() {
        //添加视频数据
        mLists.add(new OnlineVideoListItem(mVideoPlayerManager,"[服务器速率100kb/s]Tamas Welss好听的音乐","http://115.159.159.65:8080/EAsy/valder.JPG","http://115.159.159.65:8080/EAsy/valder.mp4",2));
        mLists.add(new OnlineVideoListItem(mVideoPlayerManager, "[服务器速率100kb/s]测试", "http://115.159.159.65:8080/EAsy/testcover.JPG", "http://115.159.159.65:8080/EAsy/test.mp4",1));
        mLists.add(new OnlineVideoListItem(mVideoPlayerManager,"[服务器速率100kb/s]万万没想到！","http://115.159.159.65:8080/EAsy/wwmxd.JPG","http://115.159.159.65:8080/EAsy/wwmxd.mp4",2));
        mLists.add(new OnlineVideoListItem(mVideoPlayerManager, "[服务器速率10MB/s]", "http://115.159.159.65:8080/EAsy/cover.jpg", URL,2));
        mLists.add(new OnlineVideoListItem(mVideoPlayerManager, "[服务器速率100kb/s]测试", "http://115.159.159.65:8080/EAsy/cover2.JPG", "http://115.159.159.65:8080/EAsy/test2.mp4",1));
        //http://115.159.159.65:8080/EAsy/valder.mp4
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!mLists.isEmpty()){
            // need to call this method from list view handler in order to have filled list

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());

                }
            });
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        //mVideoPlayerManager.stopAnyPlayback();
        mVideoPlayerManager.resetMediaPlayer(); // 页面不显示时, 释放播放器
    }


    private static String formatPlayTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }


}
