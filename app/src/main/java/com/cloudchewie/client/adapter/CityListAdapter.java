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
import com.cloudchewie.client.bean.CityBean;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.MyViewHolder> {
    List<CityBean> cityBeans;
    onItemClickedListener listener;
    Context context;

    public CityListAdapter(Context context, List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCityBeans(List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new MyViewHolder(view);
    }

    public CityListAdapter setListener(onItemClickedListener listener) {
        this.listener = listener;
        return this;
    }

    public List<CityBean> getCityListBeans() {
        return cityBeans;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == cityBeans) {
            return;
        }
        if (position < 0 || position >= cityBeans.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final CityBean cityBean = cityBeans.get(position);
        if (null == cityBean) {
            return;
        }
        holder.textView.setText(cityBean.getName());
        holder.mItemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return cityBeans == null ? 0 : cityBeans.size();
    }

    public interface onItemClickedListener {
        void onItemClicked(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView textView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            textView = view.findViewById(R.id.list_view_item_text);
        }

        public String getText() {
            return textView.getText().toString();
        }
    }
}
