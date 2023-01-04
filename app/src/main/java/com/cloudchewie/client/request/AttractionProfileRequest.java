package com.cloudchewie.client.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.client.domin.Article;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.util.enumeration.ResponseCode;
import com.cloudchewie.client.util.http.HttpRequestUtil;

import java.util.List;

public class AttractionProfileRequest {
    public Attraction info(int attractionId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_FORM, "/attraction/profile/info"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response[0].getJSONObject("data").toJavaObject(Attraction.class);
    }

    public List<String> getAlbum(int attractionId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/profile/album/" + attractionId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getJSONArray("data").toJavaList(String.class);
        return null;
    }

    public List<Post> getPosts(int attractionId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/profile/posts/" + attractionId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getJSONArray("data").toJavaList(Post.class);
        return null;
    }

    public List<Article> getArticles(int attractionId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/profile/articles/" + attractionId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getJSONArray("data").toJavaList(Article.class);
        return null;
    }

}
