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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.duanqu.Idea.ViewHolder.ParallaxUser_ITEM2;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.ParallaxOtherUserBean;
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
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ParallaxOtherUserInfoDisplayActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ParallaxListview listview;
    private float mLastY;
    private View header;
    private View userView;
    private TextView nickname;
    private TextView sigh;
    private TextView tag;
    private Button contacts;

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
    private ParallaxOtherUserBean bean;


    private String updateResult;
    private SimpleDraweeView drawee;
    private String path;
    private String newPath;
    public String UpdateUrl;
    private SimpleDraweeView userhead;
    private RelativeLayout loading;
    private String strNickname = "";
    private String Uid;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    loading.setVisibility(View.GONE);
                    Log.e("pouda", bean.toString());
                    Log.e("xxx", bean.getHeadurl());
                    Uid = String.valueOf(bean.getId());
                    userhead.setImageURI(Uri.parse(bean.getHeadurl()));
                    drawee.setImageURI(Uri.parse(bean.getImageurl()));
                    ParallaxOtherUserBean tempbean = (ParallaxOtherUserBean) beans.get(1);
//            beans.remove(1);
//            bean.setType(3);
//            beans.add(1,bean);
                    Log.e("1", beans.get(1).toString());
                    tempbean.setUsername(bean.getUsername());
                    tempbean.setHeadurl(bean.getHeadurl());
                    tempbean.setNickname(bean.getNickname());
                    tempbean.setId(Integer.parseInt(Uid));
                    Log.e("2", beans.get(1).toString());
                    ((ParallaxOtherUserBean) beans.get(1)).setAlready(bean.getAlready());
                    //更新adapter
                    parallaxUserAdapter.notifyDataSetChanged();

                    RreshText();
                    //初始化圆角圆形参数对象
                    RoundingParams rp = new RoundingParams();
                    //设置图像是否为圆形
                    rp.setRoundAsCircle(true);
                    rp.setBorder(Color.WHITE, 5);
                    //获取GenericDraweeHierarchy对象
                    GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.
                            newInstance(getResources()).setRoundingParams(rp).setFadeDuration(300).build();
                    userhead.setHierarchy(hierarchy);
                    if (bean.getHeadurl() == null || bean.getHeadurl().equals("") || bean.getHeadurl().equals("http://115.159.159.65:8080/userhead/")) {
                        userhead.setImageURI(Uri.parse(Config.defaultHeader));
                    } else {
                        userhead.setImageURI(Uri.parse(bean.getHeadurl()));
                    }

                    GetuserNumberFromServer();

                    break;
                }

                case 10: {
                    parallaxUserAdapter.setDatas(beans);
                    parallaxUserAdapter.notifyDataSetChanged();
                }

            }


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax_otheruser_info_display);
        loading = (RelativeLayout) findViewById(R.id.loading);
        initView();
        initListen();
        Intent intent = getIntent();
        String contact = intent.getStringExtra("contact");
        String userid = intent.getStringExtra("userid");
        String nickname = intent.getStringExtra("nickname");

        try {
            nickname = URLEncoder.encode(nickname, "UTF-8");
            nickname = URLEncoder.encode(nickname, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        strNickname = nickname;

        if(strNickname == null){
            strNickname = "";
        }
        RequestCall http = null;
        if (contact != null) {
            http = OkHttpUtils.post().url(Datas.GetUserInfo)
                    .addParams("username", Config.username)
                    .addParams("Token", Config.Token)
                    .addParams("contact", contact)
                    .build();
        } else if (userid != null) {
            http = OkHttpUtils.post().url(Datas.GetUserInfo)
                    .addParams("username", Config.username)
                    .addParams("Token", Config.Token)
                    .addParams("userid", userid)
                    .build();

        } else if (nickname != null) {
            http = OkHttpUtils.post().url(Datas.GetUserInfo)
                    .addParams("username", Config.username)
                    .addParams("Token", Config.Token)
                    .addParams("nickname", nickname)
                    .build();
        }

        http.execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                Gson gson = new Gson();
                bean = gson.fromJson(response.body().string(), ParallaxOtherUserBean.class);
                handler.sendEmptyMessage(0);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }


    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.user_detal);
        listview = (ParallaxListview) findViewById(R.id.parallaxListview);
        listview.setOverScrollMode(View.OVER_SCROLL_NEVER);

        header = View.inflate(this, R.layout.parallax_lis_header, null);
        nickname = (TextView) header.findViewById(R.id.nickname);
        sigh = (TextView) header.findViewById(R.id.sign);
        tag = (TextView) header.findViewById(R.id.tag);
        drawee = (SimpleDraweeView) header.findViewById(R.id.bg_cover);
        userhead = (SimpleDraweeView) header.findViewById(R.id.userhead);
        //userhead.setImageURI(Uri.parse( "res://"+this.getPackageName()+"/"+ R.drawable.default_head_1));
        listview.setDraweeView(drawee);
        listview.setUser_info((LinearLayout) header.findViewById(R.id.user_info));
        //设置listView的头部
        listview.addHeaderView(header, null, false);
        InitAdapter();
        listview.setAdapter(parallaxUserAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 4: {
                        //他的发言
                        //发言
                        Intent intent = new Intent(ParallaxOtherUserInfoDisplayActivity.this,UserFeedsDisplayAty.class);
                        intent.putExtra("uid",Uid);
                        startActivity(intent);
                        break;
                    }
                    case 5: {
                        //他的收藏
                        Intent intent = new Intent(ParallaxOtherUserInfoDisplayActivity.this,HistoryAndCacheCommentActitity.class);
                        intent.putExtra("type","cache");
                        intent.putExtra("uid",Uid);
                        startActivity(intent);
                        break;
                    }

                    case 6: {
                        //标签
                        break;
                    }

                }


            }
        });

        InitToolBar();
        addContentView(toolbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        toolbar.layout(0, 0, toolbar.getWidth(), toolbar.getHeight());
    }


    private void RreshText() {
        //这里是设置标签的

        nickname.setText(bean.getNickname());
        if (!bean.getSign().equals("")) {
            sigh.setText(bean.getSign());
        } else {
            sigh.setText("空空如也");
        }
        // Log.e("otherUserInfoDispaly","sign"+bean.getSign()+"len"+bean.getSign().length());
        tag.setText(bean.getSchool() + " " + bean.getGrades() + " " + bean.getMajor());
        if (tag.getText().toString().equals("  ")) {
            tag.setText("未填写任何信息");
        }
        //Log.e("otherUserInfoDispaly","tag"+tag.getText().toString().length());
        if (bean.getSex().equals("1")) {
            tag.setBackground(getResources().getDrawable(R.drawable.fenghong));
        } else {
            tag.setBackground(getResources().getDrawable(R.drawable.tianlan));
        }
    }

    private void InitAdapter() {
        InitLoaclItem();

        parallaxUserAdapter = new ParallaxUserAdapter(this,
                new BaseAdapter.Builder()
                        .addType(0, ParallaxUserBlack.class)
                        .addType(1, ParallaxUser_ITEM1.class)
                        .addType(2, ParallaxUserGroupBy.class)
                        .addType(3, ParallaxUser_ITEM2.class)
                        .setDatas(beans).build());
    }

    private void InitLoaclItem() {
        com.duanqu.Idea.bean.ParallaxUserBlack black = new com.duanqu.Idea.bean.ParallaxUserBlack();
        com.duanqu.Idea.bean.ParallaxUserBlack black1 = new com.duanqu.Idea.bean.ParallaxUserBlack();

        black.setType(0);
        black1.setType(0);
        black1.setHeight(10);
        ParallaxOtherUserBean item2 = new ParallaxOtherUserBean();
        ParallaxUserItem1 item3 = new ParallaxUserItem1();
        ParallaxUserItem1 item4 = new ParallaxUserItem1();
        ParallaxUserItem1 item6 = new ParallaxUserItem1();


        item2.setType(3);

        item3.setType(1);
        item3.setCount(0);
        item3.setIco(R.drawable.ic_replay_red_300_24dp);
        item3.setTitle("他的发言");

        item4.setType(1);
        item4.setCount(0);
        item4.setIco(R.drawable.ic_assistant_red_300_24dp);
        item4.setTitle("他的收藏");


        item6.setType(1);
        item6.setCount(0);
        item6.setIco(R.drawable.ic_tag_faces_red_300_24dp);
        item6.setTitle("他的标签");


        beans.add(black);
        beans.add(item2);
        beans.add(black1);
        beans.add(item3);
        beans.add(item4);
        beans.add(item6);
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
            return super.dispatchTouchEvent(event);
        }
        mLastY = Y;
        return super.dispatchTouchEvent(event);
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


    private void GetuserNumberFromServer() {
        OkHttpUtils.post().url(Datas.getDisPlayNums)
                .addParams("Token", Config.Token)
                .addParams("username", Config.username)
                .addParams("nickname", strNickname)
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String nums = response.body().string();
                JSONArray array = new JSONArray(nums);

                ParallaxOtherUserBean item2 = (ParallaxOtherUserBean) beans.get(1);
                ParallaxUserItem1 item3 = (ParallaxUserItem1) beans.get(3);
                ParallaxUserItem1 item4 = (ParallaxUserItem1) beans.get(4);
                ParallaxUserItem1 item6 = (ParallaxUserItem1) beans.get(5);

                for (int i = 0; i < array.length(); i++) {
                    System.out.println(array.get(i));
                }

                item2.setContactsCount((Integer) array.get(4));
                item2.setCountLove((Integer) array.get(6));
                item2.setRevcontactsCount((Integer) array.get(5));

                item3.setCount((Integer) array.get(3));
                item4.setCount((Integer) array.get(1));


                handler.sendEmptyMessage(10);

                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }


}
