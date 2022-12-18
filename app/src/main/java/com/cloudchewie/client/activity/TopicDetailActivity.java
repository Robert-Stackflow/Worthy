/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.fragment.PostsFragment;
import com.cloudchewie.ui.BottomSheet;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends BaseActivity {
    Topic topic;
    Toolbar mToolbar;
    TopicDetailFragmentAdapter adapter;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        topic = (Topic) intent.getSerializableExtra("topic");
        setContentView(R.layout.activity_topic_detail);
        findViewById(R.id.topic_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.topic_detail_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        ((TextView) findViewById(R.id.topic_detail_name)).setText(topic.getName());
        ((TextView) findViewById(R.id.topic_detail_describe)).setText(topic.getDescribe());
        ((TextView) findViewById(R.id.topic_detail_hotvalue)).setText(topic.getHotvalue() + "热度 · " + topic.getFollowerCount() + "人关注");
        tabLayout = findViewById(R.id.topic_detail_content_tab_layout);
        viewPager = findViewById(R.id.topic_detail_content_viewpager);
        fragments = new ArrayList<>(2);
        titles = new ArrayList<>(2);
        titles.add("帖子");
        titles.add("文章");
        fragments.add(new PostsFragment());
        fragments.add(new PostsFragment());
        adapter = new TopicDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
    }

    public String getTopic() {
        return topic.getName();
    }

    public class TopicDetailFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public TopicDetailFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            fragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}