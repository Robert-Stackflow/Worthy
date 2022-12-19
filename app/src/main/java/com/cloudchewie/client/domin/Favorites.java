/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 12:09:14
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import java.io.Serializable;
import java.util.Date;

public class Favorites implements Serializable {
    String name;
    String username;
    String describe;
    Date createTime;
    boolean isPublic;
    int thumbupCount;
    int itemCount;
    int subscribeCount;
    int visitorCount;

    public Favorites() {
    }

    public Favorites(String name, String username, String describe, Date createTime, boolean isPublic, int thumbupCount, int itemCount, int subscribeCount, int visitorCount) {
        this.name = name;
        this.username = username;
        this.describe = describe;
        this.createTime = createTime;
        this.isPublic = isPublic;
        this.thumbupCount = thumbupCount;
        this.itemCount = itemCount;
        this.subscribeCount = subscribeCount;
        this.visitorCount = visitorCount;
    }

    public int getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(int visitorCount) {
        this.visitorCount = visitorCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(int subscribeCount) {
        this.subscribeCount = subscribeCount;
    }
}
