package com.cloudchewie.client.request;

import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.util.http.HttpRequestUtil;

public class AttractionOperationRequest {
    public void follow(int attractionId, Attraction.FOLLOW_TYPE type) {
        new Thread(() -> HttpRequestUtil.sendToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/operation/follow/" + attractionId + "/" + type)).start();
    }

    public void unfollow(int attractionId) {
        new Thread(() -> HttpRequestUtil.deleteFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/operation/unfollow/" + attractionId)).start();
    }

    public void evaluate(int attractionId, double score) {
        new Thread(() -> HttpRequestUtil.sendToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/operation/evaluate/" + attractionId + "/" + score)).start();
    }
}
