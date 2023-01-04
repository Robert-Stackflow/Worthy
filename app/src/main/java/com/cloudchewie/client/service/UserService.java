/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.service;

import com.blankj.utilcode.util.ThreadUtils;
import com.cloudchewie.client.domin.User;
import com.cloudchewie.client.request.UserAuthRequest;
import com.cloudchewie.client.util.system.LocalStorage;

import java.util.Date;

public class UserService {

    public static void insert(User user) {
        LocalStorage.getAppDatabase().userDao().insert(user);
    }

    public static User getUserInfo() {
        return LocalStorage.getAppDatabase().userDao().findById(LocalStorage.getUser_id());
    }

    public static Date getGmtCreate() {
        return getUserInfo().getGmt_create();
    }

    public static void update(User user) {
        LocalStorage.getAppDatabase().userDao().update(user);
    }

    public static void refreshToken(String token) {
        User user = getUserInfo();
        user.setToken(token);
        LocalStorage.getAppDatabase().userDao().update(user);
    }

    public static void logout() {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() {
                new UserAuthRequest().logout();
                return null;
            }

            @Override
            public void onSuccess(Object result) {
            }
        });
        LocalStorage.getAppDatabase().userDao().delete(getUserInfo());
        LocalStorage.logout();
        LocalStorage.getAppDatabase().userDao().clear();
    }
}
