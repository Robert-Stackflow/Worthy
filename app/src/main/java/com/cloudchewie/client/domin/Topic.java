/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Topic implements Serializable {
    String name;
    String describe;
    int hotvalue;
    int followerCount;

    public Topic() {
    }

    public Topic(String name, String describe, int hotvalue, int followerCount) {
        this.name = name;
        this.hotvalue = hotvalue;
        this.describe = describe;
        this.followerCount = followerCount;
    }

    public int getHotvalue() {
        return hotvalue;
    }

    public void setHotvalue(int hotvalue) {
        this.hotvalue = hotvalue;
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
        return "Topic{" +
                "name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", followerCount=" + followerCount +
                '}';
    }
}
