package com.duanqu.Idea.ViewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.ParallaxUserItem1;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUser_ITEM1 extends BaseItemImp<ParallaxUserItem1>{
    private ImageView ico;
    private TextView text;
    private TextView count;

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull ParallaxUserItem1 data, int dynamicType) {
        // ico  先不设置了
        ico.setImageDrawable(getActivityContext().getResources().getDrawable(data.getIco()));
        text.setText(data.getTitle());
        count.setText(String.valueOf(data.getCount()));
    }


    @Override
    public int getViewRes() {
        return R.layout.parallax_user_item1;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        ico = (ImageView) parent.findViewById(R.id.ico);
        text= (TextView) parent.findViewById(R.id.text);
        count = (TextView) parent.findViewById(R.id.count);

    }


}
