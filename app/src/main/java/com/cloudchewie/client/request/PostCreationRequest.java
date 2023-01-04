package com.cloudchewie.client.request;

import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.util.http.HttpRequestUtil;

public class PostCreationRequest {
    public void publish(Post post) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/creation/publish/", post)).start();
    }

    public void delete(int postId) {
        new Thread(() -> HttpRequestUtil.deleteFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/creation/delete/" + postId)).start();
    }

    public void update(Post post) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/creation/update/", post)).start();
    }
}
