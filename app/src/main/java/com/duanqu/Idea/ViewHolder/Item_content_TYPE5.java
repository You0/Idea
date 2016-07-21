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
public class Item_content_TYPE5 extends MainMessageBaseViewHolder {
    private GridView gridView;
    private TextView content;
    private String tempStr;
    private GridViewAdapter gridViewAdapter;

    @Override
    protected void bindData(MainMessageBean data) {
        LinkedList images = data.getImages();
        gridViewAdapter = new GridViewAdapter(getActivityContext(),R.layout.griditem,images);
        tempStr = data.getTextContent();
        content.setText(tempStr);
        gridView.setAdapter(gridViewAdapter);
        gridViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getViewLayout() {
        return R.layout.item_content_type5;
    }

    @Override
    protected void FindView(View parent) {
        gridView = (GridView) parent.findViewById(R.id.gridView);
        content = (TextView) parent.findViewById(R.id.content);
    }
}
