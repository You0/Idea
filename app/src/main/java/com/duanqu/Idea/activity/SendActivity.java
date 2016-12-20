package com.duanqu.Idea.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.HorizontalScrollViewEx;
import com.duanqu.Idea.CustomView.ToSomeOne;
import com.duanqu.Idea.CustomView.UserTag;
import com.duanqu.Idea.R;
import com.duanqu.Idea.fragment.BaseFragment;
import com.duanqu.Idea.fragment.ImagePostFragment;
import com.duanqu.Idea.fragment.TextPostFragment;
import com.duanqu.Idea.fragment.VideoPostFragment;
import com.duanqu.Idea.test.Datas;
import com.duanqu.Idea.utils.FeedUploadUtils;
import com.duanqu.Idea.utils.UploadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SendActivity extends AppCompatActivity implements ViewGroup.OnClickListener{
    private String id;
    private Toolbar toolbar;
    private ToSomeOne toSomeOne;
    private FrameLayout frameLayout;
    private static HorizontalScrollViewEx hs;
    private LinearLayout at;
    private GetFriendsRelationship popWindow = null;
    private BaseFragment fragment;
    private Intent intent;
    private boolean updateResult;
    private ProgressDialog progress;
    private String Content = null;
    private String VideoUrl = null;
    private ArrayList<String> images = null;



    private static HashMap<String,View> views = new HashMap<>();

    public static int IMAGE_CHANGE = 99;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.sent_layout);
        intent = getIntent();
        id = intent.getStringExtra("id");
        init();
        chooseFragment();
    }

    private void chooseFragment() {
        if(id.equals("image")){
            //将image信息传递进fragment
            fragment = new ImagePostFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();
            ArrayList paths = intent.getStringArrayListExtra("images");
            if(paths==null)
            {
                paths = new ArrayList();
                paths.add(intent.getStringExtra("image"));
            }
            ((ImagePostFragment)fragment).setPath(paths);
        }else if(id.equals("text")){
            fragment = new TextPostFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();
        }else if(id.equals("video")){
            fragment = new VideoPostFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();
            ((VideoPostFragment) fragment).setData(intent.getStringExtra("videoUri"),
                    intent.getStringExtra("thum"));

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onactivityresult");
        if(resultCode==RESULT_OK){
            if(requestCode==IMAGE_CHANGE){
                ImagePostFragment imagePostFragment = (ImagePostFragment)fragment;
                if(data.getStringArrayListExtra("data_return")==null){
                    ArrayList<String> array = new ArrayList<>();
                    System.out.println(data.getStringExtra("data_return"));
                    array.add(data.getStringExtra("data_return"));
                    array.addAll(ImageWatchActivity.mImageWatchActivity.getGridViewAdapter().getmSelected());
                    imagePostFragment.getPath().clear();
                    imagePostFragment.setPath(array);
                    imagePostFragment.UpdateImages();
                    return;
                }

                ((ImagePostFragment)fragment).getPath().clear();
                ((ImagePostFragment)fragment).setPath(data.getStringArrayListExtra("data_return"));
                ((ImagePostFragment)fragment).UpdateImages();
            }
        }
    }

    public static void removeViews(String name){
        //及时remove掉view
        hs.removeView(views.get(name));
        views.remove(name);

    }

    public static void removeViews(){
        views.clear();
    }

    public static Set<String> getViews(){
        return  views.keySet();
    }

    public static void setViews(String name,View view) {
        views.put(name,view);
        hs.addView(view);
    }

    private void init() {

        at = (LinearLayout) findViewById(R.id.at);
        hs = (HorizontalScrollViewEx) findViewById(R.id.hs);

        //初始化标题栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("发送");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //加入点击
        hs.setOnClickListener(this);
        at.setOnClickListener(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(popWindow==null){
            popWindow = new GetFriendsRelationship(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progress = new ProgressDialog(this);
        progress.setMax(100);
        progress.setMessage("正在上传...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);

        if(id.equals("image")){
            ImagePostFragment imagePostFragment = (ImagePostFragment)fragment;
            images  = imagePostFragment.getPath();
            Content = imagePostFragment.getContent();



        }else if(id.equals("text")){
            TextPostFragment textPostFragment = (TextPostFragment)fragment;
            Content = textPostFragment.getEditorText().toString();


        }else if(id.equals("video")){
            VideoPostFragment videoPostFragment = (VideoPostFragment)fragment;
            Content = videoPostFragment.getContent();
            VideoUrl = videoPostFragment.getVideoUri();
            if(images==null){
                images = new ArrayList<>();
            }
            images.add(videoPostFragment.getThum());


        }

        if(VideoUrl!=null){
            images.add(VideoUrl);

        }
        if(id.equals("text")){
            //上传10
            new MyAsyncTask().execute(new File[0]);
        }else{
            File[] params = new File[images.size()];
            for(int i=0;i<images.size();i++){
                params[i] = new File(images.get(i));
            }
            new MyAsyncTask().execute(params);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.at:{
                System.out.println("hs click");
                if(popWindow==null){
                    popWindow = new GetFriendsRelationship(this);
                }
                popWindow.showAsDropDown(toolbar, (int)Config.WIDTH/3,0);
                //popWindow.showAtLocation(at,Gravity.RIGHT|Gravity.TOP,0,0);
                break;
            }

        }
    }


    class MyAsyncTask extends AsyncTask<File[],Integer,Void>
    {
        @Override
        protected Void doInBackground(File[]... params) {
            FeedUploadUtils mUploadUtils = new FeedUploadUtils();
            File[] files = new File[params[0].length];
            for (int i=0;i<files.length;i++){
                files[i] = params[0][i];
            }
            String atUsernames = "";
            Set<String> AtBodys  = views.keySet();
            for (String name : AtBodys){
                atUsernames = atUsernames + name+";";
            }

            //atUsernames = "E41414005;E41414006";
            HashMap<String,String> text = new HashMap<>();

            text.put("ownerId",Config.userid);
            text.put("text",atUsernames);
            text.put("content",Content);
            text.put("token","123");


            mUploadUtils.SetListener(new FeedUploadUtils.UpdateProgress() {
                @Override
                public void update(int i) {
                    publishProgress(i);
                }
            });

            if(VideoUrl!=null){
                Log.e("SendActivity","video");
                updateResult = mUploadUtils.UpdateOnlyVideo(Datas.PublishFeed,files,text);
            }else{
                Log.e("SendActivity","image OR text");
                updateResult = mUploadUtils.Update(Datas.PublishFeed,text,files);
            }

            Log.e("xxx","x"+updateResult);
            return null;
        }

        @Override
        protected void onPreExecute() {
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.cancel();
            Toast.makeText(SendActivity.this, "发布成功!", Toast.LENGTH_SHORT).show();
            finish();
            super.onPostExecute(aVoid);
        }
    }








}
