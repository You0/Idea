package com.duanqu.Idea.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    protected List<T> datas = new ArrayList<>();

    protected HashMap<Integer, Class<? extends BaseItemView<T>>> itemInfos;
    protected Activity context;
    protected LayoutInflater mInflater;

    public BaseAdapter(Activity context, Builder<T> mBuilder) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        datas.clear();
        datas.addAll(mBuilder.datas);
        itemInfos = mBuilder.itemInfos;
    }


    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract int getItemViewType(int position);

    @Override
    public int getViewTypeCount() {
        return 15;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int dynamicType = getItemViewType(position);
        BaseItemView view = null;
        if (convertView == null) {
            Class viewClass = itemInfos.get(dynamicType);
            Log.e("TAG", "" + viewClass);
            try {
                view = (BaseItemView) viewClass.newInstance();
            } catch (InstantiationException e) {
                Log.e("TAG", "反射失败");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Log.e("TAG", "反射失败");
                e.printStackTrace();
            }

            if(view !=null){
                convertView = mInflater.inflate(view.getViewRes(),viewGroup,false);
                convertView.setTag(view);
            }else{
                throw new NullPointerException("view是空的哦~");
            }
        }else{
            view = (BaseItemView) convertView.getTag();
        }

        view.setActivityContext(context);
        view.onFindView(convertView);
        view.onBindData(position,convertView,getItem(position), dynamicType);
        return convertView;
    }


    public static class Builder<T> {
        private HashMap<Integer, Class<? extends BaseItemView<T>>> itemInfos;
        private Activity context;
        private List<T> datas;

        public Builder() {
            itemInfos = new HashMap<>();
        }

        public Builder(List<T> datas) {
            itemInfos = new HashMap<>();
            this.datas = datas;
        }

        public Builder addType(int type, Class<? extends BaseItemView<T>> viewClass) {
            itemInfos.put(type, viewClass);
            return this;
        }

        public Builder setDatas(List<T> datas) {
            this.datas = datas;
            return this;
        }

        public Builder build() {
            return this;
        }


    }


}
