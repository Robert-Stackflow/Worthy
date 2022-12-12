package com.cloudchewie.client.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.TitleBar;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((TitleBar) findViewById(R.id.about_titlebar)).setLeftButtonClickListener(v -> finish());
    }

    @Override
    public void onClick(View view) {

    }
}
