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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.discover.AttractionDetailActivity;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.util.image.CornerTransformation;
import com.cloudchewie.ui.FlowTagLayout;
import com.cloudchewie.ui.TagItem;

import java.util.List;

public class AttractionListAdapter extends RecyclerView.Adapter<AttractionListAdapter.MyViewHolder> {
    private Context context;

    private List<Attraction> attractions;

    public AttractionListAdapter(Context context, List<Attraction> attractions) {
        this.context = context;
        this.attractions = attractions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttractionListAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_attraction_item, parent, false));
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
        if (holder.flowTagLayout.getChildCount() > 0)
            holder.flowTagLayout.removeAllViews();
        for (String tag : attraction.getTags()) {
            TagItem tagItem = new TagItem(context);
            tagItem.setText(tag);
            tagItem.setTextSize(11);
            tagItem.setPadding(8, 4, 8, 4);
            tagItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tagItem.setTextColor(context.getColor(R.color.text_color_entry));
            tagItem.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_small_tag));
            tagItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.flowTagLayout.addView(tagItem);
        }
        holder.statisticsView.setText(attraction.getStatistics());
        Glide.with(context).load(attraction.getCoverImageUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(context, true, true, false, false))).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return attractions == null ? 0 : attractions.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView locationView;
        public ImageView imageView;
        public FlowTagLayout flowTagLayout;
        public TextView statisticsView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.attraction_card_name);
            locationView = view.findViewById(R.id.attraction_card_location);
            imageView = view.findViewById(R.id.attraction_card_image);
            flowTagLayout = view.findViewById(R.id.attraction_card_tags);
            statisticsView = view.findViewById(R.id.attraction_card_statistics);
        }
    }
}
