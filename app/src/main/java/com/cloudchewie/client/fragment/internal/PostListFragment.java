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
import com.cloudchewie.client.adapter.StaggerPostListAdapter;
import com.cloudchewie.client.entity.Attraction;
import com.cloudchewie.client.entity.Post;
import com.cloudchewie.client.entity.Topic;
import com.cloudchewie.client.fragment.global.BaseFragment;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.cloudchewie.client.util.decoration.StaggerItemDecoration;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    List<Post> posts;
    StaggerPostListAdapter postListAdapter;
    RecyclerView followingRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;
    POSTLIST_TYPE type;
    Object obj = null;
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
        switch (type) {
            case TOPIC:
            case ATTRACTION:
                message.obj = DomainUtil.getPostList(getContext(), obj);
                break;
            case DEFAULT:
                message.obj = DomainUtil.getPostList(getContext());
                break;
        }
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getMoreDatas = () -> {
        Message message = handler.obtainMessage();
        switch (type) {
            case TOPIC:
            case ATTRACTION:
                message.obj = DomainUtil.getPostList(getContext(), obj);
                break;
            case DEFAULT:
                message.obj = DomainUtil.getPostList(getContext());
                break;
        }
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };

    public PostListFragment() {
    }

    public PostListFragment(Object object) {
        this.obj = object;
        if (obj != null) {
            if (obj instanceof Topic) {
                type = POSTLIST_TYPE.TOPIC;
            } else if (obj instanceof Attraction) {
                type = POSTLIST_TYPE.ATTRACTION;
            } else {
                type = POSTLIST_TYPE.DEFAULT;
            }
        } else {
            type = POSTLIST_TYPE.DEFAULT;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            obj = getArguments().getSerializable("obj");
        if (obj != null) {
            if (obj instanceof Topic) {
                type = POSTLIST_TYPE.TOPIC;
            } else if (obj instanceof Attraction) {
                type = POSTLIST_TYPE.ATTRACTION;
            } else {
                type = POSTLIST_TYPE.DEFAULT;
            }
        } else {
            type = POSTLIST_TYPE.DEFAULT;
        }
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
        switch (type) {
            case TOPIC:
            case ATTRACTION:
                posts.addAll(DomainUtil.getPostList(getContext(), obj));
                break;
            case DEFAULT:
                posts.addAll(DomainUtil.getPostList(getContext()));
                break;
        }
        postListAdapter = new StaggerPostListAdapter(getActivity(), posts);
        followingRecyclerView.setAdapter(postListAdapter);
        followingRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        followingRecyclerView.addItemDecoration(new StaggerItemDecoration(getContext(), getResources().getDimensionPixelSize(R.dimen.dp3)));
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

    public enum POSTLIST_TYPE {
        DEFAULT,
        ATTRACTION,
        TOPIC
    }

}
