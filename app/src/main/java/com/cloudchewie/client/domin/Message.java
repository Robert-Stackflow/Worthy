package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

import java.util.Date;

public class Message {
    int aId;
    int bId;
    int state;//0未读,1已读,-1删除
    int type;
    Date date;
    String content;

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "aId=" + aId +
                ", bId=" + bId +
                ", state=" + state +
                ", type=" + type +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }

    public Message() {
    }

    public Message(int aId, int bId, int state, int type, Date date, String content) {
        this.aId = aId;
        this.bId = bId;
        this.state = state;
        this.type = type;
        this.date = date;
        this.content = content;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public int getbId() {
        return bId;
    }

    public void setbId(int bId) {
        this.bId = bId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
