package com.duanqu.Idea.JsonParse;

import com.duanqu.Idea.bean.FeedBean;
import com.duanqu.Idea.bean.InnerFeedBean;
import com.duanqu.Idea.bean.MainMessageBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FeedJsonParse extends BaseJsonParse<MainMessageBean> {

    //无脑解析。。。写这种代码真是烦，，
    public MainMessageBean Parse(String json) {
        try {
            HashMap<String,Object> userInfo = new HashMap<>();
            HashMap<String,Object> messageInfo = new HashMap<>();
            MainMessageBean feedBean = new MainMessageBean();
            JSONObject jsonObject = new JSONObject(json);
            feedBean.setFeed(true);
            //用户的一些信息
            userInfo.put("nickname",jsonObject.getString("despatcherName"));
            userInfo.put("headurl",jsonObject.getString("despatcherAvatar"));
            feedBean.setUserInfo(userInfo);
            //message的一些信息
            messageInfo.put("_id",jsonObject.getString("_id"));
            feedBean.setMessageInfo(messageInfo);
            feedBean.setFeedId(jsonObject.getString("feedId"));
            feedBean.setTextContent(jsonObject.getString("content"));
            JSONArray jsonArray = jsonObject.getJSONArray("attachComments");
            ArrayList<InnerFeedBean> innerFeedBeens = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                InnerFeedBean innerFeedBean = new InnerFeedBean();
                innerFeedBean.setContent(object.getString("content"));
                innerFeedBean.setLeftusername(object.getString("fromName"));
                innerFeedBean.setRightusername(object.getString("toName"));
                innerFeedBean.setFeedId(feedBean.getFeedId());
                innerFeedBeens.add(innerFeedBean);
            }
            feedBean.setInnerBeans(innerFeedBeens);
            feedBean.setType(0);
            return feedBean;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
