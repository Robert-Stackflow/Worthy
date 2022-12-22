/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 12:08:19
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.user.FavoritesDetailActivity;
import com.cloudchewie.client.domin.Favorites;
import com.cloudchewie.ui.IconTextItem;

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.MyViewHolder> {
    private List<Favorites> favoritesList;
    private Context context;

    public FavoritesListAdapter(Context context, List<Favorites> favoritesList) {
        this.favoritesList = favoritesList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_favorites_item, parent, false);
        return new FavoritesListAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavoritesListAdapter.MyViewHolder holder, int position) {
        if (null == favoritesList) {
            return;
        }
        if (position < 0 || position >= favoritesList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Favorites favorites = favoritesList.get(position);
        if (null == favorites) {
            return;
        }
        holder.mItemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FavoritesDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("favorites", favorites);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.nameView.setText(favorites.getName());
        holder.authorView.setText("创建者:" + favorites.getUsername());
        holder.isPublicView.setText(favorites.isPublic() ? "公开" : "私密");
        holder.describeView.setText("简介:" + favorites.getDescribe());
        holder.itemCount.setText(String.valueOf(favorites.getItemCount()));
        Glide.with(context).load(favorites.getCoverUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.coverView);
    }

    @Override
    public int getItemCount() {
        return favoritesList == null ? 0 : favoritesList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView authorView;
        public TextView isPublicView;
        public TextView describeView;
        public IconTextItem itemCount;
        public ImageView coverView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.favorites_item_name);
            authorView = view.findViewById(R.id.favorites_item_author);
            describeView = view.findViewById(R.id.favorites_item_describe);
            itemCount = view.findViewById(R.id.favorites_item_count);
            isPublicView = view.findViewById(R.id.favorites_item_ispublic);
            coverView = view.findViewById(R.id.favorites_item_cover);
        }
    }
}
