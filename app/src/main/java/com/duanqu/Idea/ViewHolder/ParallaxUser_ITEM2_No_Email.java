package com.duanqu.Idea.ViewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.bean.ParallaxOtherUserBean;
import com.duanqu.Idea.bean.ParallaxUserItem1;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUser_ITEM2_No_Email extends BaseItemImp<ParallaxOtherUserBean>{
    private TextView love_count;
    private TextView contact_count;
    private TextView rev_contact_count;

    @Override
    public int getViewRes() {
        return R.layout.parallax_user_item2_no_email;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        rev_contact_count = (TextView) parent.findViewById(R.id.revcontact_count);
        love_count= (TextView) parent.findViewById(R.id.love_count);
        contact_count = (TextView) parent.findViewById(R.id.contact_count);

    }


    @Override
    protected void bindData(int position, @NonNull View v, @NonNull ParallaxOtherUserBean data, int dynamicType) {
        love_count.setText(String.valueOf(data.getCountLove()));
        contact_count.setText(String.valueOf(data.getContactsCount()));
        rev_contact_count.setText(String.valueOf(data.getRevcontactsCount()));
    }
}
