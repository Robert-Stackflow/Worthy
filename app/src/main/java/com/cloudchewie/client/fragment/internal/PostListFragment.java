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
import com.cloudchewie.client.adapter.PostListAdapter;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class PostListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    List<Post> posts;
    PostListAdapter postListAdapter;
    RecyclerView followingRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            posts.addAll((Collection<? extends Post>) msg.obj);
            postListAdapter.notifyItemInserted(posts.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getPostList(getContext());
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getMoreDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getPostList(getContext());
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_posts, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        posts = new ArrayList<>();
        followingRecyclerView = mainView.findViewById(R.id.fragment_following_recyclerview);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            posts.addAll(DomainUtil.getPostList(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        postListAdapter = new PostListAdapter(getActivity(), posts);
        followingRecyclerView.setAdapter(postListAdapter);
        followingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_following_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_following_swipe_refresh_header);
        footer = mainView.findViewById(R.id.fragment_following_swipe_footer);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setRefreshFooter(footer);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
        swipeRefreshLayout.setOnLoadMoreListener(v -> handler.post(getMoreDatas));
        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
        header.setTextSizeTitle(14);
        footer.setTextSizeTitle(14);
    }

    @Override
    public void performRefresh() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
    }

}
