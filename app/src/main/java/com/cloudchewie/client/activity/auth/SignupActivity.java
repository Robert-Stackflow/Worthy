package com.cloudchewie.client.activity.auth;

import android.annotation.SuppressLint;
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
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class SignupActivity extends BaseActivity implements View.OnClickListener {
    TextView termView;
    RefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_signup);
        termView = findViewById(R.id.activity_signup_term);
        ((TitleBar) findViewById(R.id.activity_signup_titlebar)).setLeftButtonClickListener(v -> finish());
        findViewById(R.id.activity_signup_confirm).setOnClickListener(this);
        setTermView();
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_signup_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    void setTermView() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("点击注册即表示您已阅读并同意");
        SpannableString userTermString = new SpannableString("《用户协议》和");
        userTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                IToast.makeTextBottom(SignupActivity.this, "阅读用户协议", Toast.LENGTH_SHORT).show();
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
                IToast.makeTextBottom(SignupActivity.this, "阅读隐私政策", Toast.LENGTH_SHORT).show();
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
    }
}
