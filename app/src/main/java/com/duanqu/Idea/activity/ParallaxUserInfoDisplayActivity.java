package com.duanqu.Idea.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Adapter.ParallaxUserAdapter;
import com.duanqu.Idea.CustomView.ParallaxListview;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.ParallaxUserBlack;
import com.duanqu.Idea.ViewHolder.ParallaxUserGroupBy;
import com.duanqu.Idea.ViewHolder.ParallaxUser_ITEM1;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.Type;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.Animator;
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
    private View userView;
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

                if (listview != null && listview.getChildCount() > 0) {
                    if (firstVisibleItem == 0 && -view.getChildAt(firstVisibleItem).getTop() > header.getHeight() / 5 * 2 && ALPHA) {
                        ObjectAnimator animator = new ObjectAnimator().ofFloat(toolbar, "alpha", 0, 1)
                                .setDuration(300);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (animation.getAnimatedFraction() < 0.1) {
                                    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_titlecolor));
                                }
                            }
                        });

                        animator.start();
                        ALPHA = false;
                        NALPHA = true;
                    } else if (firstVisibleItem == 0 && NALPHA == true && -view.getChildAt(firstVisibleItem).getTop() < header.getHeight() / 5 * 2) {
                        ObjectAnimator animator = new ObjectAnimator().ofFloat(toolbar, "alpha", 1, 0)
                                .setDuration(300);

                        animator.addListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.tran_bg));
                                toolbar.setAlpha(1);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

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

        listview = (ParallaxListview) findViewById(R.id.parallaxListview);
        listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //listview.setOverScrollMode();

        //toolbar.setAlpha(0);
        header = View.inflate(this, R.layout.parallax_lis_header, null);
        SimpleDraweeView drawee = (SimpleDraweeView) header.findViewById(R.id.bg_cover);
        drawee.setImageURI(Uri.parse(Datas.ueser_bg1));

        listview.setDraweeView(drawee);
        listview.setUser_info((LinearLayout) header.findViewById(R.id.user_info));
        //设置listView的头部
        listview.addHeaderView(header, null, false);
        InitAdapter();
        listview.setAdapter(parallaxUserAdapter);

        InitUserView();
        InitToolBar();
        addContentView(toolbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        toolbar.layout(0, 0, toolbar.getWidth(), toolbar.getHeight());


    }

    private void InitUserView() {

        userView = header.findViewById(R.id.userhead);
        //获取SimpleDraweeView
        SimpleDraweeView sdv = (SimpleDraweeView) userView.findViewById(R.id.userhead);
        sdv.setImageURI(Uri.parse(Datas.userHead));
        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        rp.setBorder(Color.WHITE, 5);

        //获取GenericDraweeHierarchy对象
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.
                newInstance(getResources()).setRoundingParams(rp).setFadeDuration(300).build();
        sdv.setHierarchy(hierarchy);
    }

    private void InitAdapter() {
        com.duanqu.Idea.bean.ParallaxUserBlack black = new com.duanqu.Idea.bean.ParallaxUserBlack();
        black.setType(0);
        beans.add(black);
        beans.add(black);
        beans.add(black);
        beans.add(black);
        beans.add(black);
        beans.add(black);
        beans.add(black);
        beans.add(black);
        beans.add(black);


        parallaxUserAdapter = new ParallaxUserAdapter(this,
                new BaseAdapter.Builder().addType(0, ParallaxUserBlack.class)
                        .addType(1, ParallaxUser_ITEM1.class)
                        .addType(2, ParallaxUserGroupBy.class)
                        .setDatas(beans).build());
    }


    private void InitToolBar() {
        toolbar = (Toolbar) View.inflate(this, R.layout.parallax_user_display_toolbar, null);
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.tran_bg));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float Y = event.getY();

        if (mLastY == 0) {
            mLastY = Y;
        }
        float deltY = Y - mLastY;
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(10000);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return super.dispatchTouchEvent(event);
        }

        if (listview.getChildAt(listview.getFirstVisiblePosition()) != null && listview.getChildAt(listview.getFirstVisiblePosition()).getTop() == 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    if (deltY > 0) {
                        float xVelocity = mVelocityTracker.getYVelocity();

                        listview.setParallaxHead(true, (int) (-xVelocity / 1000));

                    } else {
                        break;
                    }
                }

            }
            mLastY = Y;

            if (MotionEvent.ACTION_UP == event.getAction()) {
                listview.ResetAnimator();
                mVelocityTracker.clear();
                listview.SetSelectionFromTop();
            }
            return true;
        }


        mLastY = Y;
        return super.dispatchTouchEvent(event);
    }
}
