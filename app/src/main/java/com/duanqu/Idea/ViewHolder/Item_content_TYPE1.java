package com.duanqu.Idea.ViewHolder;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.CustomProgressBar;
import com.duanqu.Idea.CustomView.MImageView;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ImageDisplay;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.MainMessageBean;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/11.
 */
public class Item_content_TYPE1 extends MainMessageBaseViewHolder {
    private TextView content;
    private mSimpleDraweeView imageView;
    private String tempStr;
    private ArrayList arrayList = new ArrayList();
    @Override
    protected void bindData(MainMessageBean data) {
        LinkedList linkedList = data.getImages();
        arrayList.clear();
        arrayList.addAll(linkedList);
        tempStr = (String) linkedList.getFirst();
        Uri uri = Uri.parse(tempStr);
        //imageView.setImageURI(uri);
        MyApplication.LoadImageBySize(uri,imageView, (int) Config.WIDTH, (int) Config.HEIGHT/3*2);
        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
        hierarchy.setProgressBarImage(new CustomProgressBar());

        tempStr = data.getTextContent();
        content.setText(tempStr);




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivityContext(), ImageDisplay.class);
                intent.putCharSequenceArrayListExtra("images",arrayList);
                intent.putExtra("position",0);
                getActivityContext().startActivity(intent);
                getActivityContext().overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
            }
        });
    }

    @Override
    protected int getViewLayout() {
        return R.layout.item_content_type1;
    }

    @Override
    protected void FindView(View parent) {
        content = (TextView) parent.findViewById(R.id.content);
        imageView = (mSimpleDraweeView) parent.findViewById(R.id.image1);
    }
}
