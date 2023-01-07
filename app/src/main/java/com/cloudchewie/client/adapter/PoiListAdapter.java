package com.cloudchewie.client.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.cloudchewie.client.R;

import java.util.List;

public class PoiListAdapter extends RecyclerView.Adapter<PoiListAdapter.MyViewHolder> {
    private List<PoiInfo> mPoiInfos = null;

    private OnSugItemClickListener mOnItemClickListener;

    public PoiListAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public PoiListAdapter(List<PoiInfo> poiInfoList) {
        mPoiInfos = poiInfoList;
        notifyDataSetChanged();
    }

    public PoiInfo getItemPoiInfo(int pos) {
        if (pos < 0 || null == mPoiInfos || pos >= mPoiInfos.size()) {
            return null;
        }
        return mPoiInfos.get(pos);
    }

    public void setOnItemClickListener(OnSugItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<PoiInfo> poiInfos) {
        if (null == poiInfos) return;
        mPoiInfos = poiInfos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_poi_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == mPoiInfos) {
            return;
        }
        if (position < 0 || position >= mPoiInfos.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        if (position < 0 || position >= mPoiInfos.size()) {
            return;
        }
        final PoiInfo poiInfo = mPoiInfos.get(position);
        if (null == poiInfo) {
            return;
        }
        holder.mItemView.setOnClickListener(v -> {
            if (null == mOnItemClickListener) {
                return;
            }
            int pos = holder.getAdapterPosition();
            long id = holder.getItemId();
            mOnItemClickListener.onItemClick(holder.mItemView, pos, holder.mPoiInfo);
        });
        holder.setPoiInfo(poiInfo);
        holder.mPoiName.setText(poiInfo.getName());
        String address = poiInfo.getAddress();
        if (TextUtils.isEmpty(address)) {
            holder.mPoiAddress.setVisibility(View.GONE);
        } else {
            holder.mPoiAddress.setText(poiInfo.getAddress());
            holder.mPoiAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return null == mPoiInfos ? 0 : mPoiInfos.size();
    }

    public interface OnSugItemClickListener {
        void onItemClick(View view, int pos, PoiInfo info);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView mPoiNamePreFix;
        public TextView mPoiName;
        public TextView mPoiAddress;
        public PoiInfo mPoiInfo = null;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            mPoiNamePreFix = view.findViewById(R.id.namePrefix);
            mPoiName = view.findViewById(R.id.poiResultName);
            mPoiAddress = view.findViewById(R.id.poiAddress);
        }

        public void setPoiInfo(PoiInfo poiInfo) {
            mPoiInfo = poiInfo;
        }
    }
}
