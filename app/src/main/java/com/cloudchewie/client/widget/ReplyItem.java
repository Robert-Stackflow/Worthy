/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 20:29:31
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.widget;

import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudchewie.client.activity.discover.CommentDetailActivity;
import com.cloudchewie.client.domin.Comment;

public class ReplyItem extends ConstraintLayout {
    Comment mComment;
    Context context;
    private ConstraintLayout mainLayout;
    private TextView username;
    private TextView content;

    public ReplyItem(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public ReplyItem(@NonNull Context context, Comment comment) {
        super(context);
        initView(context, null);
        setComment(comment);
    }

    public ReplyItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ReplyItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public ReplyItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(com.cloudchewie.ui.R.layout.widget_reply_item, this, true);
        mainLayout = findViewById(com.cloudchewie.ui.R.id.reply_item_layout);
        username = findViewById(com.cloudchewie.ui.R.id.reply_item_username);
        content = findViewById(com.cloudchewie.ui.R.id.reply_item_content);
    }

    public void setComment(Comment comment) {
        this.mComment = comment;
        username.setText(comment.getUsername());
        content.setText(handleLineBreaks(comment.getContent()));
        mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("comment", mComment);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }
}
