package com.cloudchewie.client.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class ChangePasswordActivity extends AppCompatActivity {
    RefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ((TitleBar) findViewById(R.id.change_password_titlebar)).setLeftButtonClickListener(v -> finish());
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.change_password_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }
}