package com.cloudchewie.client.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Attraction;

public class AttractionDetailActivity extends AppCompatActivity {
    Attraction attraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        attraction = (Attraction) intent.getSerializableExtra("attraction");
        setContentView(R.layout.activity_attraction_detail);
        findViewById(R.id.attraction_detail_back).setOnClickListener(v -> finish());
    }
}