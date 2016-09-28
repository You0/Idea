package com.duanqu.Idea.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Adapter.ParallaxUserAdapter;
import com.duanqu.Idea.CustomView.ParallaxListview;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.ParallaxUserBlack;
import com.duanqu.Idea.ViewHolder.ParallaxUserGroupBy;
import com.duanqu.Idea.ViewHolder.ParallaxUser_ITEM1;
import com.duanqu.Idea.bean.Type;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ParallaxUserInfoDisplayActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ParallaxListview listview;
    private float mLastY;
    private View header;
    VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    public boolean ALPHA = true;
    public boolean NALPHA = false;

    private ArrayList<Type> beans = new ArrayList<>();


    private String Tag = "Act";
    private ParallaxUserAdapter parallaxUserAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax_user_info_display);

        initView();
        initListen();

    }

    private void initListen() {

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(listview!=null&&listview.getChildCount()>0)
                {
                    Log.e(Tag,"firstVisibleItem"+firstVisibleItem+"top"+view.getChildAt(firstVisibleItem).getTop());

                    if(-view.getChildAt(firstVisibleItem).getTop()>header.getHeight()/3*2&&ALPHA&&firstVisibleItem==0)
                    {
                        ObjectAnimator animator = new ObjectAnimator().ofFloat(toolbar,"alpha",0,1)
                                .setDuration(300);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if(animation.getAnimatedFraction()<0.1){
                                    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_titlecolor));
                                }
                            }
                        });

                        animator.start();
                        ALPHA = false;
                        NALPHA = true;
                    }else if(NALPHA==true&&-view.getChildAt(firstVisibleItem).getTop()<header.getHeight()/3*2&&firstVisibleItem==0){
                        ObjectAnimator animator = new ObjectAnimator().ofFloat(toolbar,"alpha",1,0)
                                .setDuration(300);

                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if(animation.getAnimatedFraction()>0.9)
                                {
                                    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.tran_bg));
                                    toolbar.setAlpha(1);
                                }
                            }
                        });

                        animator.start();
                        NALPHA = false;
                        ALPHA = true;

                    }



                }


            }
        });


    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listview = (ParallaxListview) findViewById(R.id.parallaxListview);
        listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //listview.setOverScrollMode();
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.tran_bg));
        //toolbar.setAlpha(0);
        header = View.inflate(this, R.layout.parallax_lis_header, null);
        SimpleDraweeView drawee = (SimpleDraweeView) header.findViewById(R.id.bg_cover);
        drawee.setImageURI(Uri.parse(Datas.ueser_bg1));

        listview.setDraweeView(drawee);
        //设置listView的头部
        listview.addHeaderView(header);
        InitAdapter();
        listview.setAdapter(parallaxUserAdapter);




    }

    private void InitAdapter() {
        com.duanqu.Idea.bean.ParallaxUserBlack black = new com.duanqu.Idea.bean.ParallaxUserBlack();
        black.setType(0);
        beans.add(black);



        parallaxUserAdapter = new ParallaxUserAdapter(this,
                new BaseAdapter.Builder().addType(0, ParallaxUserBlack.class)
                        .addType(1, ParallaxUser_ITEM1.class)
                        .addType(2, ParallaxUserGroupBy.class)
                        .setDatas(beans).build());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        //listview.scrollTo(0,10);

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float Y = event.getY();

        if(mLastY==0){
            mLastY = Y;
        }
        float deltY = Y - mLastY;
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(10000);

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            return super.dispatchTouchEvent(event);
        }

        if (listview.getChildAt(listview.getFirstVisiblePosition()).getTop() == 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    Log.e(Tag,"Y"+Y);
                    if (deltY > 0) {
                        float xVelocity = mVelocityTracker.getYVelocity();
                        Log.e(Tag,"xV"+xVelocity);
                        listview.setParallaxHead(true, (int) (-xVelocity / 1000));

                    } else {
                        break;
                    }
                }

            }
            mLastY = Y;

            if(MotionEvent.ACTION_UP==event.getAction())
            {
                listview.ResetAnimator();
                mVelocityTracker.clear();
                listview.SetSelectionFromTop();
            }
            return true;
        }

        Log.e(Tag, "未拦截");
        mLastY = Y;
        return super.dispatchTouchEvent(event);
    }
}
