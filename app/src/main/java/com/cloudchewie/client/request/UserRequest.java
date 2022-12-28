/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.request;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.cloudchewie.client.domin.User;
import com.cloudchewie.client.util.http.HttpRequestUtil;
import com.cloudchewie.client.util.http.ResponseCode;

public class UserRequest {

    public void signUp(User user) {
        new Thread(() -> HttpRequestUtil.post(HttpRequestUtil.mediaType_JSON, "/user/signup", user)).start();
    }

    public String login(User user) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.post(HttpRequestUtil.mediaType_JSON, "/user/signin", user));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ToastUtils.showShort(response[0].getString("message"));
        if (response[0].getIntValue("code") == ResponseCode.RC102.getCode())
            return response[0].getString("data");
        return null;
    }

    public void logout() {
        new Thread(() -> HttpRequestUtil.post(HttpRequestUtil.mediaType_FORM, "/user/logout", "")).start();
    }

    public User find() {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.get(HttpRequestUtil.mediaType_FORM, "/user/find/"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JSONObject.toJavaObject(response[0].getJSONObject("data"), User.class);
    }

}
