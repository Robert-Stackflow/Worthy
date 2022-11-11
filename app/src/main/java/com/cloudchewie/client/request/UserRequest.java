package com.cloudchewie.client.request;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.cloudchewie.client.domin.User;
import com.cloudchewie.client.http.HttpRequestUtil;
import com.cloudchewie.client.http.ResponseCode;

public class UserRequest {

    public void signUp(User user) {
        HttpRequestUtil.post(HttpRequestUtil.mediaType_JSON, "/user/signup", user);
    }

    public String login(User user) {
        JSONObject response = HttpRequestUtil.post(HttpRequestUtil.mediaType_JSON, "/user/signin", user);
        ToastUtils.showShort(response.getString("message"));
        if (response.getIntValue("code") == ResponseCode.RC102.getCode())
            return response.getString("data");
        return null;
    }

    public void logout() {
        HttpRequestUtil.post(HttpRequestUtil.mediaType_FORM, "/user/logout", "");
    }

    public User find() {
        JSONObject response = HttpRequestUtil.get(HttpRequestUtil.mediaType_FORM, "/user/find/");
        return JSONObject.toJavaObject(response.getJSONObject("data"), User.class);
    }

}
