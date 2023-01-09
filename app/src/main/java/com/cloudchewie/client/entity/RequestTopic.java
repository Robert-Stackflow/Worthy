package com.cloudchewie.client.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class RequestTopic implements Serializable {
    Topic topic;
    String reason;
    List<String> imageUrls;

    public RequestTopic() {
    }

    public RequestTopic(Topic topic, String reason, List<String> imageUrls) {
        this.topic = topic;
        this.reason = reason;
        this.imageUrls = imageUrls;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @NonNull
    @Override
    public String toString() {
        return "RequestTopic{" +
                "topic=" + topic +
                ", reason='" + reason + '\'' +
                ", imageUrls='" + imageUrls + '\'' +
                '}';
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
