/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import static com.cloudchewie.client.util.basic.DateUtil.beautifyTime;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.cloudchewie.client.entity.Chatter;
import com.cloudchewie.ui.ChatterItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatterListAdapter extends RecyclerView.Adapter<ChatterListAdapter.MyViewHolder> {
    private List<Chatter> chatters;
    private Context context;

    public ChatterListAdapter(Context context, List<Chatter> chatters) {
        this.chatters = chatters;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatterItem chatterItem = new ChatterItem(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_chatter_item, parent, false);
        return new ChatterListAdapter.MyViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == chatters) {
            return;
        }
        if (position < 0 || position >= chatters.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Chatter chatter = chatters.get(position);
        if (null == chatter) {
            return;
        }
        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, MessagerDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("messager", messager);
//            intent.putExtras(bundle);
//            context.startActivity(intent);
        });
        holder.nameView.setText(chatter.getUser().getUsername());
        String info = chatter.getTypeInfo();
        if (info != null)
            holder.tagView.setText(info);
        else
            holder.tagView.setVisibility(View.GONE);
        holder.timeView.setText(beautifyTime(chatter.getLastMessage().getDate()));
        holder.contentView.setText(chatter.getLastMessage().getContent());
        Glide.with(context).load(chatter.getUser().getAvatarUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.avatarView);
    }

    @Override
    public int getItemCount() {
        return chatters == null ? 0 : chatters.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public ChatterItem chatterItem;
        public TextView nameView;
        public TextView tagView;
        public TextView timeView;
        public TextView contentView;
        public ImageView statusView;
        public CircleImageView avatarView;
        public View splitter;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            mItemView.setClickable(true);
            mItemView.setFocusable(true);
            nameView = view.findViewById(R.id.chatter_item_name);
            tagView = view.findViewById(R.id.chatter_item_tag);
            timeView = view.findViewById(R.id.chatter_item_time);
            contentView = view.findViewById(R.id.chatter_item_content);
            statusView = view.findViewById(R.id.chatter_item_status);
            splitter = view.findViewById(R.id.chatter_item_splitter);
            avatarView = view.findViewById(R.id.chatter_item_avatar);
        }
    }
}