package com.cloudchewie.client.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.R;
import com.cloudchewie.client.util.StatusBarUtil;

public class GeneralSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_general_settings);
    }
}