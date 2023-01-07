/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Topic implements Serializable {
    String name;
    String describe;
    //TODO 无效,引用为userId
    int authorId;
    int visitorCount;
    int followerCount;
    FOLLOW_TYPE myType;

    public Topic() {
    }

    public Topic(String name, String describe, int visitorCount, int followerCount, FOLLOW_TYPE type) {
        this.myType = type;
        this.name = name;
        this.visitorCount = visitorCount;
        this.describe = describe;
        this.followerCount = followerCount;
    }

    public FOLLOW_TYPE getMyType() {
        return myType;
    }

    public void setMyType(FOLLOW_TYPE myType) {
        this.myType = myType;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Topic{" + "name='" + name + '\'' + ", describe='" + describe + '\'' + ", followerCount=" + followerCount + '}';
    }

    public enum FOLLOW_TYPE {
        UNFOLLOWED, FOLLOWED
    }
}
