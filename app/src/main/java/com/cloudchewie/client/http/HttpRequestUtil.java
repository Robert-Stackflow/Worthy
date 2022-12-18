/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequestUtil {
    public static final MediaType mediaType_JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType mediaType_FORM = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
    private static final String baseUrl = "https://116.62.239.181:443";
    private final OkHttpClient okHttpClient = null;

    public static JSONObject post(MediaType contentType, String url, Object body) {
        if (body instanceof JSONObject)
            body = ((JSONObject) body).toJSONString();
        if (!(body instanceof String))
            body = com.alibaba.fastjson.JSON.toJSONString(body);
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        RequestBody requestBody = RequestBody.create((String) body, contentType);
        Request request = new Request.Builder().url(baseUrl + url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        try {
            responseString[0] = (Objects.requireNonNull(call.execute().body())).string();
        } catch (IOException e) {
            Log.d("xuruida", e.toString());
        }
        return JSON.parseObject(responseString[0]);
    }

    public static JSONObject get(MediaType contentType, String url) {
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        Request request = new Request.Builder().url(baseUrl + url).get().build();
        Call call = okHttpClient.newCall(request);
        try {
            responseString[0] = (Objects.requireNonNull(call.execute().body())).string();
        } catch (IOException e) {
            Log.d("xuruida", e.toString());
        }
        return JSON.parseObject(responseString[0]);
    }

    public static JSONObject delete(MediaType contentType, String url) {
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        Request request = new Request.Builder().url(baseUrl + url).delete().build();
        Call call = okHttpClient.newCall(request);
        try {
            responseString[0] = (Objects.requireNonNull(call.execute().body())).string();
        } catch (IOException e) {
            Log.d("xuruida", e.toString());
        }
        return JSON.parseObject(responseString[0]);
    }

    public static boolean isAuth(@NonNull String url) {
        return url.contains("/user/signup") || url.contains("/user/signin");
    }
}
