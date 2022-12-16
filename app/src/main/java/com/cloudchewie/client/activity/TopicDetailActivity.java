package com.cloudchewie.client.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.fragment.PostsFragment;
import com.cloudchewie.client.ui.BottomSheet;
import com.cloudchewie.client.ui.NoScrollViewPager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends BaseActivity {
    Topic topic;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;
    Toolbar mToolbar;
    TopicDetailFragmentAdapter adapter;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

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
        adapter = new TopicDetailFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class TopicDetailFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList;
        private final List<String> titleList;

        public TopicDetailFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
            super(fragmentManager);
            fragmentList = fragments;
            titleList = titles;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}