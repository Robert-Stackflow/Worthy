/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.discover.PostDetailActivity;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.util.basic.DateUtil;

import java.util.List;

public class SmallPostListAdapter extends RecyclerView.Adapter<SmallPostListAdapter.MyViewHolder> {
    private List<Post> posts;
    private Context context;

    public SmallPostListAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_small_post_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == posts) {
            return;
        }
        if (position < 0 || position >= posts.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Post post = posts.get(position);
        if (null == post) {
            return;
        }
        holder.mItemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("post", post);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.nameView.setText(post.getUser().getUsername());
        holder.timeView.setText(DateUtil.beautifyTime(post.getDate()));
        holder.contentView.setText(handleLineBreaks(post.getContent()));
        holder.mainLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_content));
        if (holder.imageView != null)
            Glide.with(context).load(post.getImageUrls().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView timeView;
        public TextView contentView;
        public ConstraintLayout mainLayout;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            mainLayout = view.findViewById(R.id.small_post_item_layout);
            nameView = view.findViewById(R.id.small_post_item_username);
            timeView = view.findViewById(R.id.small_post_item_time);
            contentView = view.findViewById(R.id.small_post_item_content);
            imageView = view.findViewById(R.id.small_post_item_image);
        }
    }
}
