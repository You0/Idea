package com.duanqu.Idea.ViewHolder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.FriendsListBean;

/**
 * Created by Administrator on 2016/9/14.
 */
public class Friends_TYPE0 extends BaseItemImp<FriendsListBean> {
    private mSimpleDraweeView simpleDraweeView;
    private TextView textView;
    private CheckBox checkBox;

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull FriendsListBean data, int dynamicType) {
        simpleDraweeView.setImageURI(Uri.parse(data.getUrl()));
        textView.setText(data.getName());

        
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox.isChecked()){
                    checkBox.setChecked(true);
                }else{
                    checkBox.setChecked(false);
                }
            }
        });
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
