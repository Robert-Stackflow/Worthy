/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cloudchewie.client.util.image.ImageUrlUtil;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "user")
public class User implements Serializable {
    @PrimaryKey
    private int userId;
    private String username;
    private String password;
    private String mobile;
    private String avatarUrl;
    private String backgroundUrl;
    private String signature;
    private GENDER gender;
    private Date birthday;
    private String token;
    private Date gmt_create;
    private String currentLocation;

    public User() {
    }

    @Ignore
    public User(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    @Ignore
    public User(int userId, String username, String mobile, String signature, GENDER gender, Date birthday, String currentLocation) {
        this.userId = userId;
        this.username = username;
        this.mobile = mobile;
        this.avatarUrl = ImageUrlUtil.getUrls(1).get(0);
        this.backgroundUrl = ImageUrlUtil.getUrls(1).get(0);
        this.signature = signature;
        this.gender = gender;
        this.birthday = birthday;
        this.currentLocation = currentLocation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public GENDER getGender() {
        return gender;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getGenderInfo() {
        switch (gender) {
            case OTHER:
                return "其他";
            case MALE:
                return "男";
            case FEMALE:
                return "女";
            case UNDEFINED:
            default:
                return "未知";
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", backgroundUrl='" + backgroundUrl + '\'' +
                ", signature='" + signature + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", token='" + token + '\'' +
                ", gmt_create=" + gmt_create +
                ", currentLocation='" + currentLocation + '\'' +
                '}';
    }

    public enum GENDER {
        UNDEFINED, MALE, FEMALE, OTHER
    }
}
