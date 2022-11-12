package com.cloudchewie.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.R;
import com.cloudchewie.client.util.StatusBarUtil;

public class SettingsActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.entry_account).setOnClickListener(this);
        findViewById(R.id.entry_notification).setOnClickListener(this);
        findViewById(R.id.entry_privacy).setOnClickListener(this);
        findViewById(R.id.entry_general).setOnClickListener(this);
        findViewById(R.id.entry_help).setOnClickListener(this);
        findViewById(R.id.entry_feedback).setOnClickListener(this);
        findViewById(R.id.entry_privacy_policy).setOnClickListener(this);
        findViewById(R.id.entry_service).setOnClickListener(this);
        findViewById(R.id.entry_about).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==findViewById(R.id.entry_notification)){
            Intent intent = new Intent(getApplicationContext(),NotificationSettingsActivity.class);
            startActivity(intent);
        }else if(view==findViewById(R.id.entry_account)){
            Intent intent = new Intent(getApplicationContext(),AccountSettingsActivity.class);
            startActivity(intent);
        }else if(view==findViewById(R.id.entry_general)){
            Intent intent = new Intent(getApplicationContext(),GeneralSettingsActivity.class);
            startActivity(intent);
        }
    }
}