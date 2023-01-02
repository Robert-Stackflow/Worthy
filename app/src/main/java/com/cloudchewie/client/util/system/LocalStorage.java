/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.system;

import androidx.annotation.NonNull;

import com.cloudchewie.client.util.database.AppDatabase;
import com.cloudchewie.client.util.development.JwtUtil;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Objects;

public class LocalStorage {
    private static int user_id;
    private static boolean isLogin;
    private static String token;
    private static AppDatabase appDatabase;

    @Contract(pure = true)
    public static int getUser_id() {
        return user_id;
    }

    public static void init(@NonNull AppDatabase appDatabase) {
        LocalStorage.appDatabase = appDatabase;
        List<String> tokens = appDatabase.userDao().getToken();
        isLogin = !tokens.isEmpty() && !(tokens.get(0) == null);
        if (isLogin) {
            token = tokens.get(0);
            user_id = appDatabase.userDao().getAll().get(0).getUserId();
        }
    }

    public static void logout() {
        isLogin = false;
    }

    @Contract(pure = true)
    public static boolean getIsLogin() {
        return isLogin;
    }

    @Contract(pure = true)
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        isLogin = true;
        LocalStorage.token = token;
        user_id = Integer.parseInt(Objects.requireNonNull(JwtUtil.getPayload(token).get("sub")).asString());
    }

    @Contract(pure = true)
    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static void setAppDatabase(AppDatabase appDatabase) {
        LocalStorage.appDatabase = appDatabase;
    }
}
