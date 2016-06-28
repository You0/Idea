package com.duanqu.Idea.utils;

import android.animation.Animator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;


/**
 * Created by Administrator on 2016/6/28.
 * 处理APP上下滑动所产生一些VIEW动画的类（只是Translate）
 */

public class ViewHideAnimationUtils extends MyGestureDetector implements Animator.AnimatorListener {
    private View view;
    private ViewGroup viewGroup;
    private int TranslationY;
    private int curShowHide = 0;

    public ViewHideAnimationUtils(ViewGroup view,int translationY) {
        super(view.getContext());
        this.viewGroup = view;
        TranslationY = translationY;
    }



    public ViewHideAnimationUtils(Context context) {
        super(context);
    }

    @Override
    public void onScrollDown() {
       // viewGroup.setVisibility(View.VISIBLE);
        animateShow();
        curShowHide = 0;
    }

    @Override
    public void onScrollUp() {
        //viewGroup.setVisibility(View.VISIBLE);
        animateHide();
        curShowHide = 1;
    }


    private void animateShow() {
        viewGroup.setTranslationY(TranslationY);
        viewGroup.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator())
                .setStartDelay(0).setDuration(400).setListener(this).start();
        setIgnore(true);
    }

    private void animateHide(){
        viewGroup.setTranslationY(0);
        viewGroup.animate().translationY(TranslationY).setInterpolator(new AccelerateDecelerateInterpolator())
                .setStartDelay(0).setDuration(400).setListener(this).start();

        setIgnore(true);
    }






    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        //如果
        if(curShowHide==0){
            viewGroup.setVisibility(View.VISIBLE);
            viewGroup.setTranslationY(0);
        }else if(curShowHide == 1){ //如果是隐藏的则设为不可见
            viewGroup.setVisibility(View.INVISIBLE);
            viewGroup.setTranslationY(TranslationY);
        }
        setIgnore(false);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }


}

