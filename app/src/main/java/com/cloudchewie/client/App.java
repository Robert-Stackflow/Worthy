/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 19:06:01
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client;

import android.app.Application;

import com.cloudchewie.client.util.image.MyImageLoader;
import com.previewlibrary.ZoomMediaLoader;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class App extends Application {
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ZoomMediaLoader.getInstance().init(new MyImageLoader());
    }
}