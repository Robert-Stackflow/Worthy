package com.cloudchewie.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.client.R;
import com.cloudchewie.ui.TitleBar;
import com.cloudchewie.client.util.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_settings);
        ((TitleBar) findViewById(R.id.settings_titlebar)).setLeftButtonClickListener(v -> finish());
        findViewById(R.id.entry_account).setOnClickListener(this);
        findViewById(R.id.entry_notification).setOnClickListener(this);
        findViewById(R.id.entry_privacy).setOnClickListener(this);
        findViewById(R.id.entry_general).setOnClickListener(this);
        findViewById(R.id.entry_help).setOnClickListener(this);
        findViewById(R.id.entry_feedback).setOnClickListener(this);
        findViewById(R.id.entry_about).setOnClickListener(this);
        findViewById(R.id.entry_close_app).setOnClickListener(this);
        findViewById(R.id.entry_logout).setOnClickListener(this);
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.settings_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.entry_notification)) {
            Intent intent = new Intent(getApplicationContext(), NotificationSettingsActivity.class);
            startActivity(intent);
        } else if (view == findViewById(R.id.entry_account)) {
            Intent intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);
            startActivity(intent);
        } else if (view == findViewById(R.id.entry_general)) {
            Intent intent = new Intent(getApplicationContext(), GeneralSettingsActivity.class);
            startActivity(intent);
        } else if (view == findViewById(R.id.entry_privacy)) {
            Intent intent = new Intent(getApplicationContext(), PrivacySettingsActivity.class);
            startActivity(intent);
        } else if (view == findViewById(R.id.entry_close_app)) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else if (view == findViewById(R.id.entry_logout)) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else if (view == findViewById(R.id.entry_about)) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        }
    }
}