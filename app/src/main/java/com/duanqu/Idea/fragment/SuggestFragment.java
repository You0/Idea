package com.duanqu.Idea.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.duanqu.Idea.Adapter.BaseAdapter;
import com.duanqu.Idea.Adapter.SuggestGridAdapter;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.MyAdvertisement;
import com.duanqu.Idea.CustomView.ParallaxListview;
import com.duanqu.Idea.JsonParse.MainMessageParse;
import com.duanqu.Idea.R;
import com.duanqu.Idea.ViewHolder.SuggestGridViewHolder;
import com.duanqu.Idea.activity.MoreVideoDisplayActivity;
import com.duanqu.Idea.activity.VideoPlayViewActivity;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.bean.SuggestGridBean;
import com.duanqu.Idea.bean.TopAdBean;
import com.duanqu.Idea.test.Datas;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/26.
 */
public class SuggestFragment extends BaseFragment implements View.OnClickListener{
    private MyAdvertisement advertisement;
    private View view;
    private GridView tj_video;
    private GridView new_video;
    private SimpleDraweeView company;
    private TextView company_js;
    private ArrayList<TopAdBean> datas = new ArrayList<>();
    private ArrayList<MainMessageBean> tj_videos = new ArrayList<>();
    private ArrayList<MainMessageBean> new_videos = new ArrayList<>();
    private String companyImage;
    private String companyText;
    private TextView new_more;
    private TextView tj_more;

    private SuggestGridAdapter tj_adapter;
    private SuggestGridAdapter new_adapter;

    private int Count = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    synchronized (this) {
                        Count++;
                    }
                    if (Count > 1) {
                        if (tj_videos.size() > 2) {
                            for (int i = 2; i < tj_videos.size(); i++) {
                                tj_videos.remove(i);
                            }
                        }
                        if (new_videos.size() > 9) {
                            for (int i = 9; i < new_videos.size(); i++) {
                                new_videos.remove(i);
                            }
                        }
                        tj_adapter.setDatas(tj_videos);
                        new_adapter.setDatas(new_videos);
                        tj_adapter.notifyDataSetChanged();
                        new_adapter.notifyDataSetChanged();
                        Count = 0;
                    }

                    break;
                }
            }


        }
    };



    public void setDatas(ArrayList<TopAdBean> datas) {
        this.datas = datas;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tj_layout, null);

        advertisement = (MyAdvertisement) view.findViewById(R.id.my_AD);
        tj_video = (GridView) view.findViewById(R.id.tj_video);
        new_video = (GridView) view.findViewById(R.id.new_video);
        company = (SimpleDraweeView) view.findViewById(R.id.company);
        company_js = (TextView) view.findViewById(R.id.company_js);
        new_more = (TextView) view.findViewById(R.id.new_more);
        tj_more = (TextView) view.findViewById(R.id.tj_more);
        //company = (ListView) view.findViewById(R.id.company);

        new_more.setOnClickListener(this);
        tj_more.setOnClickListener(this);
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

        tj_adapter = new SuggestGridAdapter(getActivity(),
                new BaseAdapter.Builder<MainMessageBean>().addType(0,
                        SuggestGridViewHolder.class).setDatas(tj_videos));

        tj_video.setAdapter(tj_adapter);

        new_adapter = new SuggestGridAdapter(getActivity(),
                new BaseAdapter.Builder<MainMessageBean>().addType(0,
                        SuggestGridViewHolder.class).setDatas(new_videos));

        new_video.setAdapter(new_adapter);


        company.setImageURI(Uri.parse(Datas.companyImage));
        company_js.setText(Datas.companyText);

        ReciviceFromServer();

        InitGridViewItemClickListener();
    }

    private void InitGridViewItemClickListener() {
        new_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, VideoPlayViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", new_videos.get(i));
                intent.putExtra("data",bundle);
                intent.putExtra("url", Datas.NewestVideoFeed);
                context.startActivity(intent);
            }
        });

        tj_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, VideoPlayViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", tj_videos.get(i));
                intent.putExtra("data",bundle);
                intent.putExtra("url", Datas.NewestVideoFeed);
                context.startActivity(intent);
            }
        });



    }

    private MainMessageParse mainMessageParse = new MainMessageParse();


    private void ReciviceFromServer() {
        OkHttpUtils.get().url(Datas.HotVideoFeed)
                .addParams("userId", Config.userid)
                .addParams("pageNum", "1")
                .addParams("pageSize", "10")
                .addParams("token", "123")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {

                String json = response.body().string();
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    MainMessageBean mainMessageBeen = mainMessageParse.Parse(array.get(i).toString());
                    tj_videos.add(mainMessageBeen);
                }
                handler.sendEmptyMessage(0);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });


        OkHttpUtils.get().url(Datas.NewestVideoFeed)
                .addParams("userId", Config.userid)
                .addParams("pageNum", "1")
                .addParams("pageSize", "10")
                .addParams("token", "123")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    MainMessageBean mainMessageBeen = mainMessageParse.Parse(array.get(i).toString());
                    new_videos.add(mainMessageBeen);
                }
                handler.sendEmptyMessage(0);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.new_more:{
                Intent intent = new Intent(getActivity(), MoreVideoDisplayActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tj_more:{
                //推荐视频的话直接进入更多页面就行了
                Intent intent = new Intent(context, VideoPlayViewActivity.class);
                intent.putExtra("url", Datas.HotVideoFeed);
                context.startActivity(intent);
                break;
            }


        }
    }
}
