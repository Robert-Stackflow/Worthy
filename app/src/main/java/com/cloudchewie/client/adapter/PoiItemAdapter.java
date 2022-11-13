package com.cloudchewie.client.adapter;

import java.util.List;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.cloudchewie.client.R;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PoiItemAdapter extends RecyclerView.Adapter<PoiItemAdapter.MyViewHolder> {
    private List<SuggestionResult.SuggestionInfo> mSuggestInfos = null;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public PoiItemAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public PoiItemAdapter(List<SuggestionResult.SuggestionInfo> suggestInfoList) {
        mSuggestInfos = suggestInfoList;
        notifyDataSetChanged();
    }

    public SuggestionResult.SuggestionInfo getItemSuggestInfo(int pos) {
        if (pos < 0 || null == mSuggestInfos || pos >= mSuggestInfos.size()) {
            return null;
        }
        return mSuggestInfos.get(pos);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<SuggestionResult.SuggestionInfo> suggestInfos) {
        if (null == suggestInfos)
            return;
        mSuggestInfos = suggestInfos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_poi_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == mSuggestInfos) {
            return;
        }
        if (position < 0 || position >= mSuggestInfos.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        if (position < 0 || position >= mSuggestInfos.size()) {
            return;
        }
        final SuggestionResult.SuggestionInfo suggestInfo = mSuggestInfos.get(position);
        if (null == suggestInfo) {
            return;
        }
        holder.mItemView.setOnClickListener(v -> {
            if (null == mOnItemClickListener) {
                return;
            }
            int pos = holder.getAdapterPosition();
            long id = holder.getItemId();
            mOnItemClickListener.onItemClick(null, holder.mItemView, pos, id);
        });

        holder.setSuggestInfo(suggestInfo);
        holder.mPoiName.setText(suggestInfo.getKey());

        String address = suggestInfo.getAddress();
        if (TextUtils.isEmpty(address)) {
            holder.mPoiAddress.setVisibility(View.GONE);
        } else {
            holder.mPoiAddress.setText(suggestInfo.getAddress());
            holder.mPoiAddress.setVisibility(View.VISIBLE);
        }
        if (position == 0)
            holder.mItemView.setBackground(holder.mItemView.getResources().getDrawable(R.drawable.shape_card_top_radius));
        if (position == getItemCount() - 1)
            holder.mItemView.setBackground(holder.mItemView.getResources().getDrawable(R.drawable.shape_card_bottom_radius));
        if (position == 0 && position == getItemCount() - 1)
            holder.mItemView.setBackground(holder.mItemView.getResources().getDrawable(R.drawable.shape_card_round));
    }

    @Override
    public int getItemCount() {
        return null == mSuggestInfos ? 0 : mSuggestInfos.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public View mLocateImg;
        public TextView mPoiNamePreFix;
        public TextView mPoiName;
        public TextView mPoiAddress;
        public SuggestionResult.SuggestionInfo mSuggestInfo = null;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            mLocateImg = view.findViewById(R.id.imgLocate);
            mPoiNamePreFix = view.findViewById(R.id.namePrefix);
            mPoiName = view.findViewById(R.id.poiResultName);
            mPoiAddress = view.findViewById(R.id.poiAddress);
        }

        public void setSuggestInfo(SuggestionResult.SuggestionInfo suggestInfo) {
            mSuggestInfo = suggestInfo;
        }

    }

}
