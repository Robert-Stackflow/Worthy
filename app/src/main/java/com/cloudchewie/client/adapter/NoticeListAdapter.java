/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 22:07:04
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.user.HomePageActivity;
import com.cloudchewie.client.entity.Notice;
import com.cloudchewie.client.entity.Post;
import com.cloudchewie.client.util.basic.DateUtil;
import com.cloudchewie.client.util.widget.SmallPostItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.MyViewHolder> {
    private List<Notice> notices;
    private Context context;
    private Notice.NOTICE_TYPE type;

    public NoticeListAdapter(Context context, List<Notice> notices, Notice.NOTICE_TYPE type) {
        this.notices = notices;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public NoticeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_notice_item, parent, false);
        return new NoticeListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeListAdapter.MyViewHolder holder, int position) {
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
        holder.nameView.setText(notice.getUser().getUsername());
        holder.timeView.setText(DateUtil.beautifyTime(notice.getDate()));
        holder.contentView.setText(handleLineBreaks(notice.getDescribe()));
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
                holder.nameView.setText(notice.getDescribe());
                break;
        }
        holder.avatarView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", notice.getUser());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.nameView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", notice.getUser());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        Glide.with(context).load(notice.getUser().getAvatarUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.avatarView);
    }

    @Override
    public int getItemCount() {
        return notices == null ? 0 : notices.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public CircleImageView avatarView;
        public TextView timeView;
        public TextView contentView;
        public LinearLayout referenceLayout;
        public ConstraintLayout bottomLayout;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.notice_item_username);
            avatarView = view.findViewById(R.id.notice_item_avatar);
            timeView = view.findViewById(R.id.notice_item_time);
            contentView = view.findViewById(R.id.notice_item_content);
            referenceLayout = view.findViewById(R.id.notice_item_reference_layout);
            bottomLayout = view.findViewById(R.id.notice_item_bottom_layout);
        }
    }
}