package com.cloudchewie.client.entity;

import android.os.Bundle;

import java.util.Date;

public class Draft {
    Object object;
    Bundle bundle;
    Date createTime;
    Date lastSaveTime;
    DRAFT_TYPE type;

    public Draft() {
    }

    public Draft(Object object, Bundle bundle, Date createTime, Date lastSaveTime, DRAFT_TYPE type) {
        this.object = object;
        this.bundle = bundle;
        this.createTime = createTime;
        this.lastSaveTime = lastSaveTime;
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public Draft setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Draft setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getLastSaveTime() {
        return lastSaveTime;
    }

    public Draft setLastSaveTime(Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
        return this;
    }

    public DRAFT_TYPE getType() {
        return type;
    }

    public Draft setType(DRAFT_TYPE type) {
        this.type = type;
        return this;
    }

    public enum DRAFT_TYPE {
        POST,
        ARTICLE,
        ATTRACTION,
        TOPIC
    }
}
