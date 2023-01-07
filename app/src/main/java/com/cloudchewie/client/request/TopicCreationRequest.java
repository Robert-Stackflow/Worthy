package com.cloudchewie.client.request;

import com.cloudchewie.client.entity.Topic;
import com.cloudchewie.client.util.http.HttpRequestUtil;

public class TopicCreationRequest {
    public void publish(Topic topic) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/creation/publish/", topic)).start();
    }

    public void delete(int topicId) {
        new Thread(() -> HttpRequestUtil.deleteFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/creation/delete/" + topicId)).start();
    }

    public void update(Topic topic) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/creation/update/", topic)).start();
    }
}
