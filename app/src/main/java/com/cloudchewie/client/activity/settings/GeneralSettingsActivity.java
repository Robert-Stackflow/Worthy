/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:19:00
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.settings;

import android.os.Bundle;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class GeneralSettingsActivity extends BaseActivity {
    RefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_general_settings);
        ((TitleBar) findViewById(R.id.activity_general_settings_titlebar)).setLeftButtonClickListener(v -> finish());
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_general_settings_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }
}