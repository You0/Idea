package com.duanqu.Idea.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqu.Idea.CustomView.CustomProgressBar;
import com.duanqu.Idea.CustomView.MyScaleSimpleDraweeView;
import com.duanqu.Idea.R;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;

/**
 * Created by Administrator on 2016/7/22.
 */
public class Image_Display_Fragment extends BaseFragment {
    private MyScaleSimpleDraweeView myScaleSimpleDraweeView;
    private Uri uri;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_display_item,null);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        myScaleSimpleDraweeView = (MyScaleSimpleDraweeView) view.findViewById(R.id.the_image);
        myScaleSimpleDraweeView.setImageURI(Uri.parse(bundle.getString("uri")));
        GenericDraweeHierarchy hierarchy = myScaleSimpleDraweeView.getHierarchy();
//
////        System.out.println("uri"+bundle.getString("uri"));
//
        hierarchy.setProgressBarImage(new CustomProgressBar());
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);

        return view;
    }

}
