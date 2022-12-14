/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/17 22:05:40
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudchewie.ui.util.MatricsUtil;
import com.cloudchewie.ui.util.SizeUtil;

public class MyDialog extends Dialog {
    public OnClickBottomListener onClickBottomListener;
    private TextView titleTv;
    private TextView messageTv;
    private Button negtiveBn, positiveBn;
    private ImageView closeBn;
    private String message;
    private String title = "消息提示";
    private String positive, negtive;
    private int imageResId = -1;
    private ConstraintLayout mainLayout;
    private boolean isSingle = false;

    public MyDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_dialog);
        setCanceledOnTouchOutside(false);
        initView();
        refreshView();
        initEvent();
    }

    private void initEvent() {
        positiveBn.setOnClickListener(v -> {
            if (onClickBottomListener != null) {
                onClickBottomListener.onPositiveClick();
            }
        });
        negtiveBn.setOnClickListener(v -> {
            if (onClickBottomListener != null) {
                onClickBottomListener.onNegtiveClick();
            }
        });
        closeBn.setOnClickListener(v -> {
            if (onClickBottomListener != null) {
                onClickBottomListener.onCloseClick();
            }
        });
    }

    private void refreshView() {
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(message)) {
            messageTv.setText(message);
        }
        if (!TextUtils.isEmpty(positive)) {
            positiveBn.setText(positive);
        } else {
            positiveBn.setText("确认");
        }
        if (!TextUtils.isEmpty(negtive)) {
            negtiveBn.setText(negtive);
        } else {
            negtiveBn.setText("取消");
        }
        if (isSingle) {
            negtiveBn.setVisibility(View.GONE);
        } else {
            negtiveBn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    private void initView() {
        negtiveBn = findViewById(R.id.widget_dialog_negtive);
        positiveBn = findViewById(R.id.widget_dialog_positive);
        titleTv = findViewById(R.id.widget_dialog_title);
        messageTv = findViewById(R.id.widget_dialog_message);
        mainLayout = findViewById(R.id.widget_dialog_main_layout);
        closeBn = findViewById(R.id.widget_dialog_close);
        mainLayout.setMinWidth(SizeUtil.dp2px(getContext(), MatricsUtil.getScreenWidth(getContext()) - 10));
    }

    public MyDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MyDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MyDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public MyDialog setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegtive() {
        return negtive;
    }

    public MyDialog setNegtive(String negtive) {
        this.negtive = negtive;
        return this;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public MyDialog setSingle(boolean single) {
        isSingle = single;
        return this;
    }

    public MyDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this;
    }

    public interface OnClickBottomListener {
        void onPositiveClick();

        void onNegtiveClick();

        void onCloseClick();
    }

}