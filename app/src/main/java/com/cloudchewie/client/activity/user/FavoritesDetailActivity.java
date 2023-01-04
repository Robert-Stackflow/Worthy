/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:05:46
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.adapter.SmallPostListAdapter;
import com.cloudchewie.client.domin.Favorites;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.fragment.global.ImageViewFragment;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.cloudchewie.client.util.image.CornerTransformation;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.EntryItem;
import com.cloudchewie.ui.IconTextItem;
import com.previewlibrary.GPreviewBuilder;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class FavoritesDetailActivity extends BaseActivity implements View.OnClickListener {
    Favorites favorites;
    List<Post> posts;
    SmallPostListAdapter postAdapter;
    RecyclerView favoritesRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            posts.addAll((List<Post>) msg.obj);
            postAdapter.notifyItemInserted(posts.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getPostList(this);
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_detail);
        Intent intent = this.getIntent();
        favorites = (Favorites) intent.getSerializableExtra("favorites");
        ((TextView) findViewById(R.id.favorites_detail_name)).setText(favorites.getName());
        ((TextView) findViewById(R.id.favorites_detail_author)).setText("创建者:" + favorites.getUsername());
        ((TextView) findViewById(R.id.favorites_detail_describe)).setText("简介:" + favorites.getDescribe());
        ((IconTextItem) findViewById(R.id.favorites_detail_count)).setText(String.valueOf(favorites.getItemCount()));
        ((EntryItem) findViewById(R.id.favorites_detail_collection_count)).setText(String.valueOf(favorites.getFollowerCount()));
        ((EntryItem) findViewById(R.id.favorites_detail_thumbup_count)).setText(String.valueOf(favorites.getThumbupCount()));
        ((EntryItem) findViewById(R.id.favorites_detail_visitor_count)).setText(String.valueOf(favorites.getVisitorCount()));
        findViewById(R.id.favorites_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.favorites_detail_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        findViewById(R.id.favorites_detail_cover).setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            ImageViewInfo mCoverInfo = new ImageViewInfo(favorites.getCoverUrl());
            mCoverInfo.setBounds(NineGridUtil.getBounds((ImageView) findViewById(R.id.favorites_detail_cover)));
            mThumbViewInfoList.add(mCoverInfo);
            GPreviewBuilder.from(FavoritesDetailActivity.this).setUserFragment(ImageViewFragment.class).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        initSwipeRefresh();
        initRecyclerView();
        Glide.with(this).load(favorites.getCoverUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(this, 5, true, true, true, true))).into((ImageView) findViewById(R.id.favorites_detail_cover));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.favorites_detail_swipe_refresh);
        header = findViewById(R.id.favorites_detail_swipe_refresh_header);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
        header.setTextSizeTitle(14);
    }

    void initRecyclerView() {
        posts = new ArrayList<>();
        favoritesRecyclerView = findViewById(R.id.favorites_detail_recyclerview);
        posts.addAll(DomainUtil.getPostList(this));
        postAdapter = new SmallPostListAdapter(getApplication(), posts);
        favoritesRecyclerView.setAdapter(postAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
    }

    @Override
    public void onClick(View view) {
    }
}