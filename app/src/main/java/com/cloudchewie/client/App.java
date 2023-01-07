/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 19:06:01
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client;

import android.app.Application;
import android.content.IntentFilter;

import com.baidu.mapapi.SDKInitializer;
import com.cloudchewie.client.util.image.MyImageLoader;
import com.cloudchewie.client.util.map.BaiduSdkReceiver;
import com.previewlibrary.ZoomMediaLoader;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class App extends Application {
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setDrawableSize(10).setDrawableArrowSize(1).setDrawableProgressSize(10));
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(10).setDrawableProgressSize(10).setDrawableArrowSize(10));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ZoomMediaLoader.getInstance().init(new MyImageLoader());
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        BaiduSdkReceiver mReceiver = new BaiduSdkReceiver();
        registerReceiver(mReceiver, iFilter);
    }
}