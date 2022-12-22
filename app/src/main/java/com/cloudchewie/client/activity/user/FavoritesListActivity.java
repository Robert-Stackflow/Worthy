/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:11:15
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.user;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.adapter.FavoritesListAdapter;
import com.cloudchewie.client.domin.Favorites;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoritesListActivity extends BaseActivity implements View.OnClickListener {
    List<Favorites> mFavoritesList;
    //基本控件
    TitleBar mTitleBar;
    //主要控件
    RecyclerView mRecyclerView;
    FavoritesListAdapter mAdapter;
    RefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_favorites_list);
        mTitleBar = findViewById(R.id.activity_favorites_list_titlebar);
        mRecyclerView = findViewById(R.id.activity_favorites_list_recyclerview);
        initView();
        initSwipeRefresh();
        initRecyclerView();
    }

    void initView() {
        mTitleBar.setLeftButtonClickListener(v -> finish());
        mTitleBar.setRightButtonClickListener(v -> finish());
    }

    void initRecyclerView() {
        mFavoritesList = new ArrayList<>();
        mFavoritesList.add(new Favorites("执望三千里", "余湍", "主要收藏个人喜欢的一些影视、游戏、音乐赏析或解说", ImageUrlUtil.getUrls(1).get(0), new Date(System.currentTimeMillis()), false, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
        mFavoritesList.add(new Favorites("默认收藏夹", "余湍", "默认收藏夹", ImageUrlUtil.getUrls(1).get(0), new Date(System.currentTimeMillis()), true, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
        mAdapter = new FavoritesListAdapter(getApplication(), mFavoritesList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
    }

    void initSwipeRefresh() {
        mSwipeRefreshLayout = findViewById(R.id.activity_favorites_list_swipe_refresh);
        mSwipeRefreshLayout.setEnableOverScrollDrag(true);
        mSwipeRefreshLayout.setEnableOverScrollBounce(true);
        mSwipeRefreshLayout.setEnableLoadMore(false);
        mSwipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
    }
}