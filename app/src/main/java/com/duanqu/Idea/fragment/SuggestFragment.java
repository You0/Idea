package com.duanqu.Idea.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Adapter.SuggestGridAdapter;
import com.duanqu.Idea.CustomView.MyAdvertisement;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.SuggestGridViewHolder;
import com.duanqu.Idea.bean.SuggestGridBean;
import com.duanqu.Idea.bean.TopAdBean;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/26.
 */
public class SuggestFragment extends BaseFragment {
    private MyAdvertisement advertisement;
    private View view;
    private GridView tj_video;
    private GridView new_video;
    private SimpleDraweeView company;
    private TextView company_js;
    private ArrayList<TopAdBean> datas = new ArrayList<>();
    private ArrayList<SuggestGridBean> tj_videos = new ArrayList<>();
    private ArrayList<SuggestGridBean> new_videos = new ArrayList<>();
    private String companyImage;
    private String companyText;

    public void setDatas(ArrayList<TopAdBean> datas) {
        this.datas = datas;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tj_layout,null);

        advertisement = (MyAdvertisement) view.findViewById(R.id.my_AD);
        tj_video = (GridView) view.findViewById(R.id.tj_video);
        new_video = (GridView) view.findViewById(R.id.new_video);
        company = (SimpleDraweeView) view.findViewById(R.id.company);
        company_js = (TextView) view.findViewById(R.id.company_js);
        //company = (ListView) view.findViewById(R.id.company);



        initData();


        return view;
    }

    private void initData() {
        //下面都是测试
        datas.add(Datas.temp);

        datas.add(Datas.temp2);

        datas.add(Datas.temp1);
        datas.add(Datas.temp3);
        advertisement.setData(datas);
        advertisement.Init();

        tj_video.setAdapter(new SuggestGridAdapter(getActivity(),
                new BaseAdapter.Builder<SuggestGridBean>().addType(0,
                        SuggestGridViewHolder.class).setDatas(Datas.tjGridBeens)));


        new_video.setAdapter(new SuggestGridAdapter(getActivity(),
                new BaseAdapter.Builder<SuggestGridBean>().addType(0,
                        SuggestGridViewHolder.class).setDatas(Datas.suggestGridBeens)));


        company.setImageURI(Uri.parse(Datas.companyImage));
        company_js.setText(Datas.companyText);


    }


}
