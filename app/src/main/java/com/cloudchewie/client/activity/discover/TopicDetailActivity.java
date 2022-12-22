/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:57:46
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.fragment.internal.PostListFragment;
import com.cloudchewie.ui.BottomSheet;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopicDetailActivity extends BaseActivity {
    private Topic mTopic;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mNameView;
    private TextView mDescribeView;
    private TextView mHotValueView;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private TopicDetailFragmentAdapter mAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        mBackButton = findViewById(R.id.activity_topic_detail_back);
        mMoreButton = findViewById(R.id.activity_topic_detail_more);
        mNameView = findViewById(R.id.activity_topic_detail_name);
        mDescribeView = findViewById(R.id.activity_topic_detail_describe);
        mHotValueView = findViewById(R.id.activity_topic_detail_hotvalue);
        mTabLayout = findViewById(R.id.activity_topic_detail_content_tab_layout);
        mViewPager = findViewById(R.id.activity_topic_detail_content_viewpager);
        initView();
        initViewPager();
    }

    @SuppressLint("SetTextI18n")
    void initView() {
        mBackButton.setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        Intent intent = this.getIntent();
        mTopic = (Topic) intent.getSerializableExtra("topic");
        mNameView.setText(mTopic.getName());
        mDescribeView.setText(mTopic.getDescribe());
        mHotValueView.setText(mTopic.getVisitorCount() + "热度 · " + mTopic.getFollowerCount() + "人关注");
    }

    void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.topic_detail_tab_titles));
        mFragments.add(new PostListFragment());
        mFragments.add(new PostListFragment());
        mAdapter = new TopicDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
        mViewPager.setAdapter(mAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mTitles.get(position))).attach();
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setLongClickable(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tab.view.setTooltipText(null);
                }
            }
        }
    }

    public String getTopic() {
        return mTopic.getName();
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