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
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.adapter.SmallPostListAdapter;
import com.cloudchewie.client.domin.Favorites;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.IconTextItem;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            posts.add((Post) msg.obj);
            postAdapter.notifyItemInserted(posts.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            message.obj = new Post(1, "东方不败", simpleDateFormat.parse("2022-12-13 20:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "北京", "生活圈");
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        ((TextView) findViewById(R.id.favorites_detail_ispublic)).setText(favorites.isPublic() ? "公开" : "私密");
        ((TextView) findViewById(R.id.favorites_detail_describe)).setText("简介:" + favorites.getDescribe());
        ((IconTextItem) findViewById(R.id.favorites_detail_count)).setText(String.valueOf(favorites.getItemCount()));
        ((IconTextItem) findViewById(R.id.favorites_detail_collection)).setText(String.valueOf(favorites.getSubscribeCount()));
        ((IconTextItem) findViewById(R.id.favorites_detail_thumbup)).setText(String.valueOf(favorites.getThumbupCount()));
        ((IconTextItem) findViewById(R.id.favorites_detail_visitor)).setText(String.valueOf(favorites.getVisitorCount()));
        findViewById(R.id.favorites_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.favorites_detail_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        initSwipeRefresh();
        initRecyclerView();
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
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            Post post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        postAdapter = new SmallPostListAdapter(getApplication(), posts);
        favoritesRecyclerView.setAdapter(postAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
    }

    @Override
    public void onClick(View view) {
    }
}