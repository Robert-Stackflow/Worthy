package com.cloudchewie.client.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.util.enumeration.ResponseCode;
import com.cloudchewie.client.util.http.HttpRequestUtil;

import java.util.List;

public class AttractionListRequest {
    public List<Attraction> find(String key) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/list/find/" + key));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC102.getCode())
            return response[0].getJSONArray("data").toJavaList(Attraction.class);
        return null;
    }

    public List<Attraction> recommend() {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/list/recommend/"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC102.getCode())
            return response[0].getJSONArray("data").toJavaList(Attraction.class);
        return null;
    }
}
