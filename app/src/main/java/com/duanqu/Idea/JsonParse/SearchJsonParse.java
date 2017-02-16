package com.duanqu.Idea.JsonParse;

import com.duanqu.Idea.bean.CommentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Me on 2017/1/22.
 */

public class SearchJsonParse {

    public ArrayList<CommentBean> Parse(String json)
    {
        CommentBean bean = null;
        ArrayList<CommentBean> beans = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object =jsonArray.getJSONObject(i);
                bean = new CommentBean();
                bean.setFeedId(String.valueOf(object.getInt("feedId")));
                bean.setOwnerId(object.getString("ownerId"));
                bean.setTime(object.getString("timestamp"));
                bean.setContent(object.getString("content"));
                bean.setType(object.getInt("type"));
                bean.setVideourl(object.getString("videoUrl"));
                
                JSONArray array = object.getJSONArray("imageUrls");
                ArrayList<String> images = new ArrayList<>();
                for(int j=0;j<array.length();j++)
                {
                    images.add((String) array.get(j));
                }
                bean.setImages(images);

                beans.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return beans;
    }

}
