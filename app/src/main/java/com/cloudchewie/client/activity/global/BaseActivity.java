/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:25:09
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.global;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.util.ui.StatusBarUtil;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setTextDark(this, getApplicationContext().getResources().getConfiguration().uiMode != 0x21);
    }
}
