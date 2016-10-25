package com.duanqu.Idea.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.Idea.Adapter.CotainViewPager;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.AttentionBean;
import com.duanqu.Idea.fragment.AttentionFragment;
import com.duanqu.Idea.test.Datas;
import com.duanqu.qupai.widget.android.app.ProgressDialog;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/15.
 */
public class AttentionActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager user_grid;
    private ImageButton close;
    private Button attention;
    private TextView selected;
    private LinearLayout ll_dots;
    private ProgressDialog mProgressDialog;
    private ImageButton more;
    private TextView tv_more;
    private ArrayList<ImageView> dots = new ArrayList<>();
    private CotainViewPager mAdapter = new CotainViewPager(getSupportFragmentManager());
    private LinkedList<AttentionFragment> fragments = new LinkedList<>();
    private LinkedList<AttentionFragment> temp = new LinkedList<>();
    private int level = 0;
    private ArrayList<AttentionBean> beans = new ArrayList<>();
    private ArrayList<AttentionBean> totalBeans = new ArrayList<>();

    private RotateAnimation rotate;
    private int pages = 0;
    private int CurrentItem;
    private boolean Rresh = false;
    private int sum = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    int page = fragments.size();
                    fragments.addAll(temp);
                    mAdapter.setFragments(fragments);
                    if (user_grid.getAdapter() == null) {
                        Log.e("xxx", "setadapter");
                        user_grid.setAdapter(mAdapter);
                    }
                    user_grid.setCurrentItem(page, true);
                    LightDot(page);
                    Rresh = false;
                    more.clearAnimation();

                    ObjectAnimator animator = new ObjectAnimator()
                            .ofFloat(more,"translationX",15.0f);
                    animator.setInterpolator(new CycleInterpolator(4));
                    animator.setDuration(500);
                    animator.start();

                    int count1 = CheckCount();
                    selected.setText("(已经选择了" + count1 + "人)");
                    break;
                }
                case 1: {
                    Log.e("xxx", "task");
                    Task();
                    break;
                }

                case 2:{
                    Snackbar.make(user_grid, "网络错误", Snackbar.LENGTH_SHORT).show();
                    more.clearAnimation();
                    Rresh = false;
                    break;
                }


            }
        }
    };


    private int CheckCount() {
        int count = 0;
        for (int i = 0; i < totalBeans.size(); i++) {
            if (totalBeans.get(i).getChecked() == true) {
                count++;
            }
        }
        return count;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_popup_layout);
        initView();
        bindData();
    }

    private void initView() {
        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setRepeatCount(Animation.INFINITE);


        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        user_grid = (ViewPager) findViewById(R.id.user_grid);
        close = (ImageButton) findViewById(R.id.close);
        attention = (Button) findViewById(R.id.attention);
        selected = (TextView) findViewById(R.id.selected);
        more = (ImageButton) findViewById(R.id.more);
        tv_more = (TextView) findViewById(R.id.tv_more);
        attention.setOnClickListener(this);
        close.setOnClickListener(this);
        more.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        //setBackgroundDrawable(new ColorDrawable(0x96000000));
        more.startAnimation(rotate);
        user_grid.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("xxx", "vpPosition" + position);
                LightDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void LightDot(int position) {
        for (int i = 0; i < dots.size(); i++) {
            if (i == position) {
                dots.get(i).setImageResource(R.drawable.ic_lens_red_a100_18dp);
            } else {
                dots.get(i).setImageResource(R.drawable.ic_lens_black_18dp);
            }
        }
    }

    private void bindData() {
        Snackbar.make(user_grid, "正在帮您检索同班用户", Snackbar.LENGTH_LONG).show();
        RunTask(String.valueOf(++level));

    }

    private void RunTask(final String level) {
        OkHttpUtils.post().url(Datas.Getrelation).
                addParams("username", Config.username)
                .addParams("Token", Config.Token)
                .addParams("level", level)
                .addHeader("Content-Type", "application/x-www-form-urlencoded").
                build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                //Log.e("xxx",response.body().string()+"body");
                ParseJson(response.body().string());
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("okhttp", "onError");
                AttentionActivity.this.level--;
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onResponse(Object response, int id) {
                Log.e("okhttp", "response");
            }
        });


    }

    private void Task() {
        temp.clear();
        mAdapter.notifyDataSetChanged();
        AttentionFragment attentionFragment;
        pages = beans.size() / 9 + 1;
        sum += pages;
        Log.e("xxx", "beans" + beans.size() + "pages" + pages);
        ll_dots.removeAllViews();
        dots.clear();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                16,
                16);
        params.leftMargin = 5;
        params.rightMargin = 5;
        //初始化红点
        for (int i = 0; i < sum; i++) {
            ImageView iv_dot = new ImageView(AttentionActivity.this);
            ll_dots.addView(iv_dot, params);
            dots.add(iv_dot);
            Log.e("xxx", "addDots");
        }

        for (int i = 0; i < pages; i++) {
            attentionFragment = new AttentionFragment();
            if (i == pages - 1) {
                attentionFragment.upDateUI(SubArrayList(beans, i * 9, beans.size()));
            } else {
                attentionFragment.upDateUI(SubArrayList(beans, i * 9, (i + 1) * 9));
            }
            Log.e("xx", "x" + attentionFragment.getDatas().size());

            attentionFragment.setListener(new AttentionFragment.CheckedChangeListener() {
                @Override
                public void CheckedChange(String name, boolean isChecked) {
                    UpdateTextView(name, isChecked);
                }
            });

            temp.add(attentionFragment);
        }

        handler.sendEmptyMessage(0);
    }

    private void UpdateTextView(String name, boolean isChecked) {
        Log.e("xxx", "UPDAT" + name + isChecked);
        for (int i = 0; i < totalBeans.size(); i++) {
            if (totalBeans.get(i).getName().equals(name)) {
                totalBeans.get(i).setChecked(isChecked);
            }
        }
        selected.setText("(已经选择了" + CheckCount() + "人)");
    }


    private ArrayList<AttentionBean> SubArrayList(ArrayList<AttentionBean> array, int start, int end) {
        ArrayList<AttentionBean> subArray = new ArrayList<>();
        Log.e("xxx", start + ":" + end);
        for (; start < end; start++) {
            subArray.add(array.get(start));
        }
        return subArray;
    }

    private void ParseJson(String json) {
        Log.e("Json", json);
        try {
            JSONArray jsonArray = new JSONArray(json);
            Log.e("xxx", "len" + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                AttentionBean bean = new AttentionBean();
                bean.setUsername(object.getString("username"));
                Log.e("xxx", object.getString("username"));
                bean.setName(object.getString("realname"));
                bean.setUrl(object.getString("headurl"));
                boolean add = true;
                for (int j = 0; j < totalBeans.size(); j++) {
                    if (totalBeans.get(j).getName().equals(bean.getName())) {
                        add = false;
                    }
                }
                if (add) {
                    bean.setChecked(true);
                    beans.add(bean);
                }
            }
            totalBeans.addAll(beans);
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragments = null;
        user_grid = null;
        mAdapter = null;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close: {
                finish();
                overridePendingTransition(0, 0);
                break;
            }
            case R.id.attention: {
                mProgressDialog = ProgressDialog.show(this, null, "正在努力为您添加关注...");

                //发一个handler然后结束
            }

            case R.id.tv_more:
            case R.id.more: {
                if (Rresh == true) {
                    return;
                }

                more.startAnimation(rotate);

                Rresh = true;
                //开始加载
                if (level <= 3) {
                    beans.clear();
                    pages = 0;

                    switch (level) {
                        case 0:
                            Snackbar.make(user_grid, "为您检索同班用户", Snackbar.LENGTH_LONG).show();
                            RunTask(String.valueOf(++level));
                            break;
                        case 1:
                            Snackbar.make(user_grid, "为您检索同专业的用户", Snackbar.LENGTH_LONG).show();
                            RunTask(String.valueOf(++level));
                            break;
                        case 2:
                            Snackbar.make(user_grid, "为您检索同年级的用户", Snackbar.LENGTH_LONG).show();
                            RunTask(String.valueOf(++level));
                            break;
                        case 3:
                            Snackbar.make(user_grid, "为您检索同院系的用户", Snackbar.LENGTH_LONG).show();
                            RunTask(String.valueOf(++level));
                            break;
                    }
                } else {
                    Snackbar.make(user_grid, "已无更多╮(╯▽╰)╭", Snackbar.LENGTH_SHORT).show();
                    more.clearAnimation();
                }
                break;
            }
        }
    }
}
