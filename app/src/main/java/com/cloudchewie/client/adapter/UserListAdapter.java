/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 10:30:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.user.HomePageActivity;
import com.cloudchewie.client.domin.User;
import com.cloudchewie.ui.IconTextItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private List<User> users;
    private Context context;

    public UserListAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_user_item, parent, false);
        return new UserListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {
        if (null == users) {
            return;
        }
        if (position < 0 || position >= users.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final User user = users.get(position);
        if (null == user) {
            return;
        }
        holder.mItemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.nameView.setText(user.getUsername());
        holder.locationView.setText(user.getCurrentLocation().replace("市", ""));
        Glide.with(context).load(user.getAvatarUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.avatarView);
        holder.postView.setText(String.valueOf(((int) (Math.random() * 100))));
        holder.fansView.setText(String.valueOf(((int) (Math.random() * 10000))));
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public IconTextItem locationView;
        public IconTextItem postView;
        public IconTextItem fansView;
        public CircleImageView avatarView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.user_item_username);
            locationView = view.findViewById(R.id.user_item_location);
            postView = view.findViewById(R.id.user_item_post_count);
            fansView = view.findViewById(R.id.user_item_fans_count);
            avatarView = view.findViewById(R.id.user_item_avatar);
        }
    }
}