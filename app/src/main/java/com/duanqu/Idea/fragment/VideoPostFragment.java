package com.duanqu.Idea.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.VideoPlayView;
import com.duanqu.Idea.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/10/25.
 */
public class VideoPostFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private EditText content;
    private ImageView play_btn;
    private SimpleDraweeView cover_image;
    private VideoPlayView video_play_view;
    private String videoUri;
    private String thum;
    private LinearLayout play_linearlayout;
    int videoScale;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_post_fragmnet,null);
        content = (EditText) view.findViewById(R.id.content);
        play_btn = (ImageView) view.findViewById(R.id.play_btn);
        cover_image = (SimpleDraweeView) view.findViewById(R.id.cover_image);
        video_play_view = (VideoPlayView) view.findViewById(R.id.video_play_view);
        play_linearlayout = (LinearLayout) view.findViewById(R.id.play_linearlayout);
        setListener();
        return view;
    }

    private void setListener() {
        play_btn.setOnClickListener(this);
    }

    public String getContent() {
        return content.getText().toString();
    }

    public String getVideoUri() {
        return videoUri;
    }

    public String getThum() {
        return thum;
    }

    public int getVideoScale() {
        return videoScale;
    }
    @Override
    public void onStart() {
        super.onStart();
        cover_image.setImageURI(Uri.parse("file://"+thum));

        Bitmap temp = BitmapFactory.decodeFile(thum);
        int height = temp.getHeight();
        int width = temp.getWidth();

        //16:9
        if(height/(float)width > 1.3){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int)Config.WIDTH/16*9);
            play_linearlayout.setLayoutParams(params);
            videoScale = 2;
        }else{
            //4:3
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) Config.WIDTH);
            play_linearlayout.setLayoutParams(params);
            videoScale = 1;
        }
    }

    public void setData(String uri, String thum) {
        this.videoUri = uri;
        this.thum = thum;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play_btn:{
                cover_image.setVisibility(View.GONE);
                play_btn.setVisibility(View.GONE);
                Toast.makeText(getActivity(), videoUri, Toast.LENGTH_LONG).show();
                video_play_view.setVisibility(View.VISIBLE);
                video_play_view.setUrl(videoUri);
                video_play_view.openVideo();
                break;
            }
        }
    }
}
