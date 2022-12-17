package com.cloudchewie.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog {
    public OnClickBottomListener onClickBottomListener;
    private TextView titleTv;
    private TextView messageTv;
    private Button negtiveBn, positiveBn;
    private View columnLineView;
    private String message;
    private String title;
    private String positive, negtive;
    private int imageResId = -1;
    private boolean isSingle = false;

    public CustomDialog(Context context) {
        super(context, R.style.CustomDialog);
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
            columnLineView.setVisibility(View.GONE);
            negtiveBn.setVisibility(View.GONE);
        } else {
            negtiveBn.setVisibility(View.VISIBLE);
            columnLineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    private void initView() {
        negtiveBn = findViewById(R.id.negtive);
        positiveBn = findViewById(R.id.positive);
        titleTv = findViewById(R.id.title);
        messageTv = findViewById(R.id.message);
        columnLineView = findViewById(R.id.column_line);
    }

    public CustomDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CustomDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CustomDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public CustomDialog setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegtive() {
        return negtive;
    }

    public CustomDialog setNegtive(String negtive) {
        this.negtive = negtive;
        return this;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public CustomDialog setSingle(boolean single) {
        isSingle = single;
        return this;
    }

    public CustomDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this;
    }

    public interface OnClickBottomListener {
        void onPositiveClick();

        void onNegtiveClick();
    }

}