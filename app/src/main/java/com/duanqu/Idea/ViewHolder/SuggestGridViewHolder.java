package com.duanqu.Idea.ViewHolder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.SuggestGridBean;

/**
 * Created by Administrator on 2016/7/27.
 */
public class SuggestGridViewHolder extends BaseItemImp<SuggestGridBean> {
    private mSimpleDraweeView DraweeView;
    private TextView text;

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull SuggestGridBean data, int dynamicType) {
        DraweeView.setImageURI(Uri.parse(data.getCoverImage()));
        text.setText(data.getTextInfo());

        //其余处理在下面编写
    }

    @Override
    public int getViewRes() {
        return R.layout.suggest_grid_item;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        DraweeView = (mSimpleDraweeView) parent.findViewById(R.id.video);
        text = (TextView) parent.findViewById(R.id.text);

    }
}
