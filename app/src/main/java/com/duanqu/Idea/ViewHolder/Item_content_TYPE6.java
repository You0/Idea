package com.duanqu.Idea.ViewHolder;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.test.GridViewAdapter;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/11.
 */
public class Item_content_TYPE6 extends MainMessageBaseViewHolder {
    private TextView content;
    private String tempStr;

    @Override
    protected void bindData(MainMessageBean data) {
        tempStr = data.getTextContent();
        content.setText(tempStr);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.item_content_type6;
    }

    @Override
    protected void FindView(View parent) {
        content = (TextView) parent.findViewById(R.id.content);
    }
}
