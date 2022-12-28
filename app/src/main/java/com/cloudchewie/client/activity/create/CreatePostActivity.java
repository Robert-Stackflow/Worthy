package com.cloudchewie.client.activity.create;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.CustomDialog;
import com.cloudchewie.ui.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class CreatePostActivity extends BaseActivity {
    RefreshLayout swipeRefreshLayout;
    TitleBar titleBar;
    EditText content;
    TextView wordCount;
    int maxSize = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_create_post);
        titleBar = findViewById(R.id.activity_create_post_titlebar);
        content = findViewById(R.id.activity_create_post_content);
        wordCount = findViewById(R.id.activity_create_post_count);
        initView();
        initSwipeRefresh();
    }

    @SuppressLint("SetTextI18n")
    void initView() {
        wordCount.setText("0/" + maxSize);
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSize)});
        titleBar.setRightButtonClickListener(v -> finish());
        titleBar.setLeftButtonClickListener(v -> {
            CustomDialog dialog = new CustomDialog(CreatePostActivity.this);
            dialog.setMessage("是否将本次编辑保存为草稿？保存后下次可以继续编写");
            dialog.setNegtive("放弃并退出");
            dialog.setPositive("保存为草稿");
            dialog.setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    finish();
                }
            });
            dialog.show();
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordCount.setText(charSequence.length() + "/" + maxSize);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        CustomDialog dialog = new CustomDialog(CreatePostActivity.this);
        dialog.setMessage("是否将本次编辑保存为草稿？保存后下次可以继续编写");
        dialog.setNegtive("放弃并退出");
        dialog.setPositive("保存为草稿");
        dialog.setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                finish();
            }

            @Override
            public void onNegtiveClick() {
                finish();
            }
        });
        dialog.show();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_create_post_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }
}
