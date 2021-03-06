package com.duanqu.Idea.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.PopupMenu;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.MyScaleSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.utils.FileUtils;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/15.
 */
public class ImageDisplay extends AppCompatActivity {
    private ViewPager vp;
    private Intent intent;
    private ArrayList images;
    public static ImageDisplay imageD;

    private int Position = 0;
    private FloatingActionButton fab;
    private TextView nums;
    private int currentItem;
    private PopupMenu mPopMenu;

    private LinkedList l = new LinkedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_display);
        intent = getIntent();
        if(images!=null){
            images.clear();
        }
        images = intent.getCharSequenceArrayListExtra("images");
        currentItem = intent.getIntExtra("position", 0);
        initView();
        bindData();
        imageD = this;


    }

    private void bindData() {
        nums.setText(currentItem + 1 + "/" + images.size());
//        for(int i=0;i<images.size();i++)
//        {
//
//            display_fragment = new Image_Display_Fragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("uri", (String) images.get(i));
//            display_fragment.setArguments(bundle);
//            fragments.add(display_fragment);
//        }
//        display_fragment = new Image_Display_Fragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("uri", (String) images.get(0));
//        display_fragment.setArguments(bundle);
//        fragments.add(display_fragment);
//
//        cotainViewPagerAdapter = new CotainViewPager(getSupportFragmentManager());
//        cotainViewPagerAdapter.setFragments(fragments);
//        vp.setAdapter(cotainViewPagerAdapter);
        vp.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                MyScaleSimpleDraweeView myScaleView = new MyScaleSimpleDraweeView(getApplicationContext());

                //myScaleView.setImageURI(Uri.parse((String) images.get(position)));
                MyApplication.LoadImageBySize(Uri.parse((String) images.get(position)),myScaleView,
                        (int) Config.WIDTH,(int) Config.WIDTH);
                myScaleView.setPosition(position);
                container.addView(myScaleView);

                GenericDraweeHierarchy hierarchy = myScaleView.getHierarchy();
                hierarchy.setProgressBarImage((new ProgressBarDrawable()));
                hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                return myScaleView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {


            }


            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }


        });
        vp.setCurrentItem(currentItem, false);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Position = position;
                nums.setText(position + 1 + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopMenu.show();
            }
        });


    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.image_disPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        nums = (TextView) findViewById(R.id.nums);

        mPopMenu = new PopupMenu(this, fab);
        mPopMenu.getMenuInflater().inflate(R.menu.fabmenu, mPopMenu.getMenu());
        mPopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save: {
                        File image = null;
                        //System.out.println("xxx");
                       image = MyApplication.GetFrescoFile((String) images.get(Position));
                        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                        String path = FileUtils.saveFile(getApplicationContext(),
                                Environment.getExternalStorageDirectory().toString(),
                                System.currentTimeMillis() + ".jpg", bitmap);

                        Snackbar.make(fab, "保存成功！保存位置位于："+path , Snackbar.LENGTH_LONG)
                                .show();

                        break;
                    }

                    case R.id.share: {

                        break;
                    }


                }
                return true;
            }
        });

    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.push_bottom_out);
    }


    public void Finish() {
        finish();
        overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
    }


}
