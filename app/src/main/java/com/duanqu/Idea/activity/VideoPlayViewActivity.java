package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.VideoPlayView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.VideoInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinxin on 16/4/9.
 */
public class VideoPlayViewActivity extends AppCompatActivity implements VideoPlayView.MediaPlayerImpl, AbsListView.OnScrollListener {
    private Toolbar toolbar;
    private ListView videoList;
    private MyAdapter adapter;
    private VideoPlayView playView;
    private View currentItemView;
    private int currentPosition = -1;
    private int scrollDistance;// 记录切换到横屏时滑动的距离
    private List<VideoInfo> path;
    private boolean isPlaying;
    private int firstVisiblePosition;
    private LinearLayout linearLayout;

    private boolean isAnimtion = false;

    private int VisibleCount = -1;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();


        Log.i("XX", "从心开始了");
    }

    private void initView() {
        setContentView(R.layout.video_watch_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("更多视频");
        linearLayout = (LinearLayout) findViewById(R.id.main_ll);

        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Config.WIDTH = linearLayout.getWidth();
                Config.HEIGHT = linearLayout.getHeight();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        videoList = (ListView) findViewById(R.id.video_watch_list);
    }

    private void initData() {
        adapter = new MyAdapter(this);
        videoList.setAdapter(adapter);
        videoList.setOnScrollListener(this);
        path = new ArrayList<VideoInfo>();

        path.add(new VideoInfo("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4",
                "http://115.159.159.65:8080/EAsy/cover.jpg", "[服务器速率10MB/s]", "", 2));

        path.add(new VideoInfo("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4",
                "http://115.159.159.65:8080/EAsy/cover.jpg", "[服务器速率10MB/s]", "", 2));


        path.add(new VideoInfo("http://115.159.159.65:8080/EAsy/test2.mp4",
                "http://115.159.159.65:8080/EAsy/cover2.JPG","5:4","",1));

        path.add(new VideoInfo("http://115.159.159.65:8080/EAsy/valder.mp4",
                "http://115.159.159.65:8080/EAsy/valder.JPG","16:9","",2));


        path.add(new VideoInfo("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4",
                "http://115.159.159.65:8080/EAsy/cover.jpg", "[服务器速率10MB/s]", "", 2));
        path.add(new VideoInfo("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4",
                "http://115.159.159.65:8080/EAsy/cover.jpg", "[服务器速率10MB/s]", "", 2));

        path.add(new VideoInfo("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4",
                "http://115.159.159.65:8080/EAsy/cover.jpg", "[服务器速率10MB/s]", "", 2));

        path.add(new VideoInfo("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4",
                "http://115.159.159.65:8080/EAsy/cover.jpg", "[服务器速率10MB/s]", "", 2));

        path.add(new VideoInfo("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4",
                "http://115.159.159.65:8080/EAsy/cover.jpg", "[服务器速率10MB/s]", "", 2));
        adapter.setData(path);
    }


    //    //通过 set 方法在适配器中拿到这三个参数
    public void setPlayView(VideoPlayView playView) {
        this.playView = playView;
        playView.setMediaPlayerListenr(this);
        Log.i("XX", currentPosition + "");
        int[] curr = new int[2];
        currentItemView.getLocationOnScreen(curr);
        Log.i("TAG", curr[1] + "");
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i("EE", "当前" + currentPosition);
        Log.i("EE", "可见的第一个" + videoList.getFirstVisiblePosition());

        if(videoList.getChildAt(0)==null){
            return;
        }

        if(getVisibilityPercents(videoList.getChildAt(0))<=80)
        {
            int next =1;
            final MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) videoList.getChildAt(0).getTag();
            final MyAdapter.ViewHolder viewHolder1 = (MyAdapter.ViewHolder) videoList.getChildAt(next).getTag();

            if (viewHolder.black_cover.getVisibility() == View.GONE && isAnimtion==false) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.setBlack_cover();
                        //closeVideo();

                    }
                }, 1);
            }

            if(viewHolder1.black_cover.getVisibility()!=View.GONE && isAnimtion==false){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder1.setBlack_coverGone();
                    }
                }, 1);
            }
        }else{
            int next =1;
            final MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) videoList.getChildAt(0).getTag();
            final MyAdapter.ViewHolder viewHolder1 = (MyAdapter.ViewHolder) videoList.getChildAt(next).getTag();

            if (viewHolder.black_cover.getVisibility() != View.GONE && isAnimtion==false) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.setBlack_coverGone();
                    }
                }, 1);
            }

            if(viewHolder1.black_cover.getVisibility()==View.GONE && isAnimtion==false){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder1.setBlack_cover();
                        //closeVideo();
                    }
                }, 1);
            }

        }



        //videoList.getChildAt(0);
        System.out.println("百分比" + videoList.getFirstVisiblePosition() + ":" + getVisibilityPercents(videoList.getChildAt(0)));
        //划出屏幕时关闭
        System.out.println("百分比+bottom" + getVisibilityPercents(videoList.getChildAt(visibleItemCount - 1)));

        if ((currentPosition < videoList.getFirstVisiblePosition() || currentPosition > videoList
                .getLastVisiblePosition()) && isPlaying && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            closeVideo();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playView != null) {
            playView.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playView != null) {
            playView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playView != null) {
            playView.stop();
        }
    }

    private void closeVideo() {
        currentPosition = -1;
        isPlaying = false;
        playView.stop();
        adapter.notifyDataSetChanged();
        playView = null;
        currentItemView = null;
    }

    /**
     * 在 manifest 中设置当前 activity, 当横竖屏切换时会执行该方法, 否则会 finish 重新执行一遍生命周期
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (playView != null && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // scrollDistance = currentItemView.getTop();

            //获取状态栏高度(如果设置沉浸式状态栏了就不需要获取)
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            currentItemView.setLayoutParams(new AbsListView.
                    LayoutParams(ListView.LayoutParams.MATCH_PARENT,
                    getWindowManager().getDefaultDisplay().getHeight() - rect.top));
            //设置横屏后要显示的当前的 itemView
            videoList.post(new Runnable() {
                @Override
                public void run() {
                    //一定要对添加这句话,否则无效,因为界面初始化完成后 listView 失去了焦点
                    videoList.requestFocusFromTouch();
                    videoList.setSelection(currentPosition);
                }
            });
            Log.i("XX", "横屏");
        } else if (playView != null && newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //横屏时的设置会影响返回竖屏后的效果, 这里设置高度与 xml 文件中的高度相同
            Log.i("MM", currentPosition + "竖屏");
            currentItemView.setLayoutParams(new AbsListView.LayoutParams(
                    ListView.LayoutParams.MATCH_PARENT, getResources()
                    .getDimensionPixelOffset(R.dimen.itmes_height)));
            //本来想切换到竖屏后恢复到初始位置,但是上部出现空白
//            videoList.scrollBy(0, -(scrollDistance));
            //通过该方法恢复位置,不过还是有点小问题
            videoList.post(new Runnable() {
                @Override
                public void run() {
                    videoList.requestFocusFromTouch();
                    videoList.setSelection(firstVisiblePosition);
                }
            });
            Log.i("XX", "竖屏");
        }
    }

    /**
     * 当窗口处于横屏时,点击返回键退出横屏
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (playView != null) {
                    playView.setExpendBtn(false);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 由于视频在 listView 中,横屏后仍然后滑动
     * 此处判断当处于横屏时将滑动事件消耗掉
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onError() {
        closeVideo();
    }

    @Override
    public void onExpend() {
        firstVisiblePosition = videoList.getFirstVisiblePosition();
        //强制横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onShrik() {
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public int getVisibilityPercents(View view) {
        if (view == null) {
            return -1;
        }

        Rect rect = new Rect();
        View tv = view;
        tv.getLocalVisibleRect(rect);
        int height = tv.getHeight();
        int percents = 100;
        if (rect.top == 0 && rect.bottom == height) {
            percents = 100;
        } else if (rect.top > 0) {
            percents = (height - rect.top) * 100 / height;
        } else if (rect.bottom > 0 && rect.bottom < height) {
            percents = rect.bottom * 100 / height;
        }
        return percents;
    }


    //适配器

    private class MyAdapter extends BaseAdapter {

        private Context mContext;

        private List<VideoInfo> videoPaths;

        public MyAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public void setData(List<VideoInfo> videoPaths) {
            this.videoPaths = videoPaths;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return videoPaths != null && videoPaths.size() > 0 ? videoPaths.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return videoPaths != null && videoPaths.size() > 0 ? videoPaths.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.black_cover, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.play_btn = (ImageView) convertView.findViewById(R.id.play_btn);
                viewHolder.play_view = (VideoPlayView) convertView.findViewById(R.id.video_play_view);
                viewHolder.black_cover = convertView.findViewById(R.id.black_cover);
                viewHolder.cover_image = (SimpleDraweeView) convertView.findViewById(R.id.cover_image);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.play_linearlayout = (LinearLayout) convertView.findViewById(R.id.play_linearlayout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //先让黑色背景消失
            //viewHolder.black_cover.setVisibility(View.GONE);
            viewHolder.cover_image.setImageURI(Uri.parse(videoPaths.get(position).getCover_Url()));

            viewHolder.play_btn.setOnClickListener(new MyClick(viewHolder, convertView, position));
            viewHolder.title.setText(videoPaths.get(position).getTitle());

            if (videoPaths.get(position).getScale() == 2) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int)Config.WIDTH/16*9);
                viewHolder.play_linearlayout.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int)Config.WIDTH);
                viewHolder.play_linearlayout.setLayoutParams(params);
            }


            if (currentPosition == position) {
                viewHolder.play_view.setVisibility(View.VISIBLE);
            } else {
                viewHolder.play_view.setVisibility(View.GONE);
                viewHolder.play_view.stop();
                viewHolder.cover_image.setVisibility(View.VISIBLE);
                viewHolder.play_btn.setVisibility(View.VISIBLE);
            }


            return convertView;
        }

        private class ViewHolder {
            private ImageView play_btn;//播放按钮
            private View black_cover;
            private SimpleDraweeView cover_image;
            private VideoPlayView play_view;
            private TextView title;
            private LinearLayout play_linearlayout;


            public void setBlack_coverGone() {
                Animation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration(100);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        black_cover.setVisibility(View.GONE);
                        isAnimtion = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                isAnimtion = true;
                black_cover.startAnimation(alphaAnimation);

                //black_cover.setVisibility(View.GONE);
            }

            public void setBlack_cover() {
                Animation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(100);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        black_cover.setVisibility(View.VISIBLE);
                        isAnimtion = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                isAnimtion = true;
                black_cover.startAnimation(alphaAnimation);
            }


        }

        class MyClick implements View.OnClickListener {
            private ViewHolder viewHolder;
            private View convertView;
            private int position;
            private VideoPlayView playView;


            public MyClick(ViewHolder viewHolder, View convertView, int position) {
                this.viewHolder = viewHolder;
                this.convertView = convertView;
                this.position = position;
                //这个palyview是总全局变量
                this.playView = viewHolder.play_view;
            }

            @Override
            public void onClick(View v) {
                isPlaying = true;
                currentPosition = position;
                currentItemView = convertView;
                //开始播放时隐藏图标
                viewHolder.play_btn.setVisibility(View.GONE);
                viewHolder.cover_image.setVisibility(View.GONE);

                playView.setUrl(videoPaths.get(position).getVideoUrl());

                setPlayView(playView);
                playView.openVideo();
                notifyDataSetChanged();
            }
        }
    }


}
