package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.TopAdBean;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/23.
 */
public class MyAdvertisement extends FrameLayout {
    private ArrayList<TopAdBean> data;
    private Context context;
    private ViewPager vp;
    private LinearLayout ll_dot;
    private List<ImageView> iv_dots;
    private List<View> views;
    private int delayTime = 2000;
    private int currentItem;
    private boolean isAutoPlay;
    private Handler handler = new Handler();
    public void setData(ArrayList data) {
        this.data = data;
    }



    public MyAdvertisement(Context context) {
        super(context);
    }

    public MyAdvertisement(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAdvertisement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void Init()
    {
        views = new ArrayList<View>();
        iv_dots = new ArrayList<ImageView>();
        views.clear();
        try {
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initUI() throws Exception {
        context = getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.ad_layout, this, true);
        vp = (ViewPager) view.findViewById(R.id.vp);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        ll_dot.removeAllViews();

        if(data.size()==0){
            throw  new Exception("数据还未初始化！");
        }


        int len = data.size();
        for (int i = 0; i < len; i++) {
            ImageView iv_dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            ll_dot.addView(iv_dot, params);
            iv_dots.add(iv_dot);
        }

        for (int i = 0; i <= len + 1; i++) {
            String info;
            View fm = LayoutInflater.from(context).inflate(
                    R.layout.ad_item, null);
            SimpleDraweeView iv = (SimpleDraweeView) fm.findViewById(R.id.iv_title);
            TextView tv_title = (TextView) fm.findViewById(R.id.tv_title);
            iv.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
//            iv.setBackgroundResource(R.drawable.loading1);
            if (i == 0) {
                //如果为0，加载最后一张
                info = data.get(len-1).getInfo();
                iv.setImageURI(Uri.parse(data.get(len-1).getImage()));
                tv_title.setText(data.get(len - 1).getText());
            } else if (i == len + 1) {
                //超出2张加载第一张
                info = data.get(0).getInfo();
                iv.setImageURI(Uri.parse(data.get(0).getImage()));
                tv_title.setText(data.get(0).getText());
            } else {
                //其余的加载前一张
                info = data.get(i-1).getInfo();
                iv.setImageURI(Uri.parse(data.get(i-1).getImage()));
                tv_title.setText(data.get(i-1).getText());
            }
            views.add(fm);
            fm.setTag(info);
            fm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String info = (String) v.getTag();
                    //点击事件在下面处理


                }
            });
        }
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
        vp.setFocusable(true);
        vp.setCurrentItem(1);
        currentItem = 1;
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < iv_dots.size(); i++) {
                    if (i == position - 1) {
                        iv_dots.get(i).setImageResource(R.drawable.dot_focus);
                    } else {
                        iv_dots.get(i).setImageResource(R.drawable.dot_blur);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:
                        isAutoPlay = false;
                        break;
                    case 2:
                        isAutoPlay = true;
                        break;
                    case 0:
                        if (vp.getCurrentItem() == 0) {
                            vp.setCurrentItem(data.size(), false);
                        } else if (vp.getCurrentItem() == data.size() + 1) {
                            vp.setCurrentItem(1, false);
                        }
                        currentItem = vp.getCurrentItem();
                        isAutoPlay = true;
                        break;
                }

            }
        });
        startPlay();
    }
    private void startPlay() {
        isAutoPlay = true;
        handler.postDelayed(task, 3000);
    }


    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            if (isAutoPlay) {
                currentItem = currentItem % (data.size() + 1) + 1;
                if (currentItem == 1) {
                    vp.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    vp.setCurrentItem(currentItem);
                    handler.postDelayed(task, 5000);
                }
            } else {
                handler.postDelayed(task, 5000);
            }
        }
    };
}
