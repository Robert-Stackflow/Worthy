/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

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

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.AttractionDetailActivity;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.ui.IconTextItem;

import java.util.List;

public class AttractionsAdapter extends RecyclerView.Adapter<AttractionsAdapter.MyViewHolder> {
    private List<Attraction> attractions;
    private Context context;

    public AttractionsAdapter(Context context, List<Attraction> attractions) {
        this.attractions = attractions;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_attraction_card, parent, false);
        return new AttractionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == attractions) {
            return;
        }
        if (position < 0 || position >= attractions.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Attraction attraction = attractions.get(position);
        if (null == attraction) {
            return;
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, AttractionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("attraction", attraction);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.nameView.setText(attraction.getName());
        holder.locationView.setText(attraction.getLocation());
        holder.follow.setText(String.valueOf(attraction.getFollowerCount()));
        holder.visitor.setText(String.valueOf(attraction.getVisitorCount()));
        holder.post.setText(String.valueOf(attraction.getPostCount()));
    }

    @Override
    public int getItemCount() {
        return attractions == null ? 0 : attractions.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView locationView;
        public ImageView jumpToMap;
        public IconTextItem follow;
        public IconTextItem visitor;
        public IconTextItem post;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.attraction_card_name);
            locationView = view.findViewById(R.id.attraction_card_location);
            jumpToMap = view.findViewById(R.id.attraction_card_jump_to_map);
            follow = view.findViewById(R.id.attraction_card_follow);
            visitor = view.findViewById(R.id.attraction_card_visitor);
            post = view.findViewById(R.id.attraction_card_post);
        }
    }
}
