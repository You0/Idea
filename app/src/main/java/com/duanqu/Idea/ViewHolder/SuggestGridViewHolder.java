package com.duanqu.Idea.ViewHolder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.CustomView.mSimpleDraweeView;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.bean.SuggestGridBean;

/**
 * Created by Administrator on 2016/7/27.
 */
public class SuggestGridViewHolder extends BaseItemImp<MainMessageBean> {
    private mSimpleDraweeView DraweeView;
    private TextView text;

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MainMessageBean data, int dynamicType) {
        try{
            DraweeView.setImageURI(Uri.parse((String) data.getImages().get(0)));
            text.setText(data.getTextContent());
        }catch (Exception e){
            e.printStackTrace();
        }
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
