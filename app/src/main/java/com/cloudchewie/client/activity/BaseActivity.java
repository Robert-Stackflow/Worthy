package com.cloudchewie.client.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.util.StatusBarUtil;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setTextDark(this, getApplicationContext().getResources().getConfiguration().uiMode != 0x21);
    }
}
