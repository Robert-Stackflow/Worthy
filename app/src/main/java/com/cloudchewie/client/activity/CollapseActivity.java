package com.cloudchewie.client.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toolbar;

import com.cloudchewie.client.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class CollapseActivity extends AppCompatActivity {
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapse);
        mToolbar = findViewById(R.id.tool_bar);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
    }
}