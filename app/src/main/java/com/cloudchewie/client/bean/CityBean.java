package com.cloudchewie.client.bean;

import androidx.annotation.NonNull;

import com.cloudchewie.client.util.basic.CharacterUtil;

public class CityBean {
    int tag;
    String name;

    public CityBean(int tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "CityBean{" +
                "tag=" + tag +
                ", name='" + name + '\'' +
                '}';
    }

    public String getPinyin() {
        return CharacterUtil.getPinYin(name).toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
