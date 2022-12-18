/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 22:07:04
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Notice;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.util.TimeUtil;
import com.cloudchewie.client.widget.SmallPostItem;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {
    private List<Notice> notices;
    private Context context;
    private Notice.NOTICE_TYPE type;

    public NoticeAdapter(Context context, List<Notice> notices, Notice.NOTICE_TYPE type) {
        this.notices = notices;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public NoticeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_notice_item, parent, false);
        return new NoticeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.MyViewHolder holder, int position) {
        if (null == notices) {
            return;
        }
        if (position < 0 || position >= notices.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Notice notice = notices.get(position);
        if (null == notice) {
            return;
        }
        holder.nameView.setText(notice.getUsername());
        holder.timeView.setText(TimeUtil.dateToString(notice.getDate()));
        holder.contentView.setText(notice.getDescribe());
        switch (type) {
            case COMMENT:
            case REPLY:
            case COLLECT:
            case THUMBUP:
                if (notice.getObj() != null && notice.getObj() instanceof Post)
                    holder.referenceLayout.addView(new SmallPostItem(context, (Post) notice.getObj()));
                break;
            default:
                holder.referenceLayout.setVisibility(View.GONE);
                holder.contentView.setVisibility(View.GONE);
//                holder.bottomLayout.setVisibility(View.GONE);
                holder.nameView.setText(notice.getDescribe());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notices == null ? 0 : notices.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView timeView;
        public TextView contentView;
        public LinearLayout referenceLayout;
        public ConstraintLayout bottomLayout;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.notice_item_username);
            timeView = view.findViewById(R.id.notice_item_time);
            contentView = view.findViewById(R.id.notice_item_content);
            referenceLayout = view.findViewById(R.id.notice_item_reference_layout);
            bottomLayout = view.findViewById(R.id.notice_item_bottom_layout);
        }
    }
}