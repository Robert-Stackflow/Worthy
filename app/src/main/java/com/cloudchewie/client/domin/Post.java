/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

import com.cloudchewie.client.util.image.ImageUrlUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable {
    int userId;
    String username;
    Date date;
    String content;
    List<String> imageUrls;
    int commentCount;
    int thumbupCount;
    int collectionCount;
    String location;
    String tag;
    POST_TYPE type;

    public Post() {
    }

    public Post(int userId, String username, Date date, String content, int commentCount, int thumbupCount, int collectionCount, String location, String tag) {
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.content = content;
        this.commentCount = commentCount;
        this.thumbupCount = thumbupCount;
        this.collectionCount = collectionCount;
        this.location = location;
        this.tag = tag;
        imageUrls = ImageUrlUtil.getUrls(15);
    }

    public POST_TYPE getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImages(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @NonNull
    @Override
    public String toString() {
        return "Post{" + "userId=" + userId + ", username='" + username + '\'' + ", date=" + date + ", content='" + content + '\'' + ", imageUrls=" + imageUrls + ", commentCount=" + commentCount + ", thumbupCount=" + thumbupCount + ", location='" + location + '\'' + ", tag='" + tag + '\'' + '}';
    }

    enum POST_TYPE {
        TEXT, TEXT_IMAGE, TEXT_WEB, VIDEO
    }
}
