/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "user")
public class User implements Serializable {
    private int user_id;
    private String username;
    private String password;
    @PrimaryKey
    @NonNull
    private String mobile = null;
    private String mail;
    private String avatar_path;
    private String signature;
    private int gender = 0;
    private Date birthday;
    private String token;
    private Date gmt_create;

    public User() {
    }

    @Ignore
    public User(@NonNull String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    @Ignore
    public User(int user_id, String username, String password, @NonNull String mobile, String mail, String avatar_path, String signature, int gender, Date birthday, String token, Date gmt_create) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.mail = mail;
        this.avatar_path = avatar_path;
        this.signature = signature;
        this.gender = gender;
        this.birthday = birthday;
        this.token = token;
        this.gmt_create = gmt_create;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getMobile() {
        return mobile;
    }

    public void setMobile(@NonNull String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mail='" + mail + '\'' +
                ", avatar_path='" + avatar_path + '\'' +
                ", signature='" + signature + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", token='" + token + '\'' +
                ", gmt_create=" + gmt_create +
                '}';
    }
}
