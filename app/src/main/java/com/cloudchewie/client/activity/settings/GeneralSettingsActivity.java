/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:19:00
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.settings;

import android.os.Bundle;
import android.widget.Toast;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.bean.ListBottomSheetBean;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.client.util.widget.ListBottomSheet;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.SettingItem;
import com.cloudchewie.ui.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class GeneralSettingsActivity extends BaseActivity {
    RefreshLayout swipeRefreshLayout;
    SettingItem languageEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_general_settings);
        ((TitleBar) findViewById(R.id.activity_general_settings_titlebar)).setLeftButtonClickListener(v -> finish());
        languageEntry = findViewById(R.id.entry_language);
        languageEntry.setOnClickListener(v -> {
            List<String> strings = new ArrayList<>();
            strings.add("跟随系统");
            strings.add("简体中文");
            strings.add("English");
            ListBottomSheet bottomSheet = new ListBottomSheet(GeneralSettingsActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
                IToast.makeTextBottom(GeneralSettingsActivity.this, "切换语言为" + strings.get(position), Toast.LENGTH_SHORT).show();
                languageEntry.setTipText(strings.get(position));
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        });
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