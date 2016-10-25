package com.duanqu.Idea.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.duanqu.Idea.R;

/**
 * Created by Administrator on 2016/9/13.
 */
public class TextPostFragment extends BaseFragment {
    private EditText content;
    private View view;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.text_post_fragment,null);
        content = (EditText) view.findViewById(R.id.content);

        return view;
    }

    public String getEditorText()
    {
        return content.getText().toString();
    }

}
