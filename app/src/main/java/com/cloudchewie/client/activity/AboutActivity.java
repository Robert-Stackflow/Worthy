package com.cloudchewie.client.activity;

import android.os.Bundle;
import android.view.View;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.TitleBar;
import com.cloudchewie.client.util.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_about);
        ((TitleBar) findViewById(R.id.about_titlebar)).setLeftButtonClickListener(v -> finish());
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.about_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

    }
}
