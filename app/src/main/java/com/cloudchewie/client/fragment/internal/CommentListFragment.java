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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.CommentListAdapter;
import com.cloudchewie.client.entity.Comment;
import com.cloudchewie.client.fragment.global.BaseFragment;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.cloudchewie.client.util.decoration.DividerItemDecoration;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class CommentListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    CommentListAdapter.OnCommentItemClickListener listener;
    List<Comment> comments;
    CommentListAdapter commentListAdapter;
    RecyclerView commentsRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            comments.addAll((List<Comment>) msg.obj);
            commentListAdapter.notifyItemInserted(comments.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getCommentList(getContext(), 3);
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getMoreDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getCommentList(getContext(), 3);
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CommentListFragment setOnCommentClickListener(CommentListAdapter.OnCommentItemClickListener listener) {
        this.listener = listener;
        if (commentListAdapter != null)
            commentListAdapter.setListener(listener);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_comments, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        comments = new ArrayList<>();
        commentsRecyclerView = mainView.findViewById(R.id.fragment_comments_recyclerview);
        comments.addAll(DomainUtil.getCommentList(getContext(), 3));
        commentListAdapter = new CommentListAdapter(getContext(), comments);
        if (listener != null) {
            commentListAdapter.setListener(listener);
        }
        commentsRecyclerView.setAdapter(commentListAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_comments_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_comments_swipe_refresh_header);
        footer = mainView.findViewById(R.id.fragment_comments_swipe_footer);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setRefreshFooter(footer);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
        swipeRefreshLayout.setOnLoadMoreListener(v -> handler.post(getMoreDatas));
//        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
        header.setTextSizeTitle(14);
        footer.setTextSizeTitle(14);
    }

    @Override
    public void performRefresh() {
        swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
    }
}