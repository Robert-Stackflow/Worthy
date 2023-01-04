package com.cloudchewie.client.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.client.domin.Article;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.domin.Favorites;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.domin.User;
import com.cloudchewie.client.util.enumeration.ResponseCode;
import com.cloudchewie.client.util.http.HttpRequestUtil;

import java.util.List;

public class UserProfileRequest {
    public List<User> getFollowers(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/followers/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getJSONArray("data").toJavaList(User.class);
        return null;
    }

    public List<Attraction> getVisitedAttractions(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/visited/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getJSONArray("data").toJavaList(Attraction.class);
        return null;
    }

    public List<Attraction> getWantAttractions(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/want/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getJSONArray("data").toJavaList(Attraction.class);
        return null;
    }

    public List<String> getAlbum(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/album/" + userId));
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

    public List<Post> getPosts(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/posts/" + userId));
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

    public List<Article> getArticles(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/articles/" + userId));
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

    public List<Favorites> getFavorites(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/favorites/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getJSONArray("data").toJavaList(Favorites.class);
        return null;
    }

    public int getRelation(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/relation/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC100.getCode())
            return response[0].getIntValue("data");
        return -1;
    }

}
