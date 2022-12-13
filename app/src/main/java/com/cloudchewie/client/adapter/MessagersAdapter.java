package com.cloudchewie.client.adapter;

import static com.cloudchewie.client.util.TimeUtil.dateToString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Messager;
import com.cloudchewie.client.ui.ChatterItem;

import java.util.List;

public class MessagersAdapter extends RecyclerView.Adapter<MessagersAdapter.MyViewHolder> {
    private List<Messager> messagers;
    private Context context;

    public MessagersAdapter(Context context, List<Messager> messagers) {
        this.messagers = messagers;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatterItem chatterItem = new ChatterItem(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_chatter_item, parent, false);
        return new MessagersAdapter.MyViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == messagers) {
            return;
        }
        if (position < 0 || position >= messagers.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Messager messager = messagers.get(position);
        if (null == messager) {
            return;
        }
        if (position == 0 && position == getItemCount() - 1)
            holder.mItemView.setBackground(holder.mItemView.getResources().getDrawable(R.drawable.shape_card_round));
        if (position == 0)
            holder.mItemView.setBackground(holder.mItemView.getResources().getDrawable(R.drawable.shape_card_top_radius));
        else if (position == getItemCount() - 1)
            holder.mItemView.setBackground(holder.mItemView.getResources().getDrawable(R.drawable.shape_card_bottom_radius));
        else
            holder.mItemView.setBackground(holder.mItemView.getResources().getDrawable(R.drawable.shape_card));
        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, MessagerDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("messager", messager);
//            intent.putExtras(bundle);
//            context.startActivity(intent);
        });
        holder.nameView.setText(messager.getName());
        if (messager.isStranger())
            holder.tagView.setText("陌生人");
        else
            holder.tagView.setVisibility(View.GONE);
        holder.timeView.setText(dateToString(messager.getLastMessage().getDate()));
        holder.contentView.setText(messager.getLastMessage().getContent());
    }

    @Override
    public int getItemCount() {
        return messagers == null ? 0 : messagers.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public ChatterItem chatterItem;
        public TextView nameView;
        public TextView tagView;
        public TextView timeView;
        public TextView contentView;
        public ImageView statusView;

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
        }
    }
}