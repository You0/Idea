package com.duanqu.Idea.Adapter;


import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.VideoWatchActivity;
import com.duanqu.Idea.player.view.MediaController;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.view.SimpleDraweeView;
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper;
import com.volokh.danylo.video_player_manager.ui.ScalableTextureView;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/7/28.
 */
public class VideoWatchAdapter extends RecyclerView.Adapter<VideoWatchAdapter.VideoViewHolder>{

    private final List<VideoListItem> mList; // 视频项列表

    // 构造器
    public VideoWatchAdapter(List<VideoListItem> list) {
        mList = list;
    }

    @Override
    public VideoWatchAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.black_cover, parent, false);
        // 必须要设置Tag, 否则无法显示
        VideoWatchAdapter.VideoViewHolder holder = new VideoWatchAdapter.VideoViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoWatchAdapter.VideoViewHolder holder, int position) {
        VideoListItem videoItem = mList.get(position);
        holder.bindTo(videoItem,position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        VideoPlayerView mVpvPlayer; // 播放控件
        SimpleDraweeView mIvCover; // 覆盖层
        SimpleDraweeView messageHead;//用户头像
        TextView mTvTitle; // 标题
        ImageView mPlayImg;//播放按钮
        SeekBar mProgressSeekBar;//播放进度条
        TextView mTimeTxt;//播放时间
        ImageView play_btn; //播放的图标
        View mProgressBarView;//加载中按钮
        RelativeLayout video_watch_list_rl;
        LinearLayout video_watch_list_ll;
        View black_cover;
        boolean pause = false;
        int position;
        boolean SeekBarVisible = true;


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mVpvPlayer.getCurrentPosition()<mVpvPlayer.getDuration()){
                    setProgressBar(mVpvPlayer.getCurrentPosition(),mVpvPlayer.getDuration());
                    System.out.println("当前播放时间"+formatPlayTime(mVpvPlayer.getCurrentPosition()));
                    setPlayProgressTxt(mVpvPlayer.getCurrentPosition(),mVpvPlayer.getDuration());
                    updatePlayProgress();
                    handler.postDelayed(this,1000);
                }
            }
        };



        private Context mContext;
        private MediaPlayerWrapper.MainThreadMediaPlayerListener mPlayerListener;

        public VideoViewHolder(View itemView) {
            super(itemView);
            //用户
            messageHead = (SimpleDraweeView) itemView.findViewById(R.id.messageHead);

            //

            video_watch_list_ll = (LinearLayout) itemView.findViewById(R.id.seek_bar);
            play_btn = (ImageView) itemView.findViewById(R.id.play_btn);
            mVpvPlayer = (VideoPlayerView) itemView.findViewById(R.id.item_video_vpv_player); // 播放控件
            mTvTitle = (TextView) itemView.findViewById(R.id.item_video_tv_title);
            mIvCover = (SimpleDraweeView) itemView.findViewById(R.id.item_video_iv_cover);
            mPlayImg = (ImageView) itemView.findViewById(R.id.pause);
            mProgressSeekBar = (SeekBar) itemView.findViewById(R.id.media_controller_progress);
            mTimeTxt = (TextView) itemView.findViewById(R.id.time);
            black_cover = itemView.findViewById(R.id.black_cover);


            video_watch_list_rl = (RelativeLayout) itemView.findViewById(R.id.video_watch_list_rl);
            mVpvPlayer.setScaleType(ScalableTextureView.ScaleType.CENTER_CROP);


            //注册监听事件
            mVpvPlayer.setOnClickListener(this);
            mPlayImg.setOnClickListener(this);
            play_btn.setOnClickListener(this);
            mIvCover.setOnClickListener(this);

            //视频未播放的时候,seekbar不显示
            setSeekBarGone();
            mProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                        onProgressTurn(MediaController.ProgressState.DOING, progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            mContext = itemView.getContext().getApplicationContext();
            mPlayerListener = new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
                @Override
                public void onVideoSizeChangedMainThread(int width, int height) {
                }

                @Override
                public void onVideoPreparedMainThread() {
                    //播放准备阶段
                    // 视频播放隐藏前图
                    mVpvPlayer.setScaleType(ScalableTextureView.ScaleType.FILL);
                    mVpvPlayer.setVisibility(View.VISIBLE);
                    play_btn.setVisibility(View.GONE);
                    mIvCover.setVisibility(View.GONE);

                    if(black_cover.getVisibility()==View.GONE){
                        setBlack_coverGone();
                    }

                    //设置seekbar
                    setSeekBarVisible();
                    mTimeTxt.setText(formatPlayTime(mVpvPlayer.getDuration()));
                    setPlayProgressTxt(mVpvPlayer.getCurrentPosition(),mVpvPlayer.getDuration());
                    setPlayState(MediaController.PlayState.PLAY);
                    handler.postDelayed(runnable,1000);

                }

                @Override
                public void onVideoCompletionMainThread() {
                }

                @Override
                public void onErrorMainThread(int what, int extra) {
                }

                @Override
                public void onBufferingUpdateMainThread(int percent) {

                }



                @Override
                public void onVideoStoppedMainThread() {
                    // 视频暂停显示前图
                    BeforePlayView();
                    handler.removeCallbacks(runnable);
                }

                @Override
                public void Buffering(boolean buffering) {
                    if(buffering){
                        System.out.println("正在缓冲！");
                    }else{
                        System.out.println("缓冲结束!");
                    }

                }
            };

            mVpvPlayer.addMediaPlayerListener(mPlayerListener);
        }



        public void bindTo(VideoListItem vli,int position) {
            mTvTitle.setText(vli.getTitle());
            mIvCover.setImageURI(Uri.parse(vli.getCoverImageUrl()));
            ViewGroup.LayoutParams params =  video_watch_list_rl.getLayoutParams();
            messageHead.setImageURI(Uri.parse(Datas.userHead));

            if(vli.getRadio()==1){
                params.height=720;
            }else{
                params.height=405;
            }

            this.position = position;

        }


        public int Position() {
            return position;
        }

        private void setSeekBarGone()
        {
            video_watch_list_ll.setVisibility(View.GONE);
        }

        private void setSeekBarVisible()
        {
//            mPlayImg.setVisibility(View.VISIBLE);
//            mProgressSeekBar.setVisibility(View.VISIBLE);
//            mTimeTxt.setVisibility(View.VISIBLE);
            video_watch_list_ll.setVisibility(View.VISIBLE);

        }

        public View getBlack_cover() {
            return black_cover;
        }
        public void setBlack_coverGone(){
            Animation alphaAnimation = new AlphaAnimation(1,0);
            alphaAnimation.setDuration(200);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getBlack_cover().setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

            black_cover.startAnimation(alphaAnimation);

            //black_cover.setVisibility(View.GONE);
        }

        public void setBlack_cover(){
            Animation alphaAnimation = new AlphaAnimation(0,1);
            alphaAnimation.setDuration(200);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    getBlack_cover().setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            black_cover.startAnimation(alphaAnimation);
        }



        // 返回播放器
        public VideoPlayerView getVpvPlayer() {
            return mVpvPlayer;
        }

        private String formatPlayTime(long time) {
            DateFormat formatter = new SimpleDateFormat("mm:ss");
            return formatter.format(new Date(time));
        }

        public void setProgressBar(int progress, int secondProgress) {
            if (progress < 0) progress = 0;
            if (progress > 100) progress = 100;
            if (secondProgress < 0) secondProgress = 0;
            if (secondProgress > 100) secondProgress = 100;
            System.out.println("SEEKBAR PROGRRESS"+progress + "  "+ secondProgress);
            mProgressSeekBar.setProgress(progress);
            mProgressSeekBar.setSecondaryProgress(secondProgress);
        }

        public void setPlayState(MediaController.PlayState playState) {
            mPlayImg.setImageResource(playState.equals(MediaController.PlayState.PLAY) ? R.drawable.biz_video_pause : R.drawable.biz_video_play);
        }

        public void setPlayProgressTxt(int nowSecond, int allSecond) {
            mTimeTxt.setText(getPlayTime(nowSecond, allSecond));
        }

        private String getPlayTime(int playSecond, int allSecond) {
            String playSecondStr = "00:00";
            String allSecondStr = "00:00";
            if (playSecond > 0) {
                playSecondStr = formatPlayTime(playSecond);
            }
            if (allSecond > 0) {
                allSecondStr = formatPlayTime(allSecond);
            }
            return playSecondStr + "/" + allSecondStr;
        }


        private void updatePlayProgress() {
            int allTime = mVpvPlayer.getDuration();
            int playTime = mVpvPlayer.getCurrentPosition();
            int progress = playTime * 100 / allTime;
            setProgressBar(progress, allTime);
        }

        public void onProgressTurn(MediaController.ProgressState state, int progress) {
                mVpvPlayer.SeekTo(progress);
                updatePlayTime();
            }

        private void updatePlayTime() {
            int allTime = mVpvPlayer.getDuration();
            int playTime = mVpvPlayer.getCurrentPosition();
            setPlayProgressTxt(playTime, allTime);
        }

        public void BeforePlayView()
        {
            setSeekBarGone();
            play_btn.setVisibility(View.VISIBLE);
            mIvCover.setVisibility(View.VISIBLE);
            mVpvPlayer.setVisibility(View.GONE);
        }






        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pause:{
                    if(pause){
                        setPlayState(MediaController.PlayState.PLAY);
                        pause = false;
                        mVpvPlayer.start();
                    }else{
                        setPlayState(MediaController.PlayState.PAUSE);
                        mVpvPlayer.pause();
                        pause = true;
                    }

                    break;
                }

                case R.id.item_video_iv_cover:
                case R.id.play_btn:{
                    play_btn.setVisibility(View.GONE);
                   // mIvCover.setVisibility(View.GONE);
                    mList.get(position).start(itemView,position);
                    break;
                }


                //点击视频的时候隐藏或显示SeekBar
                case R.id.item_video_vpv_player:{
                    if(SeekBarVisible){
                        Animation animation = AnimationUtils.loadAnimation(mContext,
                                R.anim.anim_exit_from_bottom);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                video_watch_list_ll.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        video_watch_list_ll.startAnimation(animation);
                        SeekBarVisible = false;

                    }else{
                        video_watch_list_ll.setVisibility(View.VISIBLE);
                        video_watch_list_ll.clearAnimation();
                        Animation animation = AnimationUtils.loadAnimation(mContext,
                                R.anim.anim_enter_from_bottom);
                        video_watch_list_ll.startAnimation(animation);
                        SeekBarVisible = true;

                    }
                    break;
                }


            }

        }
    }



}
