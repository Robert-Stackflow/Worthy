package com.cloudchewie.client.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.client.domin.PoiItem;
import com.cloudchewie.client.util.http.HttpRequestUtil;
import com.cloudchewie.client.util.map.BaiduRequestBuilder;

import java.util.List;

public class MapRequest {
    public static List<PoiItem> request(String url) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.get(HttpRequestUtil.MEDIA_TYPE_JSON, url));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JSONObject.parseArray(response[0].getJSONArray("results").toJSONString(), PoiItem.class);
    }

    public static List<PoiItem> request(double lat, double lng, String query) {
        String url = BaiduRequestBuilder.centroidOf(lat, lng).query(query).tag("旅游景点").radius(3000).build();
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.get(HttpRequestUtil.MEDIA_TYPE_JSON, url));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JSONObject.parseArray(response[0].getJSONArray("results").toJSONString(), PoiItem.class);
    }
}
