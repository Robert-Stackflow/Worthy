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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.DraftListAdapter;
import com.cloudchewie.client.entity.Draft;
import com.cloudchewie.client.fragment.global.BaseFragment;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.cloudchewie.client.util.decoration.SpacingItemDecoration;
import com.cloudchewie.client.util.enumeration.Direction;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class DraftListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    Draft.DRAFT_TYPE type;
    List<Draft> drafts;
    DraftListAdapter draftListAdapter;
    RecyclerView draftlistRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            drafts.addAll((List<Draft>) msg.obj);
            draftListAdapter.notifyItemInserted(drafts.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getDraftList(getContext(), type);
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (Draft.DRAFT_TYPE) getArguments().getSerializable("type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_draft_list, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        drafts = new ArrayList<>();
        draftlistRecyclerView = mainView.findViewById(R.id.fragment_draft_list_recyclerview);
        drafts.addAll(DomainUtil.getDraftList(getContext(), type));
        draftListAdapter = new DraftListAdapter(getActivity(), drafts, type);
        draftlistRecyclerView.setAdapter(draftListAdapter);
        draftlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        draftlistRecyclerView.addItemDecoration(new SpacingItemDecoration(getContext(), 10, Direction.BOTTOM));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_draft_list_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_draft_list_swipe_refresh_header);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
        header.setEnableLastTime(false);
        header.setTextSizeTitle(14);
    }

    @Override
    public void performRefresh() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
    }
}
