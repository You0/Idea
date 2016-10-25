package com.duanqu.Idea.activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.duanqu.Idea.Adapter.PopListViewAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Me on 2016/3/12 0012.
 */
public class ListImageDirPopupWindow extends PopupWindow{
    private ListView mListDir;
    private ArrayList<ImageWatchActivity.ImageFloder> mDatas;
    private Activity activity;
    private View view;
    public ListImageDirPopupWindow(Activity activity, ArrayList<ImageWatchActivity.ImageFloder> Datas)
    {
        this.activity = activity;
        this.mDatas = Datas;

        view = LayoutInflater.from(activity).inflate(R.layout.listview,null);
        mListDir = (ListView) view.findViewById(R.id.mListDir);

        initViews();
        initEvents();
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        setHeight(700);
        setFocusable(true);
        setOutsideTouchable(true);
        Drawable drawable = new ColorDrawable(0xffffff);
        setBackgroundDrawable(drawable);
    }




    public void initViews() {
        mListDir.setAdapter(new PopListViewAdapter(activity,R.layout.list_dir_item,mDatas));
    }

    public interface OnImageDirSelected
    {
        void selected(ImageWatchActivity.ImageFloder floder);
    }

    private OnImageDirSelected imageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
    {
        imageDirSelected = mImageDirSelected;
    }

    public void initEvents() {
        mListDir.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

                if (imageDirSelected != null)
                {
                    imageDirSelected.selected(mDatas.get(position));
                }
            }
        });

    }

    public void init() {

    }
}
