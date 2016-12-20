package com.duanqu.Idea.ViewHolder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseItemImp;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ChatActivity;
import com.duanqu.Idea.bean.ParallaxOtherUserBean;
import com.duanqu.Idea.bean.ParallaxUserItem1;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ParallaxUser_ITEM2 extends BaseItemImp<ParallaxOtherUserBean>{
    private ImageView email;
    private TextView love_count;
    private TextView contact_count;
    private Button contact;

    @Override
    public int getViewRes() {
        return R.layout.parallax_user_item2;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        email = (ImageView) parent.findViewById(R.id.email);
        love_count= (TextView) parent.findViewById(R.id.love_count);
        contact_count = (TextView) parent.findViewById(R.id.love_count);
        contact = (Button) parent.findViewById(R.id.contact);
    }


    @Override
    protected void bindData(int position, @NonNull View v, @NonNull final ParallaxOtherUserBean data, int dynamicType) {
        love_count.setText(String.valueOf(data.getCountLove()));
        contact_count.setText(String.valueOf(data.getContactsCount()));
        if(data.getAlready()){
            contact.setText("已关注");
            contact.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.huise));
        }

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item2",data.toString());
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("username",data.getUsername());
                intent.putExtra("nickname",data.getNickname());
                intent.putExtra("userhead",data.getHeadurl());
                context.startActivity(intent);
            }
        });

    }
}
