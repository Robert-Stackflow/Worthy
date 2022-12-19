/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:22:59
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import static com.cloudchewie.client.util.basic.DateUtil.beautifyTime;
import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

import android.content.Intent;
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
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.fragment.internal.CommentListFragment;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.IconTextItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostDetailActivity extends BaseActivity {
    private Post mPost;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    //基础控件
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mUserNameView;
    private TextView mTimeView;
    private TextView mContentView;
    private IconTextItem mTopicView;
    private IconTextItem mLocationView;
    private IconTextItem mCommentsCountView;
    private IconTextItem mThumbupCountView;
    private IconTextItem mCollectionCountView;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private PostDetailFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mBackButton = findViewById(R.id.activity_post_detail_back);
        mMoreButton = findViewById(R.id.activity_post_detail_more);
        mViewPager = findViewById(R.id.activity_post_detail_content_viewpager);
        mTabLayout = findViewById(R.id.activity_post_detail_comment_tab_layout);
        mUserNameView = findViewById(R.id.activity_post_detail_username);
        mTimeView = findViewById(R.id.activity_post_detail_time);
        mContentView = findViewById(R.id.activity_post_detail_content);
        mTopicView = findViewById(R.id.activity_post_detail_topic);
        mLocationView = findViewById(R.id.activity_post_detail_location);
        mCommentsCountView = findViewById(R.id.activity_post_detail_comment_count);
        mThumbupCountView = findViewById(R.id.activity_post_detail_thumbup_count);
        mCollectionCountView = findViewById(R.id.activity_post_detail_collection_count);
        mTabLayout = findViewById(R.id.activity_post_detail_comment_tab_layout);
        mViewPager = findViewById(R.id.activity_post_detail_content_viewpager);
        initView();
        initViewPager();
    }

    private void initView() {
        Intent intent = this.getIntent();
        mPost = (Post) intent.getSerializableExtra("post");
        mUserNameView.setText(mPost.getUsername());
        mTimeView.setText(beautifyTime(mPost.getDate()));
        mContentView.setText(handleLineBreaks(mPost.getContent()));
        mLocationView.setText(mPost.getLocation());
        mTopicView.setText(mPost.getTag());
        mCollectionCountView.setText(String.valueOf(mPost.getCollectionCount()));
        mCommentsCountView.setText(String.valueOf(mPost.getCommentCount()));
        mThumbupCountView.setText(String.valueOf(mPost.getThumbupCount()));
        mBackButton.setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        mTopicView.setOnClickListener(v -> {
            Intent topicDetailIntent = new Intent(PostDetailActivity.this, TopicDetailActivity.class);
            Bundle bundle = new Bundle();
            Topic topic = new Topic(mTopicView.getText(), "这里大家可以尽情分享我们的生活", (int) (Math.random() * 100000), (int) (Math.random() * 1000));
            bundle.putSerializable("topic", topic);
            topicDetailIntent.putExtras(bundle);
            startActivity(topicDetailIntent);
        });
        mLocationView.setOnClickListener(v -> {
            Attraction attraction = new Attraction(mLocationView.getText(), "湖北省武汉市洪山区", "凌波门畔，赏日出绝景", 1, Math.random() % 180, Math.random() % 180, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
            Intent attractionDetailIntent = new Intent(PostDetailActivity.this, AttractionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("attraction", attraction);
            attractionDetailIntent.putExtras(bundle);
            startActivity(attractionDetailIntent);
        });
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.post_detail_comment_tab_titles));
        mTitles.set(mTitles.size() - 1, mTitles.get(mTitles.size() - 1) + "(" + mPost.getCommentCount() + ")");
        mFragments.add(new CommentListFragment());
        mFragments.add(new CommentListFragment());
        mAdapter = new PostDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
        mViewPager.setAdapter(mAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mTitles.get(position))).attach();
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
