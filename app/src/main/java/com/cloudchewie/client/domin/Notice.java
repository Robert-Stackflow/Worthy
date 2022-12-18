/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 21:06:28
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import java.io.Serializable;
import java.util.Date;

public class Notice implements Serializable {
    int userId;
    String username;
    Date date;
    Object obj;
    Object obj2;
    NOTICE_TYPE noticeType;

    public Notice(int userId, String username, Date date, NOTICE_TYPE noticeType, Object obj, Object obj2) {
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.obj = obj;
        this.obj2 = obj2;
        this.noticeType = noticeType;
    }

    public Notice() {
    }

    public Object getObj2() {
        return obj2;
    }

    public void setObj2(Object obj2) {
        this.obj2 = obj2;
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

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public NOTICE_TYPE getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NOTICE_TYPE noticeType) {
        this.noticeType = noticeType;
    }

    public String getDescribe() {
        switch (noticeType) {
            case COMMENT:
                return "有人评论了你:\n" + ((obj2 == null || !(obj2 instanceof Comment)) ? "" : ((Comment) obj2).getContent());
            case REPLY:
                return "有人回复了你的评论:\n" + ((obj2 == null || !(obj2 instanceof Comment)) ? "" : ((Comment) obj2).getContent());
            case THUMBUP:
                return "有人点赞了你的帖子";
            case COLLECT:
                return "有人收藏了你的帖子";
            case FOLLOW:
                return username + "关注了你";
            default:
                return "有人与你进行了互动";
        }
    }

    public enum NOTICE_TYPE {
        COMMENT, REPLY, THUMBUP, COLLECT, FOLLOW
    }
}
