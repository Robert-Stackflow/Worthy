/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.entity;

import androidx.annotation.NonNull;

import java.util.Date;

public class Message {
    int senderId;
    int receiverId;
    MESSAGE_STATE state;//0未读,1已读,-1删除
    MESSAGE_TYPE type;//0文字,1图片,2位置,3视频,4语音
    Date date;
    String content;

    public Message() {
    }

    public Message(int senderId, int receiverId, MESSAGE_STATE state, MESSAGE_TYPE type, Date date, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.state = state;
        this.type = type;
        this.date = date;
        this.content = content;
    }

    public MESSAGE_STATE getState() {
        return state;
    }

    public void setState(MESSAGE_STATE state) {
        this.state = state;
    }

    public MESSAGE_TYPE getType() {
        return type;
    }

    public void setType(MESSAGE_TYPE type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "aId=" + senderId +
                ", bId=" + receiverId +
                ", state=" + state +
                ", type=" + type +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
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

    public enum MESSAGE_STATE {
        UNREAD,
        READ,
        DELETED
    }

    public enum MESSAGE_TYPE {
        TEXT,
        IMAGE,
        LOCATION,
        VIDEO,
        VOICE
    }
}
