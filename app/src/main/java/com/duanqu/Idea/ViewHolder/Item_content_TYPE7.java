package com.duanqu.Idea.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.Adapter.InnerFeedAdapter;
import com.duanqu.Idea.CustomView.HWEUQALListview;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.FeedActivity;
import com.duanqu.Idea.activity.ParallaxOtherUserInfoDisplayActivity;
import com.duanqu.Idea.bean.FeedBean;
import com.duanqu.Idea.bean.InnerFeedBean;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.utils.ListViewUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/30.
 */
public class Item_content_TYPE7 extends BaseItemImp<MainMessageBean>{
    private TextView content;
    private HWEUQALListview attach_comments;
    private SimpleDraweeView messageHead;
    private TextView username;
    private TextView sign;
    private TextView time;
    private HashMap userInfo;
    private InnerFeedAdapter adapter;
    private ArrayList<InnerFeedBean> mInnerBeans;
    String FeedId;
    String LeftName;


    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MainMessageBean data, int dynamicType) {
        String url = (String) data.getUserInfo().get("headurl");
        Uri uri = Uri.parse(url);
        SetCircleImage(messageHead,url);
        //messageHead.setImageURI(uri);
        userInfo = data.getUserInfo();
        username.setText((String)userInfo.get("nickname"));
        //sign.setText((String)userInfo.get("sign"));
        content.setText(data.getTextContent());
        mInnerBeans = data.getInnerBeans();
        adapter = new InnerFeedAdapter(getActivityContext(),mInnerBeans);
        attach_comments.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(attach_comments);


        attach_comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.e("Item_content_TYPE7","position" + position);
                InnerFeedBean bean =  mInnerBeans.get(position);
                LeftName = bean.getLeftusername();
                FeedId = bean.getFeedId();
                FeedActivity.send_edit.setFocusable(true);
                InputMethodManager inputManager =
                        (InputMethodManager)FeedActivity.send_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(FeedActivity.send_edit, 0);

            }
        });


        messageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivityContext(), ParallaxOtherUserInfoDisplayActivity.class);
                intent.putExtra("nickname", String.valueOf((String)userInfo.get("nickname")));
                getActivityContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getViewRes() {
        return R.layout.item_content_type7;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        content = (TextView) parent.findViewById(R.id.content);
        attach_comments = (HWEUQALListview) parent.findViewById(R.id.attach_comments);
        messageHead = (SimpleDraweeView) parent.findViewById(R.id.messageHead);
        username = (TextView) parent.findViewById(R.id.username);
        //sign = (TextView) parent.findViewById(R.id.sign);
        attach_comments = (HWEUQALListview) parent.findViewById(R.id.attach_comments);
    }


    private  void SetCircleImage(SimpleDraweeView image,String url) {
        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        rp.setBorder(Color.BLACK, 1);
        //获取GenericDraweeHierarchy对象
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.
                newInstance(getActivityContext().getResources()).setRoundingParams(rp).setFadeDuration(300).build();
        image.setHierarchy(hierarchy);
        image.setImageURI(Uri.parse(url));
    }
}
