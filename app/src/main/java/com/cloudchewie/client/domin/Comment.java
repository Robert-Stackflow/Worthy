/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import android.media.Image;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Comment implements Serializable {
    int userId;
    String username;
    Date date;
    String content;
    List<Image> images;
    int replyCount;
    int thumbupCount;
    COMMENT_TYPE type;

    public Comment() {
    }

    public Comment(int userId, String username, Date date, String content, int replyCount, int thumbupCount) {
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.content = content;
        this.replyCount = replyCount;
        this.thumbupCount = thumbupCount;
    }

    public int getUserId() {
        return userId;
    }

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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public COMMENT_TYPE getType() {
        return type;
    }

    public void setType(COMMENT_TYPE type) {
        this.type = type;
    }

    enum COMMENT_TYPE {
        TEXT,
        TEXT_IMAGE
    }
}
