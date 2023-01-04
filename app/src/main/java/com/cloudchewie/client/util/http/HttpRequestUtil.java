/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.http;

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
    public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_FORM = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
    private static final String serverUrl = "https://116.62.239.181:443";

    /**
     * 向默认服务器地址发送POST请求
     *
     * @param contentType 请求文件类型
     * @param url         请求路径
     * @param body        请求体
     * @return 响应结果
     */
    public static JSONObject postToServer(MediaType contentType, String url, Object body) {
        if (body instanceof JSONObject)
            body = ((JSONObject) body).toJSONString();
        if (!(body instanceof String))
            body = com.alibaba.fastjson.JSON.toJSONString(body);
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        Request request = new Request.Builder().url(serverUrl + url).post(RequestBody.create((String) body, contentType)).build();
        Call call = okHttpClient.newCall(request);
        try {
            responseString[0] = (Objects.requireNonNull(call.execute().body())).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(responseString[0]);
    }

    /**
     * 向默认服务器地址发送DELETE请求
     *
     * @param contentType 请求文件类型
     * @param url         请求路径
     * @return 响应结果
     */
    public static JSONObject deleteFromServer(MediaType contentType, String url) {
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        Request request = new Request.Builder().url(serverUrl + url).delete().build();
        Call call = okHttpClient.newCall(request);
        try {
            responseString[0] = (Objects.requireNonNull(call.execute().body())).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(responseString[0]);
    }

    /**
     * 向默认服务器地址发送请求
     *
     * @param contentType 请求文件类型
     * @param url         请求路径
     * @return 响应结果
     */
    public static JSONObject sendToServer(MediaType contentType, String url) {
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        Request request = new Request.Builder().url(serverUrl + url).build();
        Call call = okHttpClient.newCall(request);
        try {
            responseString[0] = (Objects.requireNonNull(call.execute().body())).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(responseString[0]);
    }

    /**
     * 向默认服务器地址发送GET请求
     *
     * @param contentType 请求文件类型
     * @param url         请求路径
     * @return 响应结果
     */
    public static JSONObject getFromServer(MediaType contentType, String url) {
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        Request request = new Request.Builder().url(serverUrl + url).get().build();
        Call call = okHttpClient.newCall(request);
        try {
            responseString[0] = (Objects.requireNonNull(call.execute().body())).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(responseString[0]);
    }

    /**
     * 使用完整路径发送GET请求
     *
     * @param contentType 请求文件类型
     * @param url         请求路径
     * @return 响应结果
     */
    public static JSONObject get(MediaType contentType, String url) {
        final String[] responseString = {null};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).get().build();
        try {
            responseString[0] = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(responseString[0]);
    }

    /**
     * 判断请求路径是否为认证类型请求
     *
     * @param url
     * @return
     */
    public static boolean isAuthRequest(@NonNull String url) {
        return url.contains("/user/signup") || url.contains("/user/signin");
    }
}
