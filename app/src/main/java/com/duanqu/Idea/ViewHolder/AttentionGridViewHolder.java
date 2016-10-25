package com.duanqu.Idea.ViewHolder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.AttentionBean;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/10/15.
 */
public class AttentionGridViewHolder extends BaseItemImp<AttentionBean> {
    private mSimpleDraweeView userhead;
    private TextView realname;
    private CheckBox checkbox;

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull AttentionBean data, int dynamicType) {
        if(!data.getUrl().equals("")){
            Log.e("xxx","设置头像");
            userhead.setImageURI(Uri.parse(data.getUrl()));
        }
        realname.setText(data.getName());
        checkbox.setChecked(data.getChecked());
    }

    @Override
    public int getViewRes() {
        return R.layout.attention_grid_item;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        userhead = (mSimpleDraweeView) parent.findViewById(R.id.userhead);
        realname = (TextView) parent.findViewById(R.id.realname);
        checkbox = (CheckBox) parent.findViewById(R.id.checkbox);
    }



}
