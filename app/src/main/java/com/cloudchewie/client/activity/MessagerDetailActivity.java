package com.cloudchewie.client.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cloudchewie.client.R;
import com.cloudchewie.client.util.StatusBarUtil;

public class MessagerDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_messager_detail);
    }
}