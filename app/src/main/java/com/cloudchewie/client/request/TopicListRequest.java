package com.cloudchewie.client.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.util.enumeration.ResponseCode;
import com.cloudchewie.client.util.http.HttpRequestUtil;

import java.util.List;

public class TopicListRequest {
    public List<Topic> find(String key) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/list/find/" + key));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC102.getCode())
            return response[0].getJSONArray("data").toJavaList(Topic.class);
        return null;
    }

    public List<Topic> recommend() {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/list/recommend/"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC102.getCode())
            return response[0].getJSONArray("data").toJavaList(Topic.class);
        return null;
    }
}
