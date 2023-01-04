/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:29:32
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.TitleBar;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    TextView termView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_login);
        termView = findViewById(R.id.login_term);
        ((TitleBar) findViewById(R.id.login_titlebar)).setLeftButtonClickListener(v -> finish());
        findViewById(R.id.login_by_mobile).setOnClickListener(this);
        findViewById(R.id.login_by_wechat).setOnClickListener(this);
        findViewById(R.id.login_signup).setOnClickListener(this);
        findViewById(R.id.login_problem).setOnClickListener(this);
        setTermView();
    }

    void setTermView() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("点击登录即表示您已阅读并同意");
        SpannableString userTermString = new SpannableString("《用户协议》和");
        userTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                IToast.makeTextBottom(LoginActivity.this, "阅读用户协议", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        userTermString.setSpan(new ForegroundColorSpan(getColor(R.color.color_prominent)), 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(userTermString);
        SpannableString privacyTermString = new SpannableString("《隐私政策》");
        privacyTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                IToast.makeTextBottom(LoginActivity.this, "阅读隐私政策", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 0, privacyTermString.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        privacyTermString.setSpan(new ForegroundColorSpan(getColor(R.color.color_prominent)), 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(privacyTermString);
        termView.setMovementMethod(LinkMovementMethod.getInstance());
        termView.setText(spannableStringBuilder);
        termView.setHighlightColor(Color.TRANSPARENT);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_by_mobile:
                Intent loginByMobileIntent = new Intent(this, LoginByMobileActivity.class);
                startActivity(loginByMobileIntent);
                break;
            case R.id.login_by_wechat:
                IToast.makeTextBottom(this, "系统维护中，暂时无法使用微信登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_signup:
                Intent signupIntent = new Intent(this, SignupActivity.class);
                startActivity(signupIntent);
                break;
            case R.id.login_problem:
                IToast.makeTextBottom(this, "功能维护中,暂时无法解决登录问题", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}