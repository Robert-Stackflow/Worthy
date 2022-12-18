/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import static com.cloudchewie.client.util.StringUtil.dealNewLine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.CommentDetailActivity;
import com.cloudchewie.client.domin.Comment;
import com.cloudchewie.client.util.TimeUtil;
import com.cloudchewie.client.widget.ReplyItem;
import com.cloudchewie.ui.IconTextItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private final List<Comment> comments;
    private final Context context;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_comment_item, parent, false);
        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        if (null == comments) {
            return;
        }
        if (position < 0 || position >= comments.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Comment comment = comments.get(position);
        if (null == comment) {
            return;
        }
        holder.mItemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("comment", comment);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.nameView.setText(comment.getUsername());
        holder.timeView.setText(TimeUtil.dateToString(comment.getDate()));
        holder.contentView.setText(dealNewLine(comment.getContent()));
        holder.thumbup.setText(String.valueOf(comment.getThumbupCount()));
        holder.reply.setText(String.valueOf(comment.getCommentCount()));
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            for (int i = 0; i < 3; i++) {
                Comment c = new Comment(1, "东方不败", simpleDateFormat.parse("2022-12-13 20:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
                ReplyItem item = new ReplyItem(context, c);
                holder.repliesLayout.addView(item);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView timeView;
        public TextView contentView;
        public IconTextItem reply;
        public IconTextItem thumbup;
        public LinearLayout repliesLayout;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.comment_item_username);
            timeView = view.findViewById(R.id.comment_item_time);
            contentView = view.findViewById(R.id.comment_item_content);
            reply = view.findViewById(R.id.comment_item_reply);
            thumbup = view.findViewById(R.id.comment_item_thumbup);
            repliesLayout = view.findViewById(R.id.comment_item_replies_layout);
        }
    }
}
