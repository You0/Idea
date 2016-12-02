package com.duanqu.Idea.ViewHolder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.CustomView.UserTag;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.SendActivity;
import com.duanqu.Idea.bean.FriendsListBean;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;

/**
 * Created by Administrator on 2016/9/14.
 */
public class Friends_TYPE0 extends BaseItemImp<FriendsListBean> {
    private mSimpleDraweeView simpleDraweeView;
    private TextView textView;
    private CheckBox checkBox;
    private FriendsListBean data;
    @Override
    protected void bindData(int position, @NonNull View v, @NonNull final FriendsListBean data, int dynamicType) {
        //System.out.println("消息获得:"+data.getName()+data.getUrl());

        //this.data = data;
        simpleDraweeView.setImageURI(Uri.parse(data.getUrl()));
        GenericDraweeHierarchy hier = simpleDraweeView.getHierarchy();
        hier.setRoundingParams(RoundingParams.asCircle());
        textView.setText(data.getName());
        //checkBox.setChecked(data.getIsChecked());

        //在这里把已经checked的数据给打上勾。

        if(SendActivity.getViews().contains(data.getName()))
        {
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    System.out.println("set check");
                    checkBox.setChecked(true);
                    SendActivity.setViews(data.getUsername(),UserTag.Build(context,data.getUrl(),data.getName()));

                }else{
                    System.out.println("set false");
                    checkBox.setChecked(false);
                    SendActivity.removeViews(data.getUsername());

                }
            }
        });
    }

    public void SetChecked()
    {


    }



    @Override
    public int getViewRes() {
        return R.layout.friends_list;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        simpleDraweeView = (mSimpleDraweeView) parent.findViewById(R.id.userhead);
        textView = (TextView) parent.findViewById(R.id.uid);
        checkBox = (CheckBox) parent.findViewById(R.id.checkbox);

    }
}
