package com.duanqu.Idea.ViewHolder;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.MainActivity1;
import com.duanqu.Idea.activity.VideoPlayViewActivity;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.fragment.BaseFragment;
import com.duanqu.Idea.player.view.MediaController;
import com.duanqu.Idea.player.view.SuperVideoPlayer;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Item_content_TYPE4 extends MainMessageBaseViewHolder {
    private ImageView mPlayBtnView;
    private Uri uri;
    private ImageView frame;

    @Override
    protected void bindData(MainMessageBean data) {
        String video = data.getVideoUri();
        uri = Uri.parse(video);
//        try {
//            if (Build.VERSION.SDK_INT >= 14) {
//                retriever.setDataSource(video, new HashMap<String, String>());
//            } else {
//                retriever.setDataSource(video);
//            }
//            frame.setImageBitmap(retriever.getFrameAtTime(1));
//        } catch (RuntimeException ex) {
//        } finally {
//            try {
//                retriever.release();
//            } catch (RuntimeException ex) {
//            }
//        }

    }

    @Override
    protected int getViewLayout() {
        return R.layout.item_content_type4;
    }

    @Override
    protected void FindView(View parent) {
        mPlayBtnView = (ImageView) parent.findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivityContext(), VideoPlayViewActivity.class);
                getActivityContext().startActivity(intent);
            }
        });
        frame = (ImageView) parent.findViewById(R.id.frame);
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.play_btn: {
//
//
//                break;
//            }
//
//
//        }
//
//
//    }

    /**
     * 播放器的回调函数
     */
//    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
//        /**
//         * 播放器关闭按钮回调
//         */
//        @Override
//        public void onCloseVideo() {
//            mSuperVideoPlayer.close();//关闭VideoView
//            mPlayBtnView.setVisibility(View.VISIBLE);
//            resetPageToPortrait();
//        }
//
//        /**
//         * 播放器横竖屏切换回调
//         */
//        @Override
//        public void onSwitchPageType() {
////            if (MainActivity1.mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
////                MainActivity1.mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
////            } else {
////                MainActivity1.mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
////                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
////            }
//        }
//
//        /**
//         * 播放完成回调
//         */
//        @Override
//        public void onPlayFinish() {
//
//        }
//    };
//
//    /***
//     * 恢复屏幕至竖屏
//     */
//    private void resetPageToPortrait() {
////        if (MainActivity1.mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
////            MainActivity1.mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
////        }
//    }

}
