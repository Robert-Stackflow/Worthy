/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:26:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.http;

import androidx.annotation.NonNull;

import com.cloudchewie.client.util.system.LocalStorage;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    public TokenInterceptor() {
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originRequest = chain.request();
        if (!HttpRequestUtil.isAuthRequest(originRequest.url().toString())) {
            Request newRequest = originRequest.newBuilder()
                    .addHeader("token", LocalStorage.getToken())
                    .build();
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(originRequest);
        }
    }
}
