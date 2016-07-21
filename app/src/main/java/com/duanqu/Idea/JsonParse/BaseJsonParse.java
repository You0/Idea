package com.duanqu.Idea.JsonParse;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract  class BaseJsonParse<T> {
    protected static JSONObject object;

    public abstract  T Parse(String json);
}
