package com.cloudchewie.client.activity;

import android.os.Bundle;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.TitleBar;
import com.cloudchewie.client.util.StatusBarUtil;

public class NotificationSettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_notification_settings);
        ((TitleBar) findViewById(R.id.notification_settings_titlebar)).setLeftButtonClickListener(v -> finish());
    }
}
