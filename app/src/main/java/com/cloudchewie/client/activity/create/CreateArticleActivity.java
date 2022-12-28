package com.cloudchewie.client.activity.create;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.util.ui.KeyBoardUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.CustomDialog;
import com.cloudchewie.ui.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import jp.wasabeef.richeditor.RichEditor;

public class CreateArticleActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    TitleBar titleBar;
    EditText title;
    EditText content;
    RichEditor richEditor;
    int maxTitleSize = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_create_article);
        titleBar = findViewById(R.id.activity_create_article_titlebar);
        title = findViewById(R.id.activity_create_article_title);
        content = findViewById(R.id.activity_create_article_content);
        richEditor = findViewById(R.id.activity_create_article_editor);
        initView();
        initEditor();
        initSwipeRefresh();
    }

    void initEditor() {
        richEditor.setEditorHeight(400);
        richEditor.setEditorFontSize(15);
        richEditor.setEditorFontColor(R.color.color_accent);
        richEditor.setPlaceholder("分享你的打卡经历");
        richEditor.setInputEnabled(true);
    }

    void initView() {
        titleBar.setRightButtonClickListener(v -> finish());
        title.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxTitleSize)});
        titleBar.setLeftButtonClickListener(v -> {
            CustomDialog dialog = new CustomDialog(CreateArticleActivity.this);
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
    }

    @Override
    public void onBackPressed() {
        CustomDialog dialog = new CustomDialog(CreateArticleActivity.this);
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
        swipeRefreshLayout = findViewById(R.id.activity_create_article_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_create_article_operation_bold:
                againEdit();
                richEditor.setBold();
                break;
            case R.id.activity_create_article_operation_italic:
                againEdit();
                richEditor.setItalic();
                break;
            case R.id.activity_create_article_operation_underline:
                againEdit();
                richEditor.setUnderline();
                break;
            case R.id.activity_create_article_operation_list:
                againEdit();
                richEditor.setBullets();
                richEditor.setNumbers();
                break;
            case R.id.activity_create_article_operation_leftalignment:
                againEdit();
                richEditor.setAlignLeft();
                break;
            case R.id.activity_create_article_operation_centeralignment:
                againEdit();
                richEditor.setAlignCenter();
                break;
            case R.id.activity_create_article_operation_rightalignment:
                againEdit();
                richEditor.setAlignRight();
                break;
            case R.id.activity_create_article_operation_picture:
                break;
            case R.id.activity_create_article_operation_emoji:
                break;
            case R.id.activity_create_article_operation_undo:
                richEditor.undo();
                break;
            case R.id.activity_create_article_operation_redo:
                richEditor.redo();
                break;
        }
    }

    private void againEdit() {
        richEditor.focusEditor();
        KeyBoardUtil.openKeybord(title, CreateArticleActivity.this);
    }
}
