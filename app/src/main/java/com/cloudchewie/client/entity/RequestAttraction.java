package com.cloudchewie.client.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class RequestAttraction implements Serializable {
    Attraction attraction;
    String reason;
    List<String> imageUrls;

    public RequestAttraction(Attraction attraction, String reason, List<String> imageUrls) {
        this.attraction = attraction;
        this.reason = reason;
        this.imageUrls = imageUrls;
    }

    public RequestAttraction() {
    }

    @NonNull
    @Override
    public String toString() {
        return "RequestAttraction{" +
                "attraction=" + attraction +
                ", reason='" + reason + '\'' +
                ", imageUrls='" + imageUrls + '\'' +
                '}';
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
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
