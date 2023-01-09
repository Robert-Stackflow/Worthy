package com.cloudchewie.client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.bean.ListBottomSheetBean;

import java.util.List;

public class ListBottomSheetAdapter extends RecyclerView.Adapter<ListBottomSheetAdapter.MyViewHolder> {
    List<ListBottomSheetBean> beans;
    OnItemClickedListener listener;
    Context context;

    public ListBottomSheetAdapter(Context context, List<ListBottomSheetBean> beans) {
        this.beans = beans;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListBottomSheetBeans(List<ListBottomSheetBean> beans) {
        this.beans = beans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new MyViewHolder(view);
    }

    public ListBottomSheetAdapter setOnItemClickedListener(OnItemClickedListener listener) {
        this.listener = listener;
        return this;
    }

    public List<ListBottomSheetBean> getCityListBeans() {
        return beans;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == beans) {
            return;
        }
        if (position < 0 || position >= beans.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final ListBottomSheetBean cityBean = beans.get(position);
        if (null == cityBean) {
            return;
        }
        if (position == 0) {
            holder.textView.setBackgroundResource(R.drawable.shape_round_top_dp10);
            holder.textView.setBackgroundTintList(context.getColorStateList(R.color.color_selector_content));
        }
        holder.textView.setText(cityBean.getText());
        holder.mItemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    public interface OnItemClickedListener {
        void onItemClicked(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView textView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            textView = view.findViewById(R.id.list_view_item_text);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        public String getText() {
            return textView.getText().toString();
        }
    }
}
