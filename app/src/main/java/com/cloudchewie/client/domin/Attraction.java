/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

import com.cloudchewie.client.util.image.ImageUrlUtil;

import java.io.Serializable;
import java.util.List;

public class Attraction implements Serializable {
    String name;
    String location;
    String describe;
    //TODO 无效,引用为userId
    int authorId;
    double latitude;
    double longtitude;
    int goneCount;
    int recommendCount;
    int wantCount;
    String coverImageUrl;
    List<String> tags;

    public Attraction() {
    }

    public Attraction(String name, String location, String describe, int authorId, double longtitude, double latitude, int wantCount, int recommendCount, int goneCount, List<String> tags) {
        this.name = name;
        this.location = location;
        this.describe = describe;
        this.authorId = authorId;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.wantCount = wantCount;
        this.recommendCount = recommendCount;
        this.goneCount = goneCount;
        this.tags = tags;
        coverImageUrl = ImageUrlUtil.getUrls(1).get(0);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatistics() {
        return recommendCount + "人推荐 · " + goneCount + "人看过 · " + wantCount + "人想去";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public int getWantCount() {
        return wantCount;
    }

    public void setWantCount(int wantCount) {
        this.wantCount = wantCount;
    }

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }

    public int getGoneCount() {
        return goneCount;
    }

    public void setGoneCount(int goneCount) {
        this.goneCount = goneCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Attraction{" + "name='" + name + '\'' + ", location='" + location + '\'' + ", detail='" + describe + '\'' + ", authorId=" + authorId + ", longtitude=" + longtitude + ", latitude=" + latitude + ", followerCount=" + wantCount + ", visitorCount=" + recommendCount + ", postCount=" + goneCount + '}';
    }
}
