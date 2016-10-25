package com.duanqu.Idea.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Adapter.ParallaxUserAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.ParallaxListview;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.ParallaxUserBlack;
import com.duanqu.Idea.ViewHolder.ParallaxUserGroupBy;
import com.duanqu.Idea.ViewHolder.ParallaxUser_ITEM1;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.ParallaxUserItem1;
import com.duanqu.Idea.bean.Type;
import com.duanqu.Idea.fragment.UserInfoDetal;
import com.duanqu.Idea.test.Datas;
import com.duanqu.Idea.utils.MD5;
import com.duanqu.Idea.utils.UploadUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ParallaxUserInfoDisplayActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ParallaxListview listview;
    private float mLastY;
    private View header;
    private View userView;
    private TextView nickname;
    private TextView sigh;
    private TextView tag;

    VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    public boolean ALPHA = true;
    public boolean NALPHA = false;
    private FloatingActionButton fab;
    private PopupMenu mPopMenu;
    private ArrayList<Type> beans = new ArrayList<>();
    private FrameLayout frameLayout;

    private String Tag = "Act";
    private ParallaxUserAdapter parallaxUserAdapter;

    private ProgressDialog progress;


    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(ParallaxUserInfoDisplayActivity.this,
                        "修改成功", Toast.LENGTH_SHORT).show();
            }else if(msg.what == 1){
                Toast.makeText(ParallaxUserInfoDisplayActivity.this,
                        "修改失败", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ParallaxUserInfoDisplayActivity.this,
                        "格式错误", Toast.LENGTH_SHORT).show();
            }


        }
    };
    private String updateResult;
    private SimpleDraweeView drawee;
    private String path;
    private String newPath;
    public String UpdateUrl;
    private SimpleDraweeView userhead;

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
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (listview != null && listview.getChildCount() > 0) {
                    if (firstVisibleItem == 0
                            && -view.getChildAt(firstVisibleItem).getTop() > header.getHeight() / 5 * 2
                            && ALPHA) {

                        ObjectAnimator animator = new ObjectAnimator().
                                ofFloat(toolbar, "alpha", 0, 1)
                                .setDuration(300);

                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (animation.getAnimatedFraction() < 0.1) {
                                    getSupportActionBar().setBackgroundDrawable(getResources()
                                            .getDrawable(R.drawable.bg_titlecolor));
                                }
                            }
                        });
                        animator.start();
                        ALPHA = false;
                        NALPHA = true;
                    } else if (firstVisibleItem == 0 && NALPHA == true
                            && -view.getChildAt(firstVisibleItem).getTop() < header.getHeight() / 5 * 2) {
                        ObjectAnimator animator = new ObjectAnimator()
                                .ofFloat(toolbar, "alpha", 1, 0)
                                .setDuration(300);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                getSupportActionBar().setBackgroundDrawable(getResources()
                                        .getDrawable(R.drawable.tran_bg));
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
        frameLayout = (FrameLayout) findViewById(R.id.user_detal);
        listview = (ParallaxListview) findViewById(R.id.parallaxListview);
        listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //listview.setOverScrollMode();
        //toolbar.setAlpha(0);
        header = View.inflate(this, R.layout.parallax_lis_header, null);
        drawee = (SimpleDraweeView) header.findViewById(R.id.bg_cover);
        userhead = (SimpleDraweeView) header.findViewById(R.id.userhead);
        if(!Config.imageurl.equals("")){
            drawee.setImageURI(Uri.parse(Config.imageurl));
        }
        if(!Config.headurl.equals("")){
            userhead.setImageURI(Uri.parse(Config.headurl));
        }


        listview.setDraweeView(drawee);
        listview.setUser_info((LinearLayout) header.findViewById(R.id.user_info));
        //设置listView的头部
        listview.addHeaderView(header, null, false);
        InitAdapter();
        listview.setAdapter(parallaxUserAdapter);
        InitPopMenu();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopMenu.show();
            }
        });


        InitUserView();
        InitToolBar();
        addContentView(toolbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        toolbar.layout(0, 0, toolbar.getWidth(), toolbar.getHeight());


    }



    private void InitUserView() {

        userView = header.findViewById(R.id.userhead);
        //获取SimpleDraweeView
        SimpleDraweeView sdv = (SimpleDraweeView) userView.findViewById(R.id.userhead);
        sdv.setImageURI(Uri.parse(Config.headurl));
        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        rp.setBorder(Color.WHITE, 5);

        nickname = (TextView) header.findViewById(R.id.nickname);
        sigh = (TextView) header.findViewById(R.id.sign);
        tag = (TextView) header.findViewById(R.id.tag);
        RreshText();




        //获取GenericDraweeHierarchy对象
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.
                newInstance(getResources()).setRoundingParams(rp).setFadeDuration(300).build();
        sdv.setHierarchy(hierarchy);
    }

    private void RreshText() {
        if(!Config.nickname.equals("")){
            nickname.setText(Config.nickname);
        }
        if(!Config.sign.equals("")){
            sigh.setText(Config.sign);
        }

        tag.setText(Config.school+" "+Config.grades+" "+Config.major
            );

//        StringBuilder stringBuilder = new StringBuilder();
//
//        if(!Config.school.equals("")) {
//            stringBuilder.append(Config.school);
//        }
//        if(!Config.grades.equals("")){
//            stringBuilder.append(" "+Config.grades);
//        }
//        if(!Config.major.equals("")){
//            stringBuilder.append(" "+Config.major);
//        }

        if(Config.sex.equals("1")){
            tag.setBackground(getResources().getDrawable(R.drawable.fenghong));
        }else{
            tag.setBackground(getResources().getDrawable(R.drawable.tianlan));
        }

//        if(stringBuilder.toString()==null){
//            tag.setText("");
//        }else{
//            tag.setText(stringBuilder.toString());
//        }




    }

    private void InitAdapter() {
        InitLoaclItem();

        parallaxUserAdapter = new ParallaxUserAdapter(this,
                new BaseAdapter.Builder().addType(0, ParallaxUserBlack.class)
                        .addType(1, ParallaxUser_ITEM1.class)
                        .addType(2, ParallaxUserGroupBy.class)
                        .setDatas(beans).build());
    }

    private void InitLoaclItem() {
        com.duanqu.Idea.bean.ParallaxUserBlack black = new com.duanqu.Idea.bean.ParallaxUserBlack();
        black.setType(0);
        ParallaxUserItem1 item1 = new ParallaxUserItem1();
        ParallaxUserItem1 item2 = new ParallaxUserItem1();
        ParallaxUserItem1 item3 = new ParallaxUserItem1();
        ParallaxUserItem1 item4 = new ParallaxUserItem1();
        ParallaxUserItem1 item5 = new ParallaxUserItem1();
        ParallaxUserItem1 item6 = new ParallaxUserItem1();
        ParallaxUserItem1 item7 = new ParallaxUserItem1();

        item1.setType(1);
        item1.setCount(0);
        item1.setTitle("我的浏览记录");
        item1.setIco(R.drawable.ic_access_time_red_300_24dp);

        item2.setType(1);
        item2.setCount(0);
        item3.setIco(R.drawable.ic_replay_red_300_24dp);
        item2.setTitle("我的回复");

        item3.setType(1);
        item3.setCount(0);
        item2.setIco(R.drawable.ic_assignment_red_300_24dp);
        item3.setTitle("我的发言");

        item4.setType(1);
        item4.setCount(0);
        item4.setIco(R.drawable.ic_assistant_red_300_24dp);
        item4.setTitle("我的收藏");

        item5.setType(1);
        item5.setCount(0);
        item5.setIco(R.drawable.ic_tag_faces_red_300_24dp);
        item5.setTitle("我的标签");

        item6.setType(1);
        item6.setCount(0);
        item6.setIco(R.drawable.ic_favorite_red_300_24dp);
        item6.setTitle("累计赞数");

        item7.setType(1);
        item7.setCount(0);
        item7.setIco(R.drawable.ic_launch_red_300_24dp);
        item7.setTitle("累计被转发");

        beans.add(black);
        beans.add(item1);
        beans.add(item2);
        beans.add(item3);
        beans.add(item4);
        beans.add(item5);
        beans.add(black);
        beans.add(item6);
        beans.add(item7);
    }


    private void InitPopMenu() {
        mPopMenu = new PopupMenu(this, fab);
        mPopMenu.getMenuInflater().inflate(R.menu.user_detal_menu, mPopMenu.getMenu());
        mPopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bg: {
                        Intent intent = new Intent(ParallaxUserInfoDisplayActivity.this,ImageWatchActivity.class);
                        intent.putExtra("tag",0);
                        startActivityForResult(intent,0);
                        break;
                    }

                    case R.id.head: {
                        Intent intent = new Intent(ParallaxUserInfoDisplayActivity.this,ImageWatchActivity.class);
                        intent.putExtra("tag",0);
                        startActivityForResult(intent,1);
                        break;
                    }

                    case R.id.detal:{
                        frameLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.user_detal,
                                new UserInfoDetal()).commit();
                        getSupportActionBar().setBackgroundDrawable(getResources().
                                getDrawable(R.drawable.bg_titlecolor));
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode){
            //选择单张
            case 0:{
                if(data==null){
                    Toast.makeText(ParallaxUserInfoDisplayActivity.this, "图片无法加载",
                            Toast.LENGTH_SHORT).show();
                }else{
                    path = data.getStringExtra("data_return");
                    ImageResize();
                    progress = new ProgressDialog(this);
                    progress.setMax(100);
                    progress.setMessage("正在上传...");
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setCancelable(false);
                    UpdateUrl = Datas.imageUrl;
                    new MyAsyncTask().execute(new File(newPath));
                }
                break;
            }
            case 1:{
                if(data==null){
                    Toast.makeText(ParallaxUserInfoDisplayActivity.this, "图片无法加载",
                            Toast.LENGTH_SHORT).show();
                }else{
                    path = data.getStringExtra("data_return");
                    ImageResize();
                    progress = new ProgressDialog(this);
                    progress.setMax(100);
                    progress.setMessage("正在上传...");
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setCancelable(false);
                    UpdateUrl = Datas.HeadUrl;
                    new MyAsyncTask().execute(new File(newPath));
                }
                break;
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ImageResize() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        System.out.println("高"+options.outHeight/728+"宽radio"+options.outWidth/1024);
        int radio;
        if(options.outHeight/options.outWidth > 5){
            radio = 0;
        }else{
            radio = options.outHeight/840 > options.outWidth/1344?options.outHeight/840:
                    options.outWidth/1344;
        }
        System.out.println("the radio"+radio);
        options.inJustDecodeBounds = false;
        options.inSampleSize = radio+1;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        FileOutputStream fos = null;
        newPath = path+ MD5.getMD5("asdfcx32421$#@%FDSA")+".jpg";
        try {
            fos = new FileOutputStream(newPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return super.onCreateOptionsMenu(menu);

    }

    private void InitToolBar() {
        toolbar = (Toolbar) View.inflate(this, R.layout.parallax_user_display_toolbar, null);
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.tran_bg));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        if (listview.getChildAt(listview.getFirstVisiblePosition()) != null
                && listview.getChildAt(listview.getFirstVisiblePosition()).getTop() == 0) {
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

    @Override
    public void onBackPressed() {
        if(frameLayout.getVisibility()!=View.GONE){
            frameLayout.setVisibility(View.GONE);
            getSupportActionBar().setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.tran_bg));
            RreshText();
            toolbar.getMenu().clear();

        }else{
            super.onBackPressed();
        }
    }

    private class MyAsyncTask extends AsyncTask<File, Integer, Void>
    {

        @Override
        protected Void doInBackground(File... params) {
            UploadUtils mUploadUtils;
            if(UpdateUrl.equals(Datas.imageUrl)){
                mUploadUtils = new UploadUtils(Datas.GetImageUrl,
                        "username="+Config.username+"&Token="+Config.Token+"&param=imageurl");
            }else{
                mUploadUtils = new UploadUtils(Datas.GetHeadUrl,
                        "username="+Config.username+"&Token="+Config.Token+"&param=headurl");
            }

            File[] files = new File[params.length];
            for(int i=0;i<files.length;i++){
                files[i] = params[i];
            }
            HashMap<String,String> text = new HashMap<>();
            text.put("username",Config.username);
            text.put("Token",Config.Token);
            mUploadUtils.SetListener(new UploadUtils.UpdateProgress() {
                @Override
                public void update(int i) {
                    publishProgress(i);
                }
            });
            updateResult = mUploadUtils.Update(UpdateUrl,text,files);
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
            Log.e("UPdateResult",updateResult+"result");
            if(updateResult!=null)
            {
                if(UpdateUrl.equals(Datas.imageUrl)){
                    drawee.setImageURI(Uri.parse("file://"+ newPath));
                    //Log.e("xxx",Config.imageurl);
                    Config.imageurl = updateResult;
                    drawee.setImageURI(Uri.parse(Config.imageurl));
                    MyApplication.setSharedPreferences("imageurl",Config.imageurl);
                }else{
                    userhead.setImageURI(Uri.parse("file://"+newPath));
                    Config.headurl = updateResult;
                    userhead.setImageURI(Uri.parse(Config.headurl));
                    MyApplication.setSharedPreferences("headurl",Config.headurl);
                }
                File file = new File(newPath);
                file.delete();
                updateResult = null;
            }

            super.onPostExecute(aVoid);
        }
    }



}
