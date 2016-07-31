package com.duanqu.Idea.ViewHolder;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.duanqu.Idea.CustomView.CustomProgressBar;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ImageDisplay;
import com.duanqu.Idea.bean.MainMessageBean;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/11.
 */
public class Item_content_TYPE3 extends MainMessageBaseViewHolder {
    private TextView content;
    private mSimpleDraweeView imageView1;
    private mSimpleDraweeView imageView2;
    private mSimpleDraweeView imageView3;
    private mSimpleDraweeView imageView4;
    private int InnerClassPosition;
    private LinkedList<mSimpleDraweeView> images;
    private String tempStr;
    private Uri uri;
    private ArrayList arrayList = new ArrayList();

    @Override
    protected void bindData(MainMessageBean data) {
        tempStr = data.getTextContent();
        content.setText(tempStr);

        LinkedList linkedList = data.getImages();
        arrayList.addAll(linkedList);
        for(int i=0;i<linkedList.size();i++)
        {
            tempStr = (String) linkedList.get(i);
            uri = Uri.parse(tempStr);
            images.get(i).setImageURI(uri);
            GenericDraweeHierarchy hierarchy = images.get(i).getHierarchy();
            hierarchy.setProgressBarImage(new CustomProgressBar());
            images.get(i).setTag(i);
            images.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivityContext(), ImageDisplay.class);
                    intent.putCharSequenceArrayListExtra("images",arrayList);
                    intent.putExtra("position",(int)view.getTag());
                    getActivityContext().startActivity(intent);
                    getActivityContext().overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
                }
            });


        }
    }

    @Override
    protected int getViewLayout() {
        return R.layout.item_content_type3;
    }

    @Override
    protected void FindView(View parent) {
        content = (TextView) parent.findViewById(R.id.content);
        imageView1 = (mSimpleDraweeView) parent.findViewById(R.id.image1);
        imageView2 = (mSimpleDraweeView) parent.findViewById(R.id.image2);
        imageView3 = (mSimpleDraweeView) parent.findViewById(R.id.image3);
        imageView4 = (mSimpleDraweeView) parent.findViewById(R.id.image4);
        images = new LinkedList<>();
        images.add(imageView1);
        images.add(imageView2);
        images.add(imageView3);
        images.add(imageView4);
    }
}
