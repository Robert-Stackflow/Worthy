/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:44:18
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.internal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.AttractionListAdapter;
import com.cloudchewie.client.entity.Attraction;
import com.cloudchewie.client.fragment.global.BaseFragment;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.cloudchewie.client.util.decoration.StaggerItemDecoration;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class AttractionListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    List<Attraction> attractions;
    AttractionListAdapter attractionListAdapter;
    RecyclerView attractionsRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            attractions.addAll((List<Attraction>) msg.obj);
            attractionListAdapter.notifyItemInserted(attractions.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getAttractionList(getContext());
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getMoreDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getAttractionList(getContext());
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClassicsHeader.REFRESH_HEADER_PULLING = "????????????";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "????????????...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "????????????...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "??????????????????";
        ClassicsHeader.REFRESH_HEADER_FINISH = "????????????";
        ClassicsHeader.REFRESH_HEADER_FAILED = "????????????";
        ClassicsHeader.REFRESH_HEADER_SECONDARY = "??????????????????";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "????????????";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "????????????";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "????????????...";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "???????????????";
        ClassicsFooter.REFRESH_FOOTER_PULLING = "????????????";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "????????????";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "??????????????????";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_attractions, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        attractions = new ArrayList<>();
        attractionsRecyclerView = mainView.findViewById(R.id.fragment_attractions_recyclerview);
        attractionListAdapter = new AttractionListAdapter(getActivity(), attractions);
        attractionsRecyclerView.setAdapter(attractionListAdapter);
        attractionsRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        attractionsRecyclerView.addItemDecoration(new StaggerItemDecoration(getContext(), getResources().getDimensionPixelSize(R.dimen.dp3)));
    }

    @Override
    public void performRefresh() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.autoRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_attractions_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_attractions_swipe_refresh_header);
        footer = mainView.findViewById(R.id.fragment_attractions_swipe_footer);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setRefreshFooter(footer);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(refreshlayout -> handler.post(getRefreshDatas));
        swipeRefreshLayout.setOnLoadMoreListener(refreshlayout -> handler.post(getMoreDatas));
        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
    }

    @Override
    public void onClick(View view) {
    }
}
