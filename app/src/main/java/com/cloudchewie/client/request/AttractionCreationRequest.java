package com.cloudchewie.client.request;

import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.util.http.HttpRequestUtil;

public class AttractionCreationRequest {
    public void publish(Attraction attraction) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/creation/publish/", attraction)).start();
    }

    public void update(Attraction attraction) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/creation/update/", attraction)).start();
    }
}
