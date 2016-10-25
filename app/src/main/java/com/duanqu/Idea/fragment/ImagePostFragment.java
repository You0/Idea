package com.duanqu.Idea.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.duanqu.Idea.CustomView.HorizontalScrollViewEx;
import com.duanqu.Idea.CustomView.ImagePostFragmentImageItem;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ImageWatchActivity;
import com.duanqu.Idea.activity.SendActivity;
import com.duanqu.qupai.project.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/13.
 */
public class ImagePostFragment extends BaseFragment implements View.OnClickListener{
    private EditText content;
    public static HorizontalScrollViewEx imageHorizon;
    public static HashMap<String, View> views = new HashMap<>();
    private ArrayList<String> path;
    private TextView change;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_post_fragment, null);
        content = (EditText) view.findViewById(R.id.content);
        imageHorizon = (HorizontalScrollViewEx) view.findViewById(R.id.imageHorizon);
        change = (TextView) view.findViewById(R.id.change);
        change.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        imageHorizon.removeAllViews();
        //往imageHorizon里添加images
        if (path != null) {
            for (int i = 0; i < path.size(); i++) {
                ImagePostFragmentImageItem imagePostFragmentImageItem = new ImagePostFragmentImageItem(getActivity());
                imagePostFragmentImageItem.setImagepath(path.get(i));
                views.put(path.get(i), imagePostFragmentImageItem.getView());
                imageHorizon.addView(imagePostFragmentImageItem.getView());
            }
        }
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }

    @Override
    public void onClick(View v) {
        Log.e("click","click");
        switch (v.getId()){
            case R.id.change:{
                Log.e("click","change");
                Intent intent = new Intent(getActivity(), ImageWatchActivity.class);
                intent.putStringArrayListExtra("selected",path);
                startActivityForResult(intent,((SendActivity)getActivity()).IMAGE_CHANGE);
                break;
            }
        }
    }
}

