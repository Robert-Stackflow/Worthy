package com.cloudchewie.client.util.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.baidu.mapapi.SDKInitializer;
import com.cloudchewie.ui.IToast;

public class BaiduSdkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, @NonNull Intent intent) {
        String action = intent.getAction();
        if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
            IToast.makeTextBottom(context, "地图SDK鉴权失败", Toast.LENGTH_SHORT);
        } else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
        }
    }
}