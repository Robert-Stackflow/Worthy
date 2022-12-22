/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.message;

import android.os.Bundle;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.util.ui.StatusBarUtil;

public class MessagerDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_messager_detail);
    }
}