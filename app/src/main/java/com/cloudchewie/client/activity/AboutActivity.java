package com.cloudchewie.client.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.TitleBar;
import com.cloudchewie.client.util.StatusBarUtil;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_about);
        findViewById(R.id.entry_privacy_policy).setOnClickListener(this);
        findViewById(R.id.entry_service_terms).setOnClickListener(this);
        ((TitleBar) findViewById(R.id.about_titlebar)).setLeftButtonClickListener(v -> finish());
    }

    @Override
    public void onClick(View view) {

    }
}
