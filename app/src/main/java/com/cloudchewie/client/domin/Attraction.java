package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Attraction implements Serializable {
    String name;
    String location;
    String detail;
    int authorId;
    double longtitude;
    double latitude;
    int followerCount;
    int visitorCount;
    int postCount;

    public Attraction() {
    }

    public Attraction(String name, String location, String detail, int authorId, double longtitude, double latitude, int followerCount, int visitorCount, int postCount) {
        this.name = name;
        this.location = location;
        this.detail = detail;
        this.authorId = authorId;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.followerCount = followerCount;
        this.visitorCount = visitorCount;
        this.postCount = postCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(int visitorCount) {
        this.visitorCount = visitorCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Attraction{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", detail='" + detail + '\'' +
                ", authorId=" + authorId +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                ", followerCount=" + followerCount +
                ", visitorCount=" + visitorCount +
                ", postCount=" + postCount +
                '}';
    }
}