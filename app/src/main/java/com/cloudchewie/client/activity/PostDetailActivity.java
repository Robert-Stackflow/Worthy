/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity;

import static com.cloudchewie.client.util.StringUtil.dealNewLine;
import static com.cloudchewie.client.util.TimeUtil.dateToString;

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
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.fragment.CommentsFragment;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.IconTextItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends BaseActivity {
    Post post;
    PostDetailActivity.PostDetailFragmentAdapter adapter;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        post = (Post) intent.getSerializableExtra("post");
        setContentView(R.layout.activity_post_detail);
        findViewById(R.id.post_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.post_detail_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        tabLayout = findViewById(R.id.post_detail_comment_tab_layout);
        viewPager = findViewById(R.id.post_detail_content_viewpager);
        ((TextView) findViewById(R.id.post_detail_username)).setText(post.getUsername());
        ((TextView) findViewById(R.id.post_detail_time)).setText(dateToString(post.getDate()));
        ((TextView) findViewById(R.id.post_detail_content)).setText(dealNewLine(post.getContent()));
        ((IconTextItem) findViewById(R.id.post_detail_attraction)).setText(post.getLocation());
        ((IconTextItem) findViewById(R.id.post_detail_topic)).setText(post.getTag());
        ((IconTextItem) findViewById(R.id.post_detail_collection)).setText(String.valueOf(post.getCollectionCount()));
        ((IconTextItem) findViewById(R.id.post_detail_comment)).setText(String.valueOf(post.getCommentCount()));
        ((IconTextItem) findViewById(R.id.post_detail_thumbup)).setText(String.valueOf(post.getThumbupCount()));
        findViewById(R.id.post_detail_topic).setOnClickListener(v -> {
            Intent topicDetailIntent = new Intent(PostDetailActivity.this, TopicDetailActivity.class);
            Bundle bundle = new Bundle();
            Topic topic = new Topic(((IconTextItem) findViewById(R.id.post_detail_topic)).getText(), "这里大家可以尽情分享我们的生活", (int) (Math.random() * 100000), (int) (Math.random() * 1000));
            bundle.putSerializable("topic", topic);
            topicDetailIntent.putExtras(bundle);
            startActivity(topicDetailIntent);
        });
        findViewById(R.id.post_detail_attraction).setOnClickListener(v -> {
            Attraction attraction = new Attraction(((IconTextItem) findViewById(R.id.post_detail_attraction)).getText(), "湖北省武汉市洪山区", "凌波门畔，赏日出绝景", 1, Math.random() % 180, Math.random() % 180, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
            Intent attractionDetailIntent = new Intent(PostDetailActivity.this, AttractionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("attraction", attraction);
            attractionDetailIntent.putExtras(bundle);
            startActivity(attractionDetailIntent);
        });
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("热门评论");
        titles.add("全部评论");
        fragments.add(new CommentsFragment());
        fragments.add(new CommentsFragment());
        adapter = new PostDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
    }

    public class PostDetailFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public PostDetailFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
