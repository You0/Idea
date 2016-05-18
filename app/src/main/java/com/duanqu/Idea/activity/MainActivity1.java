package com.duanqu.Idea.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.CotainViewPager;
import com.duanqu.Idea.R;

import org.w3c.dom.Text;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView tv_msg;
    private TextView tv_tj;
    private ViewPager viewPager;
    private ImageButton menu;
    private ImageView search;
    private ImageView image;
    private ImageButton video;
    private ImageView editor;

    private LinkedList<Integer> layouts = new LinkedList<>();
    private CotainViewPager cotainViewPagerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initView();
        blindListenerView();


    }

    private void blindListenerView() {
        menu.setOnClickListener(this);
        search.setOnClickListener(this);
        image.setOnClickListener(this);
        video.setOnClickListener(this);
        editor.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int Big_size = getResources().getDimensionPixelSize(R.dimen.font_17sp);
                int Small_size = getResources().getDimensionPixelSize(R.dimen.font_14sp);
                int delt_size = Big_size-Small_size;
                if(position==0){
                    tv_msg.setTextSize(Big_size - delt_size*positionOffset);
                    tv_tj.setTextSize(Small_size + delt_size*positionOffset);
                }else{
                    tv_msg.setTextSize(Small_size + delt_size*positionOffset);
                    tv_tj.setTextSize(Big_size - delt_size*positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    tv_msg.setTextColor(getResources().getColor(R.color.black));
                    tv_tj.setTextColor(getResources().getColor(R.color.colorHuise));
                }else{
                    tv_msg.setTextColor(getResources().getColor(R.color.colorHuise));
                    tv_tj.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getLayoutInflater().inflate(R.layout.tool_bar, toolbar);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_tj = (TextView) findViewById(R.id.tv_tj);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        menu = (ImageButton) findViewById(R.id.menu);
        search = (ImageView) findViewById(R.id.search);
        image = (ImageView) findViewById(R.id.sendImage);
        video = (ImageButton) findViewById(R.id.sendVideo);
        editor = (ImageView) findViewById(R.id.sendText);

        layouts.add(R.layout.inner_viewpager);
        cotainViewPagerAdapter = new CotainViewPager(layouts);
        viewPager.setAdapter(cotainViewPagerAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search: {
                System.out.println("搜索");
                break;
            }

        }
    }

    private void InitAnimation()
    {




    }



}
