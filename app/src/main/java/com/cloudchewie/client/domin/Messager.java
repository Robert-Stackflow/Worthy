/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Messager implements Serializable {
    String name;
    int state;//0默认,1勿扰,2黑名单
    boolean isStranger;
    List<Message> messages;

    public Messager() {
    }

    public Messager(String name, int state, boolean isStranger, List<Message> messages) {
        this.name = name;
        this.state = state;
        this.isStranger = isStranger;
        this.messages = messages;
    }

    @NonNull
    @Override
    public String toString() {
        return "Messager{" +
                "name='" + name + '\'' +
                ", state=" + state +
                ", isStranger=" + isStranger +
                ", messages=" + messages +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getLastMessage() {
        int index = 0;
        long interval = Long.MAX_VALUE;
        Date date = new Date(System.currentTimeMillis());
        for (Message message : messages) {
            long temp = date.getTime() - message.getDate().getTime();
            if (temp < interval) {
                interval = Math.min(interval, temp);
                index = messages.indexOf(message);
            }
        }
        return messages.get(index);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isStranger() {
        return isStranger;
    }

    public void setStranger(boolean stranger) {
        isStranger = stranger;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
